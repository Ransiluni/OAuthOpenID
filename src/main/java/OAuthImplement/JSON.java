package OAuthImplement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import javax.net.ssl.*;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class JSON extends HttpServlet {
    static String userName;
    static String password;
    static String serverUrl;


    @Override
    public void init(ServletConfig config) throws ServletException {

        // All the code below is to overcome host name verification failure we get in certificate
        // validation due to self-signed certificate.

        try {

            SSLContext sc;

            // Get SSL context
            sc = SSLContext.getInstance("SSL");

            // Create empty HostnameVerifier
            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            };

            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                                               String authType) {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                                               String authType) {
                }
            }};

            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            //SSLSocketFactory sslSocketFactory = sc.getSocketFactory();

            //HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
            SSLContext.setDefault(sc);
            HttpsURLConnection.setDefaultHostnameVerifier(hv);

            // Load init parameters.
            userName = config.getInitParameter("userName");
            password = config.getInitParameter("password");
            serverUrl = config.getInitParameter("serverUrl");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession(false);

        String authCode = request.getParameter("code");
        String redirect_uri = request.getParameter("redirect_uri");
        String tokenEndpoint = request.getParameter("tokenEndpoint");
        String code_verifier = request.getParameter("client_secret");


        QueryBuilder tokenBuilder = new QueryBuilder();
        tokenBuilder.append("grant_type", "authorization_code");
        tokenBuilder.append("code", authCode);
        tokenBuilder.append("redirect_uri", redirect_uri);
        tokenBuilder.append("client_id", (String) session.getAttribute("client_id"));
        tokenBuilder.append("client_secret", code_verifier);


        String url = tokenBuilder.returnQuery(tokenEndpoint);
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("POST");
        //add request header
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=UTF-8");
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer re = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            re.append(inputLine);
        }
        in.close();

        //Read JSON response and print
        JSONObject myResponse = new JSONObject(re.toString());
//        System.out.println(myResponse);
//        if (myResponse != null) {
//            session.setAttribute("grant_type", "token");
//        }


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
                        "<h2>Getting Access Token and ID Token for Authorization Code</h2>\n" +
                        "<form action=\"\" id=\"tokenForm\" method=\"get\">\n" +
                            "<table class=\"user_pass_table\">\n" +
                                "<tr>\n" +
                                    "<td><label>Access Token :</label></td>\n" +
                                    "<td><input id=\"accessToken\" name=\"accessToken\" value=" + myResponse.getString("access_token") + " style=\"width:450px\" readonly/>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>Refresh Token :</label></td>\n" +
                                    "<td><input id=\"accessToken\" name=\"accessToken\" value=" + myResponse.getString("refresh_token") + " style=\"width:450px\" readonly/>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>Scope :</label></td>\n" +
                                    "<td><input id=\"accessToken\" name=\"accessToken\" value=" + myResponse.getString("scope") + " style=\"width:450px\" readonly/>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>ID Token :</label></td>\n" +
                                    "<td><textarea id=\"idToken\" name=\"idToken\"  style=\"width:450px\" rows=\"20\" readonly >" + myResponse.getString("id_token") + " </textarea>\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>Token Type :</label></td>\n" +
                                    "<td><input id=\"tokenType\" name=\"tokenType\" value=" + myResponse.getString("token_type") + " readonly />\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                    "<td><label>Expires In :</label></td>\n" +
                                    "<td><input id=\"expiresIn\" name=\"expiresIn\" value=" + myResponse.getInt("expires_in") + " readonly />\n" +
                                "</tr>\n" +
                                "<tr>\n" +
                                "<tr>" +
                                    "<td colspan=\"2\"><input type=\"submit\" name=\"tokenInfo\" value=\"Token Info\" formaction=\"TokenInfo\"/></td>" +
                                "</tr>" +
                                "<tr>" +
                                    "<td colspan=\"2\"><input type=\"submit\" name=\"tokenInfo\" value=\"User Info\" formaction=\"UserInfo\"/></td>" +
                                "</tr>" +
                                    "<td><input id=\"decodedToken\" name=\"decodedToken\" hidden/></td>\n" +
                                        "<script type=\"text/javascript\">\n" +
                                        "var idToken = \""+myResponse.getString("id_token")+"\";\n" +
                                        "var decodedToken = atob(idToken.split(\".\")[1]);\n" +
                                        "document.getElementById(\"decodedToken\").value = decodedToken;\n" +
                                        "</script>" +
                            "</table>\n" +
                        "</form>" +
                    "</body>\n" +
                "</html>"
        );

    }


}