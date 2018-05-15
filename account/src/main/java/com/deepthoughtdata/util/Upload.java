package com.deepthoughtdata.util;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

public class Upload {

    //hadoop默认文件系统
    public static final String FS_DEFAULT_FS = UploadConfigUtil.getConfigByKey("defaultFs");

    //host主机地址
    public static final String HDFS_HOST = UploadConfigUtil.getConfigByKey("host");

    //完成文件系统FileSystem的初始化

    public static FileSystem fs = null;

    /**
     * 功能描述:获取hadoop的文件系统对象
     * @param
     * @return: org.apache.hadoop.fs.FileSystem
     * @auther: 王培文
     * @date: 2018/5/14 20:27
     */
    public static FileSystem getFileSystem() throws IOException {
        if (fs == null){
            try {
                //创建文件配置对象
                Configuration conf = new Configuration();
                conf.set(FS_DEFAULT_FS, HDFS_HOST);
                //获取文件系统
                fs = FileSystem.get(conf);
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
        return fs;
    }

    /**
     * 功能描述: 文件上传
     *
     * @param: [sourceFileName 原始文件名, destPath 目的路径]
     * @return: string 图片存储路径
     * @auther: 王培文
     * @date: 2018/5/11 10:26
     */
    public static String uploadFile(String sourceFileName, String destPath) throws IOException {

        Path source = new Path(sourceFileName);

        //获取文件系统
        FileSystem fs = getFileSystem();

        //获取文件后缀名
        String suffixFileName = sourceFileName.substring(sourceFileName.lastIndexOf("."), sourceFileName.length());

        //自定义存储的图片文件名
        String destFileName = new Date().getTime() + "_" + new Random().nextInt(1000) + suffixFileName;

        //判断文件夹是否存在

        String fileAdd = DateUtils.formatDateToString(new Date(), DateUtils.DATE_FORMAT_TIME_YMD);

        Path path = new Path(destPath + "/" + fileAdd);

        //如果文件夹存在则不创建

        if (!fs.exists(path) && !fs.isDirectory(path)) {
            fs.mkdirs(path);
        }

        //构造最终的上传路径
        String destStr = path.getParent()+"/"+path.getName() + "/" + destFileName;

        Path dest = new Path(destStr);

        //上传路径
        fs.copyFromLocalFile(true, false, source, dest);

        return destStr;

    }
}
