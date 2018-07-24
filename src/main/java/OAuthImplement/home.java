package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import OAuthImplement.Constants;

public class home extends HttpServlet {

    String code ;
    String accessToken ;
    String idToken ;
    String name;
    String scope;
    String sessionState ;
    String error ;
    String grantType ;
    String authEndpoint;
    String callbackURI ;
    String accessTokenEndpoint ;

    public void init() throws ServletException{

        scope = "openid";
        grantType = "code";
        authEndpoint = "https://localhost:9443/oauth2/authorize";
        callbackURI = "http://localhost:8080/OAuthDemoApp/home.jsp";
        accessTokenEndpoint = "https://localhost:9443/oauth2/token";


    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "OAuth Demo";

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +"<body bgcolor = \"#f0f0f0\">\n" +
                    "<h2>OAuth Prototype App</h2>\n"+
                    "<form action=\"AuthCodeURL\" method=\"get\" name=\"OAuthRequest\" id=\"OAuthRequest\"autocomplete=\"on\">\n"+
                        "<table class=\"user_pass_table\">\n"+
                            "<tr>" +
                                "<td>Grant Type : </td><td><select id=\"grantType\" name=\"response_type\">\n"+
                                    "<option value=\"" + Constants.OAUTH2_GRANT_TYPE_CODE + "\" selected=\"selected\">Authorization Code</option>" +
                                    "<option value=\"" + Constants.OAUTH2_GRANT_TYPE_IMPLICIT + "\">Implicit</option></select>" +
                                "</td>" +
                            "</tr>\n"+
                            "<tr>" +
                                "<td>Scope : </td><td colspan=\"4\"><input type=\"text\" name=\"scope\" id=\"scope\" value="+scope+" size=\"50\" readonly></td>" +
                            "</tr>\n"+
                            "<tr>" +
                                "<td>Client Code : </td><td><input type=\"text\" name=\"clientCode\" id=\"client_id\" placeholder=\"Enter clientCode\"  size=\"50\" ></td>" +
                            "</tr>\n"+
                            "<tr>" +
                                "<td>Authorization End Point : </td><td colspan=\"4\"><input type=\"text\" name=\"authEndpoint\" id=\"authEndpoint\" value="+authEndpoint+" size=\"50\" readonly></td>" +
                            "</tr>\n"+
                            "<tr>" +
                                "<td>Callback URI : </td><td colspan=\"4\"><input type=\"text\" name=\"redirect_uri\" id=\"callbackURI\" placeholder=\"Enter callback URI\"  size=\"50\" ></td>" +
                            "</tr>\n"+
                            "<tr>" +
                                "<td colspan=\"2\"><input type=\"submit\" name=\"authorize\" value=\"Authorize\" onclick=\"alertVal()\"/></td>" +
                            "</tr>" +
                        "</table>" +
                    "</form>\n"+
                "</body>"+
                "</html>"
        );
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }


}
