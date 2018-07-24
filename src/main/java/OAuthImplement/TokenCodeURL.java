package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class TokenCodeURL extends HttpServlet {


    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        String authCode = request.getParameter("authCode");
        String redirect_uri = request.getParameter("redirect_uri");
        String tokenEndpoint = request.getParameter("tokenEndpoint");
        String code_verifier = request.getParameter("client_secret");


        QueryBuilder tokenBuilder=new QueryBuilder();
        tokenBuilder.append("authorization_code","authorization_code");
        tokenBuilder.append("code",authCode);
        tokenBuilder.append("redirect_uri",redirect_uri);
        tokenBuilder.append("client_id",Constants.client_id);
        tokenBuilder.append("client_secret",code_verifier);



        String url=tokenBuilder.returnQuery(tokenEndpoint);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "OAuth Demo";

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
                "<html>\n" +
                "<head><title>" + title + "</title></head>\n" +"<body bgcolor = \"#f0f0f0\">\n" +
                "<h2>"+url+"</h2>\n"+
                "</body>"+
                "</html>"
        );
        //response.sendRedirect(url);

    }




}