<!DOCTYPE html>
<html>
<body>
    <h2>Basic Authentication for Resource</h2>
    <form action="ProtectedResource" method="post">
        <table class="user_pass_table">
            <tr>
                <td>Username </td><td>: <input type="text" name="username" id="username" value="" size="50"/></td>
            </tr>
            <tr>
                <td>Password </td><td>: <input type="password" name="password" id="password" value="" size="50"/></td>
            </tr>
            <tr>
                <td><input type="submit" value="Get Resource"></td>
            </tr>
        </table>
     </form>
     <%
        if("invalid".equals(request.getParameter("error"))){
     %>
     <p><font color="red">Invalid Credentials!</font></p>

     <%
        }
     %>
</body>
</html>

<style>
table {
    font-family: arial, sans-serif;
    border-collapse: collapse;
    width: 100%;
}

td, th {
    text-align: left;
    padding: 8px;
}
</style>