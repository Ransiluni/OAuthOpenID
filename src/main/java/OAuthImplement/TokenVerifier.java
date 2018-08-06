package OAuthImplement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;

public class TokenVerifier extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        String alias = "wso2carbon";
        String token = request.getParameter("idToken");

        try {
            RSAPublicKey publicKey = null;
            InputStream file = new FileInputStream("src/main/resources/wso2carbon.jks");

            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(file, alias.toCharArray());

            Certificate cert = keystore.getCertificate(alias);
            publicKey = (RSAPublicKey) cert.getPublicKey();

            String JWTString = token;
            SignedJWT signedJWT = SignedJWT.parse(JWTString);

            JWSVerifier verifier = new RSASSAVerifier(publicKey);

            if(signedJWT.verify(verifier)){
                System.out.println("VALID");
            }
            else{
                System.out.println("INVALID");
            }
        }catch (Exception e){
            System.out.println("Error: " + e);
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

    }
}
