package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class TokenInfo extends HttpServlet {
    public Object obj;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String title = "Token Details";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        String id_token = request.getParameter("decodedToken");


        JSONParser parser = new JSONParser();

        try {
             obj = parser.parse(id_token);

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
