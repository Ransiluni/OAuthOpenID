package OAuthImplement.Filters;

import OAuthImplement.QueryBuilder;
import org.json.JSONObject;
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

    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)resp;

        HttpSession session = request.getSession(false);
        String access_Token;
        access_Token = (String)session.getAttribute("access_token");

        if("token".equals(session.getAttribute("grant_type"))){
            access_Token = request.getParameter("accessToken");
            System.out.println("hjhgjgj"+access_Token);
        }

        if(access_Token != null && !access_Token.isEmpty()){
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
                    if(client.equals(client_id)){
                        chain.doFilter(request,response);
                    }
                    else{
                        response.sendRedirect("home?error=InvalidClientError");
                    }
                }
                else{
                    response.sendRedirect("home?error=InsufficientScopeError");
                }

            }
            catch (IOException e){
                if (session != null) {
                    session.invalidate();
                }
                response.sendRedirect("home");
            }
        }
        else{
            if(request.getParameter("username") == null){
                response.sendRedirect("login.jsp");
            }
            else{
                if("admin".equals(request.getParameter("username")) && "admin".equals(request.getParameter("password"))){
                    chain.doFilter(request, response);
                }
                else{
                    response.sendRedirect("login.jsp?error=invalid");
                }
            }
        }

    }
//Arrays.asList(scope.split(" ")).contains("openid")
    
    public void destroy() {}


}