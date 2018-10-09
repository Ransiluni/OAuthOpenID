package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.simple.JSONObject;

public class TokenVerifier extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        String title = "Verify Details";
        String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";


        String alias = "wso2carbon";

        try {
            String token = request.getParameter("idToken");
            String audience = (String) session.getAttribute("client_id");
            String grantType = (String) session.getAttribute("grant_type");
            String nonce = (String) session.getAttribute("nonce");
            String access = request.getParameter("accessToken");
            String provider = (String)session.getAttribute("provider");
            String jwks;

            if("WSO2".equals(provider)){
                jwks = Constants.WSO2_JWKS_ENDPOINT;
            }
            else{
                jwks = Constants.KEYCLOAK_JWKS_ENDPOINT;
            }


            //at_hash validate
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            md.update(access.getBytes());
            byte[] digest = md.digest();
            //System.out.println("digest"+digest);
            byte[] leftmost = new byte[16];
            for (int i = 0; i < 16; i++)

            {
                leftmost[i] = digest[i];
                //System.out.println("leftmost"+leftmost);
            }
            String at_hash = new String(Base64.encodeBase64URLSafe(leftmost));
//        System.out.println(at_hash);
            String kid;
            String modulus;
            String exponent;
            try{
                String url = jwks;
                URL object = new URL(url);
                HttpURLConnection con = (HttpURLConnection) object.openConnection();

                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;
                StringBuffer re = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    re.append(inputLine);
                }
                in.close();

                org.json.JSONObject myResponse = new org.json.JSONObject(re.toString());
                JSONArray myArray = myResponse.getJSONArray("keys");
                kid = myArray.getJSONObject(0).getString("kid");
                modulus = myArray.getJSONObject(0).getString("n");
                exponent = myArray.getJSONObject(0).getString("e");

                DecodedJWT decJWT = JWT.decode(token);
                if(kid.equals(decJWT.getKeyId())){

                    RSAPublicKey publicKey = null;
//                InputStream file = new FileInputStream("src/main/resources/wso2carbon.jks");

//                KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
//                keystore.load(file, alias.toCharArray());

//                Certificate cert = keystore.getCertificate(alias);
//                Certificate cert = "";
//                publicKey = (RSAPublicKey) cert.getPublicKey();

                    KeyFactory kf = KeyFactory.getInstance("RSA");
                    BigInteger mod = new BigInteger(1,Base64.decodeBase64(modulus));
                    BigInteger exp = new BigInteger(1,Base64.decodeBase64(exponent));

                    publicKey = (RSAPublicKey) kf.generatePublic(new RSAPublicKeySpec(mod,exp));

                    Algorithm alg = Algorithm.RSA256(publicKey, null);

                    if ("authorization_code".equals(grantType)) {
                        JWTVerifier verifier = JWT.require(alg)
                                .withIssuer("https://localhost:9443/oauth2/token")
                                .withSubject("admin")
                                .withAudience(audience)
                                .withClaim("at_hash", at_hash)
                                .build();

                        DecodedJWT jwt = verifier.verify(token);
                    }
                    if ("token".equals(grantType)) {
                        JWTVerifier verifier = JWT.require(alg)
                                .withIssuer("https://localhost:9443/oauth2/token")
                                .withSubject("admin")
                                .withAudience(audience)
                                .withClaim("nonce", nonce)
                                .withClaim("at_hash", at_hash)
                                .build();

                        DecodedJWT jwt = verifier.verify(token);
                    }
                    out.println(
                            "<h2>\" Token Verified.  \"</title>" +
                                    "</body>\n" +
                                    "</html>"
                    );
                }
                else{
                    throw new IOException();
                }




//            Use this if nimbusds is used.
//            String JWTString = token;
//            SignedJWT signedJWT = SignedJWT.parse(JWTString);
//
//            JWSVerifier verifier = new RSASSAVerifier(publicKey);
//
//            if(signedJWT.verify(verifier)){
//                System.out.println("VALID");
//            }
//            else{
//                System.out.println("INVALID");
//            }
            } catch (Exception e) {
                System.out.println("Error: " + e);
                if (session != null) {
                    session.invalidate();
                }
                out.println(
                        "<h2>\" Invalid ID token.  \"</title>" +
                                "<form action=\"home\" method=\"get\" >" +
                                "<input type=\"submit\" value=\"Go back to Home Page\">" +
                                "</body>\n" +
                                "</html>"
                );
            }

        } catch (Exception e) {
            response.sendRedirect("home?errorMessage=ID Token Error");

        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

    }
}
