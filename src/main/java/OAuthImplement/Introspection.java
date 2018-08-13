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

        access_Token = (String)session.getAttribute("access_token");
        user_name=request.getParameter("username");
        password=request.getParameter("password");
        System.out.println(user_name);

        if(user_name==null){
            request.setAttribute("Authorization", "Bearer " + access_Token);
            request.getRequestDispatcher("ProtectedResource").include(request, response);
        }else{
            String encoding = Base64.getEncoder().encodeToString(
                    (user_name+":"+password).getBytes("utf-8"));
            request.setAttribute("Authorization", "Basic " + encoding);
            request.getRequestDispatcher("ProtectedResource").include(request, response);
        }


//        String url = "http://localhost:8080/ProtectedResource";
//        URL obj = new URL(url);
//
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//        // optional default is GET
//        con.setRequestMethod("POST");
//        //add request header
//        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
//        con.setRequestProperty("Authorization", "Bearer " + access_Token);
//
//
//
////
//
//
////        int responseCode = con.getResponseCode();
////        System.out.println("\nSending 'GET' request to URL : " + url);
////        System.out.println("Response Code : " + responseCode);
//        try
//
//        {
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            String inputLine;
//            StringBuffer re = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                re.append(inputLine);
//            }
//            in.close();
//
//            //Read JSON response and print
//            //JSONObject myResponse = new JSONObject(re.toString());
//            System.out.println(re.toString());
////        System.out.println(myResponse);
////        if (myResponse != null) {
////            session.setAttribute("grant_type", "token");

        //response.setContentType("text/html;charset=UTF-8");
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
