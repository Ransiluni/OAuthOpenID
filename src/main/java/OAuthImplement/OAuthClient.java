package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class OAuthClient extends HttpServlet {

    String tokenEndpoint = "https://localhost:9443/oauth2/token";
    String redirect_uri;
    String grant_type;
    String client_id;


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        String title = "Get Tokens";

        HttpSession session = request.getSession(false);
        redirect_uri = (String)session.getAttribute("redirect_uri");
        grant_type = (String)session.getAttribute("grant_type");
        client_id = (String)session.getAttribute("client_id");

        if("authorization_code".equals(grant_type)){
            String docType =
                    "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

            out.println(docType +
                    "<html>\n" +
                    "<head>" +
                    "<title>" + title + "</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                        "<h2>Getting Access Token and ID Token</h2>\n" +
                        "<form action=\"\" method=\"post\" name=\"TokenRequest\" id=\"TokenRequest\">\n"+
                            "<table class=\"user_pass_table\">\n"+
                                "<tr>" +
                                    "<td><input type=\"text\" name=grant_type value=\"" + grant_type + "\" size=\"50\" readonly hidden></td>\n"+
                                "</tr>\n"+
                                "<tr>" +
                                    "<td>Authorization Code  </td><td>: <input type=\"text\" name=code value=" + request.getParameter("code") + " size=\"50\" readonly></td>\n"+
                                "</tr>\n"+
                                "<tr>" +
                                    "<td>Callback URI  </td><td colspan=\"4\">: <input type=\"text\" name=\"redirect_uri\" id=\"redirect_uri\" value=\"" + redirect_uri +"\" size=\"50\" ></td>" +
                                "</tr>\n"+
                                "<tr>" +
                                    "<td>Token Endpoint  </td><td colspan=\"4\">:   <input type=\"text\" name=\"tokenEndpoint\" id=\"tokenEndpoint\" value=\"" + tokenEndpoint + "\" size=\"50\" ></td>" +
                                "</tr>\n"+
                                "<tr>" +
                                    "<td>Client ID  </td><td>:  <input type=\"text\" name=\"client_id\" id=\"client_id\" value=\"" + client_id +"\"size=\"50\" readonly></td>" +
                                "</tr>\n"+
                                "<tr>" +
                                    "<td>Client Secret  </td><td>:  <input type=\"password\" name=\"client_secret\" id=\"client_secret\" size=\"50\" ></td>" +
                                "</tr>\n"+
                                "<tr>" +
                                    "<td colspan=\"2\"><input type=\"submit\" name=\"getTokens\" value=\"Get Tokens\" onclick=\"changeEnd()\"></td>" +
                                "</tr>" +
                            "</table>" +
                        "</form>\n" +
                    "<script>\n" +
                    "function changeEnd(){" +
                        "val = document.getElementById(\"tokenEndpoint\").value;" +
                        "document.getElementById(\"TokenRequest\").action = val;" +
                    "}" +
                    "</script>\n"+
                    "</body>\n" +
                    "</html>"
            );
        }

        if("token".equals(grant_type)){
            String docType =
                    "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

            out.println(docType +
                    "<html>\n" +
                    "<head>" +
                        "<title>Implicit Tokens</title>\n" +
                        "<script>\n" +
                            "function getAccessToken() {\n" +
                                "var fragment = window.location.hash.substring(1);\n" +
                                "if (fragment.indexOf(\"&\") > 0) {\n" +
                                    "var arrParams = fragment.split(\"&\");\n" +
                                    "var i = 0;\n" +
                                    "for (i = 0; i < arrParams.length; i++) {\n" +
                                        "var sParam = arrParams[i].split(\"=\");\n" +
                                        "if (sParam[0] == \"access_token\") {\n" +
                                            "return sParam[1];\n" +
                                        "}\n" +
                                    "}\n" +
                                "}\n" +
                                "return \"\";\n" +
                            "}\n"+
                            "function getIDToken() {\n" +
                                "var fragment = window.location.hash.substring(1);\n" +
                                "if (fragment.indexOf(\"&\") > 0) {\n" +
                                    "var arrParams = fragment.split(\"&\");\n" +
                                    "for (var i = 0; i < arrParams.length; i++) {\n" +
                                        "var urlParameters = arrParams[i].split(\"=\");\n" +
                                        "if (urlParameters[0] == \"id_token\") {\n" +
                                            "var idToken = urlParameters[1];\n" +
                                            "return idToken;\n" +
                                        "}\n" +
                                    "}\n" +
                                "}\n" +
                                "return \"\";\n" +
                            "}" +
                            "function getTokenType() {\n" +
                                "var fragment = window.location.hash.substring(1);\n" +
                                "var arrParams = fragment.split(\"&\");\n" +
                                "for (var i = 0; i < arrParams.length; i++) {\n" +
                                    "var urlParameters = arrParams[i].split(\"=\");\n" +
                                    "if (urlParameters[0] == \"token_type\") {\n" +
                                        "var token_type = urlParameters[1];\n" +
                                        "return token_type;\n" +
                                    "}\n" +
                                "}\n" +
                                "return \"\";\n" +
                            "}" +
                            "function getExpire() {\n" +
                                "var fragment = window.location.hash.substring(1);\n" +
                                "var arrParams = fragment.split(\"&\");\n" +
                                "for (var i = 0; i < arrParams.length; i++) {\n" +
                                    "var urlParameters = arrParams[i].split(\"=\");\n" +
                                    "if (urlParameters[0] == \"expires_in\") {\n" +
                                        "var expires = urlParameters[1];\n" +
                                        "return expires;\n" +
                                    "}\n" +
                                "}\n" +
                                "return \"\";\n" +
                            "}" +
                            "function decodeToken(){\n" +
                                "var idToken = getIDToken();\n" +
                                "var decodedToken = atob(idToken.split(\".\")[1]);\n" +
                                "return decodedToken\n" +
                            "}\n" +
                        "</script>\n" +
                    "</head>\n" +
                    "<body>\n" +
                        "<h2>Getting Access Token and ID Token for Implicit Type</h2>\n" +
                        "<form action=\"TokenInfo\" id=\"tokenForm\" method=\"get\">\n" +
                            "<table class=\"user_pass_table\">\n" +
                                "<tr>\n" +
                                    "<td><label>Access Token :</label></td>\n" +
                                    "<td><input id=\"accessToken\" name=\"accessToken\" style=\"width:450px\"/>\n" +
                                         "<script type=\"text/javascript\">\n" +
                                            "document.getElementById(\"accessToken\").value = getAccessToken();\n" +
                                         "</script>" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>ID Token :</label></td>\n" +
                                    "<td><textarea id=\"idToken\" name=\"idToken\" style=\"width:450px\" rows=\"20\" ></textarea>\n" +
                                        "<script type=\"text/javascript\">\n" +
                                            "document.getElementById(\"idToken\").value = getIDToken();\n" +
                                        "</script>" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>Token Type :</label></td>\n" +
                                    "<td><input id=\"tokenType\" name=\"tokenType\"/>\n" +
                                        "<script type=\"text/javascript\">\n" +
                                            "document.getElementById(\"tokenType\").value = getTokenType();\n" +
                                        "</script>" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>Expires In :</label></td>\n" +
                                    "<td><input id=\"expiresIn\" name=\"expiresIn\"/>\n" +
                                        "<script type=\"text/javascript\">\n" +
                                            "document.getElementById(\"expiresIn\").value = getExpire();\n" +
                                        "</script>" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><input id=\"decodedToken\" name=\"decodedToken\" hidden/></td>\n" +
                                         "<script type=\"text/javascript\">\n" +
                                            "document.getElementById(\"decodedToken\").value = decodeToken();\n" +
                                         "</script>" +
                                "</tr>\n" +
                                "<tr>" +
                                    "<td colspan=\"2\"><input type=\"submit\" name=\"tokenInfo\" value=\"Token Info\"/></td>" +
                                "</tr>" +
                            "</table>\n" +
                        "</form>" +
                    "</body>\n" +
                    "</html>"
            );
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
