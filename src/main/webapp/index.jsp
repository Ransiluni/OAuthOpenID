<%
    String code = null;
    String accessToken = null;
    String idToken = null;
    String name = null;
    String scope = "openid";
    String sessionState = null;
    String error = null;
    String grantType = "code";
    String authEndpoint = "https://localhost:9443/oauth2/authorize";
    String callbackURI = "http://localhost:8080/OAuthDemoApp/home.jsp";
    String accessTokenEndpoint = "https://localhost:9443/oauth2/token";




%>

<!DOCTYPE html>
<html>
<body>
    <h2>OAuth Prototype App</h2>


    <form action="authorizeUser.jsp" method="post" name="OAuthRequest">
        <table class="user_pass_table">
            <tr>
                <td>Grant Type : </td>
                <td>
                    <select id="grantType" name="grantType" onchange="setVisibility();">
                        <option value="<%=grantType%>" selected="selected">Authorization Code</option>
                         <option value="implicit">Implicit</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Scope : </td>
                <td colspan="4"><input type="text" name="scope" value="<%=scope%>" size="50" readonly></td>
            </tr>
            <tr>
                <td>Client Code : </td>
                <td><input type="text" name="clientCode" placeholder="Enter clientCode"  size="50" ></td>
            </tr>
            <tr>
                <td>Authorization End Point : </td>
                <td colspan="4"><input type="text" name="authEndpoint" value="<%=authEndpoint%>" size="50" readonly></td>
            </tr>
            <tr>
                <td>Callback URI : </td>
                <td colspan="4"><input type="text" name="callbackURI" placeholder="Enter callback URI"  size="50" ></td>
            </tr>
            <tr>
                <td colspan="2"><input type="submit" name="authorize" value="Authorize"></td>
            </tr>
        </table>
    </form>





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