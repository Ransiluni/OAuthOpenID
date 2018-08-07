package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.apache.commons.codec.binary.Base64;

public class TokenVerifier extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        HttpSession session = request.getSession(false);

        String alias = "wso2carbon";
        String token = request.getParameter("idToken");
        String audience = (String)session.getAttribute("client_id");
        String grantType = (String)session.getAttribute("grant_type");
        String nonce = (String)session.getAttribute("nonce");
        String access = request.getParameter("accessToken");

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
        for ( int i=0; i<16;i++)

        { leftmost[i]=digest[i];
            //System.out.println("leftmost"+leftmost);
        }
        String at_hash = new String(Base64.encodeBase64URLSafe(leftmost));
//        System.out.println(at_hash);
        try {
            RSAPublicKey publicKey = null;
            InputStream file = new FileInputStream("src/main/resources/wso2carbon.jks");

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(file, alias.toCharArray());

            Certificate cert = keystore.getCertificate(alias);
            publicKey = (RSAPublicKey) cert.getPublicKey();

            Algorithm alg = Algorithm.RSA256(publicKey, null);

            if("authorization_code".equals(grantType)){
                JWTVerifier verifier = JWT.require(alg)
                        .withIssuer("https://localhost:9443/oauth2/token")
                        .withSubject("admin")
                        .withAudience(audience)
                        .withClaim("at_hash",at_hash)
                        .build();

                DecodedJWT jwt = verifier.verify(token);
            }
            if("token".equals(grantType)){
                JWTVerifier verifier = JWT.require(alg)
                        .withIssuer("https://localhost:9443/oauth2/token")
                        .withSubject("admin")
                        .withAudience(audience)
                        .withClaim("nonce",nonce)
                        .withClaim("at_hash",at_hash)
                        .build();

                DecodedJWT jwt = verifier.verify(token);
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
        }catch (Exception e){
            System.out.println("Error: " + e);
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

    }
}
