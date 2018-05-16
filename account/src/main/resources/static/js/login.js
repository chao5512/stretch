function login() {
        var oError = document.getElementById("error_box");
        var username = $("#username").val();
        var password = $("#password").val();
        console.log(username+"   "+password);
        $.ajax({
            url: "/user/login",
            data: {"username" : username,
                "password" :password
            },
            dataType : 'json',
            type : 'post',
            success :function (data){
                console.log(data);
                if(data.code == 0){
                    window.location.href=""
                }else {
                    oError.innerHTML = data.msg;
                }

            },
            error : function(data) {
                console.log(1111111111+data)
            },
        });
    }

var login = function () {
    //登录处理方法
    var handleLogin = function () {
        //表单验证
        $('.login-form').validate({
            debug: false,
            onkeyup: true,
            rules: {
                username: {
                    required: true,
                    email: true,
                    remote: {
                        type: "POST",
                        url: "/user/login",
                        data: {
                            username: function(){ return $("#username").val(); }
                        },
                    }
                },
                password: {
                    required: true,
                    minlength : 6
                }
            },
            messages: {
                username: {
                    required: "帐号不得为空！",
                    email: "请输入正确的邮箱格式！",
                    remote: "用户不存在"
                },
                password: {
                    required: "密码不得为空！",
                    minlength: "长度至少六位！"
                }
            },

        });

    }


    return {
        init: function () {

            handleLogin();

        }
    };


}();

jQuery(document).ready(function() {
    login.init();
});

