package OAuthImplement;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class ProtectedResource extends HttpServlet {
    String scope;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject result = new JSONObject();
        result.put("message","Hello World!");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        out.print(result);
        out.flush();
//        String title = "Introspection Result";
//        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

//        HttpSession session = request.getSession(false);
//        scope = (String)session.getAttribute("scope");
//        System.out.println(scope);

//        try {
//            String[] scopes = scope.split(" ");
//
//
//            out.println(
//                    "<html>\n" +
//                            "<head>" +
//                            "<title>" + title + "</title>" +
//                            "</head>\n" +
//                            "<body>\n" +
//                            "<h2> HELLO WORLD! OAUTH DONE!! </title>\n"
//            );
//
//        if(Arrays.asList(scopes).contains("write")){
//            out.println(
//                    "<input name=\"write\" />\n" +
//                    "<input type=\"button\" value=\"Submit\">"
//
//            );
//        }
//
//            out.println(
//                    "</body>\n" +
//                            "</html>"
//            );
//        }catch(NullPointerException e){
//            out.println(
//                    "<html>\n" +
//                            "<head>" +
//                            "<title>" + title + "</title>" +
//                            "</head>\n" +
//                            "<body>\n" +
//                            "<h2> HELLO WORLD! Using Basic Authentication!! </title>\n"
//            );
//        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        doGet(request,response);
    }
}
