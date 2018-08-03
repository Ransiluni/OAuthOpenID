package OAuthImplement;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

public class UserInfo extends HttpServlet {
    public Object obj;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessToken = request.getParameter("accessToken");

        String url = "https://localhost:9443/oauth2/userinfo";

        URL object  = new URL(url);
        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        // optional default is GET
        con.setRequestMethod("POST");
        //add request header
        con.setRequestProperty( "Authorization","Bearer "+accessToken);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer re = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            re.append(inputLine);
        }
        in.close();

        //Read JSON response and print
        org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "User Details";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        String access_token = request.getParameter("accessToken");


        JSONParser parser = new JSONParser();

        try {
            obj = parser.parse(access_token);

        } catch (ParseException e) {}

        JSONObject jsonObj = (JSONObject)obj;
        Iterator<?> iter = jsonObj.entrySet().iterator();

        out.println(docType +
                "<html>\n" +
                "<head>" +
                "<title>" + title + "</title>\n" +
                "<style>\n" +
                "tr:nth-child(even) {\n" +
                "background-color: #dddddd;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h3>Token Details</h3>" +
                "<table style=\"width:800px;margin-left: auto;margin-right: auto;\" class=\"striped\">"
        );

        while (iter.hasNext()){
            Map.Entry entry = (Map.Entry) iter.next();

            out.println(
                    "<tr>\n" +
                            "<td style=\"width:50%\">"+ entry.getKey() +"</td>\n" +
                            "<td>"+ entry.getValue() +"</td>\n" +
                            "</tr>"
            );
        }

        out.println(
                "</table>\n" +
                        "</body>\n" +
                        "</html>"
        );
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
