package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class ProtectedResource extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out=response.getWriter();
        String title = "Introspection Result";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

//        String[] scopes = request.getParameter("scope").split(" ");


        out.println(
                    "<html>\n" +
                            "<head>" +
                                "<title>" + title + "</title>" +
                            "</head>\n" +
                            "<body>\n"+
                                "<h2> HELLO WORLD! OAUTH DONE!! </title>\n"
            );

//        if(Arrays.asList(scopes).contains("write")){
//            out.println(
//                    "<input name=\"write\" />\n" +
//                    "<input type=\"button\" value=\"Submit\">"
//
//            );
//        }

        out.println(
                "</body>\n" +
                        "</html>"
        );

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        doGet(request,response);
    }
}
