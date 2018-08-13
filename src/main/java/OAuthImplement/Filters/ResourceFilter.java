package OAuthImplement.Filters;

import OAuthImplement.QueryBuilder;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class ResourceFilter implements Filter {


    FilterConfig fConfig = null;
    String client_id;
    String refreshToken;
    String client_secret;

    public void init(FilterConfig config) throws ServletException {
        fConfig = config;
    }

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)resp;

        String authString = (String)request.getAttribute("Authorization");
        String[] params = authString.split(" ");

        HttpSession session = request.getSession(false);
//        String access_Token;
//        //access_Token = (String)session.getAttribute("access_token");
//        access_Token = request.getParameter("accessToken");
//        session.setAttribute("refresh_token",request.getParameter("refreshToken"));
//
//        if("token".equals(session.getAttribute("grant_type"))){
//            access_Token = request.getParameter("accessToken");
//        }

        if("Bearer".equals(params[0])){
            String access_Token = params[1];
            //build url
            QueryBuilder codeBuilder = new QueryBuilder();
            codeBuilder.append("token", access_Token);

            String EndPoint = " https://localhost:9443/oauth2/introspect";
            String url = codeBuilder.returnQuery(EndPoint);


            URL object = new URL(url);
            HttpURLConnection con = (HttpURLConnection) object.openConnection();

            con.setRequestMethod("POST");

            //add request header
            con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Authorization", "Bearer " + access_Token);

            try{
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer re = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    re.append(inputLine);
                }
                in.close();

                //Read JSON response
                org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());

                String active = String.valueOf(myResponse.getBoolean("active"));
                String scope = (String)myResponse.get("scope");
                session.setAttribute("scope",scope );
                String[] scopes = scope.split(" ");
                String client = (String)myResponse.get("client_id");

                String client_id = (String)session.getAttribute("client_id");

                if("true".equals(active) && Arrays.asList(scopes).contains("read")){
                    chain.doFilter(request,response);
                }
                else{
                    response.setContentType("application/json");
                    response.setStatus(401);
                }

            }
            catch (IOException e){
//                if (session != null) {
//                    session.invalidate();
//                }
//
//                if(!((session.getAttribute("refresh_token"))==null)) {
//                    session.setAttribute("grant_type", "refresh_token");
//                    response.sendRedirect("JSON");
//                }else{
//                    response.sendRedirect("home?errorMessage=Access Token Invalid");
//                }

            }
        }
        else{
            String credString = params[1];
            String[] creds = credString.split(":");


            String username = fConfig.getInitParameter("username");
            String password = fConfig.getInitParameter("password");

            if(username.equals(creds[0]) && password.equals(creds[1])){
                chain.doFilter(request, response);
            }
            else{
                response.sendRedirect("login.jsp?error=invalid");
            }
        }

    }
//Arrays.asList(scope.split(" ")).contains("openid")
    
    public void destroy() {}


}