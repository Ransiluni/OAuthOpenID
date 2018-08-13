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
import java.util.Base64;

public class Introspection extends HttpServlet {

    String access_Token,user_name,password;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);


        user_name=request.getParameter("username");
        password=request.getParameter("password");
        System.out.println(user_name);

        if(user_name==null){
            access_Token = (String)session.getAttribute("access_token");
            request.setAttribute("Authorization", "Bearer " + access_Token);
            request.setAttribute("Client_id", session.getAttribute("client_id"));
            request.getRequestDispatcher("ProtectedResource").forward(request, response);
        }else{
            String encoding = Base64.getEncoder().encodeToString(
                    (user_name+":"+password).getBytes("utf-8"));
            request.setAttribute("Authorization", "Basic " + encoding);
            request.getRequestDispatcher("ProtectedResource").forward(request,response);
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();


    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
