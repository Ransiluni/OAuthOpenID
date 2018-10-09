package OAuthImplement;

import org.json.JSONException;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

public class RequestProcessor extends HttpServlet {

    String access_Token,user_name,password, provider;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        provider = (String)session.getAttribute("provider");


        user_name=request.getParameter("username");
        password=request.getParameter("password");
        System.out.println(user_name);
        String url = " http://localhost:8081/ProtectedResource/ProtectedResource";

        if(user_name==null){
            access_Token = (String)session.getAttribute("access_token");

            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();

            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Bearer " + access_Token);
            con.setRequestProperty("Provider", provider);

            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer re = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    re.append(inputLine);
                }
                in.close();

                //Read JSON response
                JSONObject result = new JSONObject();
                org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());
                System.out.println(myResponse);
                Set<String> keys = myResponse.keySet();
                for(String key : keys){
                    System.out.println(key);
                    result.put(key,myResponse.getString(key));
                }

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                out.print(result);
                out.flush();

            }
            catch (ConnectException e){
                response.setContentType("application/json");
                response.setStatus(404);
            }
            catch (IOException e){
                response.setContentType("application/json");
                response.setStatus(401);
            }
            catch (JSONException e){
                System.out.println(e);
            }
        }
        else{
            String encoding = Base64.getEncoder().encodeToString(
                    (user_name+":"+password).getBytes("utf-8"));

            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();

            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Basic " + encoding);

            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer re = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    re.append(inputLine);
                }
                in.close();

                //Read JSON response
                JSONObject result = new JSONObject();
                org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());
                Set<String> keys = myResponse.keySet();
                for(String key : keys){
                    System.out.println(key);
                    result.put(key,myResponse.getString(key));
                }

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();

                out.print(result);
                out.flush();

            }
            catch (IOException e){
                response.setContentType("application/json");
                response.setStatus(401);
                System.out.println(e);
            }
        }

    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
