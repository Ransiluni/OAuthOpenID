package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;



public class AuthCodeURL extends HttpServlet {


    public void service(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException{

        String response_type = request.getParameter("response_type");
        String scope = request.getParameter("scope");
        String clientCode = request.getParameter("clientCode");
        Constants.client_id=clientCode;
        String authEndPoint = request.getParameter("authEndpoint");
        String redirect_uri = request.getParameter("redirect_uri");




        QueryBuilder codeBuilder=new QueryBuilder();

        codeBuilder.append("response_type",response_type);
        codeBuilder.append("scope",scope);
        codeBuilder.append("client_id",clientCode);
        codeBuilder.append("state",Constants.state);
        codeBuilder.append("redirect_uri",redirect_uri);


        String url=codeBuilder.returnQuery(authEndPoint);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "OAuth Demo";

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";


        response.sendRedirect(url);

    }




}

