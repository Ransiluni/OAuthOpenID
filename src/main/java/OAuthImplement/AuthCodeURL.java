package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;



public class AuthCodeURL extends HttpServlet {


    public void doGet(HttpServletRequest request,HttpServletResponse response)
            throws IOException, ServletException{

        QueryBuilder codeBuilder=new QueryBuilder();

        String grant_type = request.getParameter("grant_type");

        String scope = request.getParameter("scope");
        String clientCode = request.getParameter("clientCode");
        Constants.client_id=clientCode;

        String authEndPoint = request.getParameter("authEndpoint");
        String redirect_uri = request.getParameter("redirect_uri");

        HttpSession session = request.getSession();
        session.setAttribute("redirect_uri", redirect_uri);
        session.setAttribute("client_id", clientCode);
        if("code".equals(grant_type)){
            codeBuilder.append("response_type", grant_type);
            session.setAttribute("grant_type", "authorization_code");
        }
        if("token".equals(grant_type)){
            String response_type = request.getParameter("response_type");
            codeBuilder.append("response_type", response_type);
            codeBuilder.append("nonce", "n-0S6_WzA2Mj");
            session.setAttribute("grant_type", "token");
        }



        codeBuilder.append("scope",scope);
        codeBuilder.append("client_id",clientCode);
        codeBuilder.append("state",Constants.state);
        codeBuilder.append("redirect_uri",redirect_uri);


        String url= codeBuilder.returnQuery(authEndPoint);

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "OAuth Demo";

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";


        response.sendRedirect(url);

    }




}

