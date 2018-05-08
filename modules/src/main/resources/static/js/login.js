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
