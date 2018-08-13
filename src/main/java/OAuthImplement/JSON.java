package OAuthImplement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class JSON extends HttpServlet {


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String grant_type;

        QueryBuilder tokenBuilder = new QueryBuilder();
        HttpSession session = request.getSession(false);

        grant_type = (String)session.getAttribute("grant_type");

        if (grant_type.equals("refresh_token")) {

            tokenBuilder.append("grant_type", grant_type);
            tokenBuilder.append("refresh_token", (String) session.getAttribute("refresh_token"));
            tokenBuilder.append("client_secret", (String)session.getAttribute("client_secret"));
            tokenBuilder.append("client_id", (String) session.getAttribute("client_id"));


        } else {

            String tokenEndpoint = request.getParameter("tokenEndpoint");
            session.setAttribute("tokenEndpoint",tokenEndpoint);
            session.setAttribute("client_secret",request.getParameter("client_secret"));

            String authCode = request.getParameter("code");
            String redirect_uri = request.getParameter("redirect_uri");

            String code_verifier = request.getParameter("client_secret");


            tokenBuilder.append("grant_type", "authorization_code");
            tokenBuilder.append("code", authCode);
            tokenBuilder.append("redirect_uri", redirect_uri);
            tokenBuilder.append("client_id", (String) session.getAttribute("client_id"));
            tokenBuilder.append("client_secret", code_verifier);

        }
        String url = tokenBuilder.returnQuery((String)session.getAttribute("tokenEndpoint"));
        System.out.println(url);
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("POST");
        //add request header
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer re = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                re.append(inputLine);
            }
            in.close();

            //Read JSON response and print
            JSONObject myResponse = new JSONObject(re.toString());
//        System.out.println(myResponse);
//        if (myResponse != null) {
//            session.setAttribute("grant_type", "token");
//        }
            session.setAttribute("access_token", myResponse.getString("access_token"));
            session.setAttribute("refresh_token", myResponse.getString("refresh_token"));


            response.setContentType("text/html");

            PrintWriter out = response.getWriter();
            String title = "OAuth Demo";

            String docType =
                    "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

            out.println(docType +
                    "<html>\n" +
                    "<head>" +
                    "<title>" + title + "</title>"

            );
            if(grant_type.equals("refresh_token")) {
                out.println(
                        "<script type=\"text/javascript\">\n"+
                        "window.onload = function(){ alert(\"Refreshed Access Token\");}\n" +
                        "</script>"
                );
            }
            out.println(
                    "</head>\n" +
                    "<body>\n" +
                    "<h2>Getting Access Token and ID Token for Authorization Code</h2>\n" +
                    "<form action=\"\" id=\"tokenForm\" method=\"get\">\n" +
                    "<table class=\"user_pass_table\">\n" +
                    "<tr>\n" +
                    "<td><label>Access Token :</label></td>\n" +
                    "<td><input id=\"accessToken\" name=\"accessToken\" value=" + myResponse.getString("access_token") + " style=\"width:450px\" />\n" +
                    "<td><input id=\"accessToken1\" name=\"accessToken1\"  style=\"width:450px\" hidden/>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "var idToken = \"" + myResponse.getString("access_token") + "\";\n" +
                    "var accessToken = atob(idToken.split(\".\")[1]);\n" +
                    "document.getElementById(\"accessToken1\").value = accessToken;\n" +
                    "</script>" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td><label>Refresh Token :</label></td>\n" +
                    "<td><input id=\"refreshToken\" name=\"refreshToken\" value=" + myResponse.getString("refresh_token") + " style=\"width:450px\" />\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td><label>Scope :</label></td>\n" +
                    "<td><input id=\"scope\" name=\"scope\" value=" + myResponse.getString("scope") + " style=\"width:450px\" readonly/>\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td><label>ID Token :</label></td>\n" +
                    "<td><textarea id=\"idToken\" name=\"idToken\"  style=\"width:450px\" rows=\"20\" readonly>" + myResponse.getString("id_token") + " </textarea>\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td><label>Token Type :</label></td>\n" +
                    "<td><input id=\"tokenType\" name=\"tokenType\" value=" + myResponse.getString("token_type") + " readonly />\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<td><label>Expires In :</label></td>\n" +
                    "<td><input id=\"expiresIn\" name=\"expiresIn\" value=" + myResponse.getInt("expires_in") + " readonly />\n" +
                    "</tr>\n" +
                    "<tr>\n" +
                    "<tr>" +
                    "<td colspan=\"2\"><input type=\"submit\" name=\"tokenInfo\" value=\"Token Info\" formaction=\"TokenInfo\"/></td>" +
                    "<td colspan=\"2\"><input type=\"submit\" name=\"tokenInfo\" value=\"User Info\" formaction=\"UserInfo\"/></td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"2\"><input type=\"submit\" name=\"introspect\" value=\"Access Resource\" formaction=\"ProtectedResource\"/></td>" +
                    "<td colspan=\"2\"><input type=\"submit\" name=\"validate\" value=\"Validate\" formaction=\"TokenVerifier\"/></td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td>" +
                    "<input id=\"decodedToken\" name=\"decodedToken\" hidden/></td>\n" +
                    "<script type=\"text/javascript\">\n" +
                    "var idToken = \"" + myResponse.getString("id_token") + "\";\n" +
                    "var decodedToken = atob(idToken.split(\".\")[1]);\n" +
                    "document.getElementById(\"decodedToken\").value = decodedToken;\n" +
                    "</script>" +
                    "</td>" +
                    "</tr>" +
                    "</table>\n" +
                    "</form>" +
                    "</body>\n" +
                    "</html>"
            );

        } catch (Exception e) {
            System.out.println(e);
            response.sendRedirect("home?errorMessage=Inputs Not Valid");
        }


    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }


}