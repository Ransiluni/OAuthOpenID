package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class Introspection extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "User Details";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        String access_Token = request.getParameter("accessToken");

        //build url
        QueryBuilder codeBuilder = new QueryBuilder();
        codeBuilder.append("token", access_Token);

        String EndPoint = " https://localhost:9443/oauth2/introspect";
        String url = codeBuilder.returnQuery(EndPoint);


        URL object = new URL(url);
        HttpURLConnection con = (HttpURLConnection) object.openConnection();

        // optional default is GET
        con.setRequestMethod("POST");

        //add request header
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Authorization", "Bearer " + access_Token);
        //con.setRequestProperty("Accept", "application/json");


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer re = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                re.append(inputLine);
            }
            in.close();

            //Read JSON response and print
            org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());

            out.println(docType +
                    "<html>\n" +
                    "<head>" +
                        "<title>" + title + "</title>" +
                    "</head>\n" +
                    "<body>\n"

            );
            if (myResponse.getBoolean("active") == true) {
                out.println(
                        "<h2>\" Validated \"</title>" +
                                "</body>\n" +
                                "</html>"
                );
            }
        } catch (IOException e) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            out.println(
                    "<h2>\" Invalid Access token.  \"</title>" +
                     "<form action=\"home\" method=\"get\" >"+
                        "<input type=\"submit\" value=\"Go back to Home Page\">"+
                     "</body>\n" +
                     "</html>"
            );

            //response.sendRedirect("home");


        }


    }
}
