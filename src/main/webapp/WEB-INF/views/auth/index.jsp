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
    <script type="text/javascript" src="http://www.smartpos.top/lib/jquery/jquery.1.11.3.min.js"></script>
    <script type="text/javascript">
        function login() {
            $.post("/login/login", {username: "admin", password: "123456", loginMode: "PASSWORD"}, function (result) {
                alert(result);
            }, "text");
        }
    </script>
</head>
<body>

    <form method="post" action="/login/login">
        用户名：<input type="text" name="username"><br><br>
        密码： <input type="password" name="password"><br><br>
        登录方式：
        <select name="loginMode">
            <option value="PASSWORD">密码</option>
            <option value="SMS_VERIFICATION_CODE">短信验证码</option>
        </select><br><br>
        <input type="submit" value="登录">
    </form>

<button onclick="login()">Ajax登录</button>

</body>
</html>
