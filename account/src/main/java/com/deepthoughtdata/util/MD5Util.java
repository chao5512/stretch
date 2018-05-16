package com.deepthoughtdata.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @ClassName MD5Util
 * @Description MD5+salt加密工具
 * @Auther: 王培文
 * @Date: 2018/5/15 
 * @Version 1.0
 **/
public class MD5Util {
    private static final String HEX_NUMS_STR="0123456789ABCDEF";
    private static final int SALT_LENGTH = 6;
    private static final String ALGORITHM = "MD5";
    /**
     * 功能描述:加密方法
     * @param rawPass
     * @return: java.lang.String
     * @auther: 王培文
     * @date: 2018/5/15 16:43
     */
    public static String encode(String rawPass,String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String result = null;
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        // 加密后的字符串
        result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass,salt).getBytes("utf-8")));
        return result;
    }

    /**
     * 功能描述:将原始密码+salt进行MD5加密，并与数据库中加密后的密码进行比较
     * @param encPass
     * @param rawPass
     * @param salt
     * @return: boolean
     * @auther: 王培文
     * @date: 2018/5/15 17:09
     */
    public static boolean isPasswordValid(String encPass, String rawPass,String salt) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass,salt);

        return pass1.equals(pass2);
    }


    /**
     * 功能描述:使用默认盐长度获取盐值
     * @param 
     * @return: java.lang.String
     * @auther: 王培文
     * @date: 2018/5/15 17:05
     */
    public static String createSalt(){
        int length = SALT_LENGTH;
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 功能描述:将密码与盐值进行合并
     * @param password
     * @return: java.lang.String
     * @auther: 王培文
     * @date: 2018/5/15 16:45
     */
    private static String mergePasswordAndSalt(String password,String salt) {

        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 功能描述:将byte[]转换为十六进制字符串
     * @param b
     * @return: java.lang.String
     * @auther: 王培文
     * @date: 2018/5/15 17:11
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 功能描述:将十六进制字符串转换为byte[]
     * @param hex
     * @return: byte[]
     * @auther: 王培文
     * @date: 2018/5/15 17:28
     */
    public static byte[] hexStringToByteArray(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4
                    | HEX_NUMS_STR.indexOf(hexChars[pos + 1]));
        }
        return result;
    }
}
