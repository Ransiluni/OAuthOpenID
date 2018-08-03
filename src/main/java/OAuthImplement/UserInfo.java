package OAuthImplement;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserInfo extends HttpServlet {
    public Object obj;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accessToken = request.getParameter("accessToken");

        QueryBuilder tokenBuilder = new QueryBuilder();
        tokenBuilder.append("Bearer", accessToken);


        String url = "https://localhost:9443/oauth2/userinfo";

        //BufferedReader reader = new BufferedReader(new InputStreamReader(((HttpURLConnection) (new URL(urlString)).openConnection()).getInputStream(), Charset.forName("UTF-8")));


        URL object  = new URL(url);
        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        // optional default is GET
        con.setRequestMethod("POST");
        //add request header
        con.setRequestProperty( "Authorization","Bearer "+accessToken);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer re = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            re.append(inputLine);
        }
        in.close();

        //Read JSON response and print
        org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "OAuth Demo";

        String docType =
                "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";

        out.println(docType +
                "<html>\n" +
                "<head>" +
                "<title>" + title + "</title>" +
                "</head>\n" +
                "<body>\n" +
                "<h2>User Information</h2>\n"
        );

        for(Object key : myResponse.keySet()){
            String keyStr = (String)key;
            String value = myResponse.getString(keyStr);

            out.println(
                    "<label>" + keyStr + ": " + value + "</label>\n" +
                    "</body>\n" +
                    "</html>"
            );
        }



    }

    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}

