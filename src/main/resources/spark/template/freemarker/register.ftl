<html>
<head>
    <meta charset="utf-8"/>
    <title>Register</title>
</head>
<body>
    Register
    <form method="post">
        <input type="text" name="username" value="" />
        <p />
        <input type="password" name="password" value="" />
        <p />
        <input type="submit" value="Submit" />
    </form>

    <#if msg??>
        ${msg}
    </#if>
</body>
</html>
