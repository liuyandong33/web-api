<%--
  Created by IntelliJ IDEA.
  User: liuyandong
  Date: 2019-05-10
  Time: 9:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登录页面</title>
    <style type="text/css">
        body {
            margin: 0;
        }
    </style>
    <script type="text/javascript" src="http://www.smartpos.top/lib/jquery/jquery.1.11.3.min.js"></script>
    <script type="text/javascript">
        function login() {
            var username = $("#username").val();
            var password = $("#password").val();
            var loginMode = $("#loginMode").val();
            var params = {username: username, password: password, loginMode: loginMode};
            if ($("#rememberMe").prop("checked")) {
                params["remember-me"] = "on";
            }
            debugger
            $.post("/auth/login", params, function (result) {
                alert(result);
            }, "text");
        }
    </script>
</head>
<body>

<div style="text-align: center;">
    <form method="post" action="/auth/login">
        用户名：<input type="text" id="username" name="username"><br><br>
        密码： <input type="password" id="password" name="password"><br><br>
        登录方式：
        <select name="loginMode" id="loginMode">
            <option value="PASSWORD">密码</option>
            <option value="SMS_VERIFICATION_CODE">短信验证码</option>
        </select><br><br>
        记住我：<input type="checkbox" name="remember-me" id="rememberMe"><br><br>
        <input type="submit" value="登录">
    </form>

    <button onclick="login()">Ajax登录</button>
</div>

</body>
</html>
