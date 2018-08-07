package OAuthImplement.Filters;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;


public class ResourceFilter implements Filter {



    public void init(FilterConfig arg0) throws ServletException {}

    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        String active = req.getParameter("active");
        String scope = req.getParameter("scope");

        //PrintWriter out=resp.getWriter();
        System.out.println(active);
        System.out.println(scope);

        if (("true".equals(active)) && "openid".equals(scope) ) {
            chain.doFilter(req, resp);
        }
        else{
            RequestDispatcher rd=req.getRequestDispatcher("home");
            rd.include(req, resp);
        }




    }





    public void destroy() {}


}