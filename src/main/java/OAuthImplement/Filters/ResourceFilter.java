package OAuthImplement.Filters;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;


public class ResourceFilter implements Filter {



    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse)resp;

        String active = request.getParameter("active");
        String scope = request.getParameter("scope");

        String[] scopes = scope.split(" ");

        //PrintWriter out=resp.getWriter();
        System.out.println(active);
        System.out.println(scope);

        if(active != null){
            if (("true".equals(active)) && Arrays.asList(scopes).contains("read")) {
                chain.doFilter(request, response);
            }
            else{
                RequestDispatcher rd = req.getRequestDispatcher("home");
                rd.include(request, response);
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