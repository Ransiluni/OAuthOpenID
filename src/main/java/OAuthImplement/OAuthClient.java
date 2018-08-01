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

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
            "<html>\n" +
            "<head>" +
                "<title>" + title + "</title>\n" +
            "</head>\n" +
            "<body>\n" +
                "<h2>Getting Access Token and ID Token</h2>\n" +
                "<form action=\"JSON\" method=\"post\" name=\"TokenRequest\" id=\"TokenRequest\">\n"+
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

            "</body>\n" +
            "</html>"
        );
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
