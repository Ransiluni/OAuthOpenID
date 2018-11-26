package OAuthImplement;

import com.google.common.cache.*;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class Cache {

    private static LoadingCache<String, JSONArray> keyCache;

    static {
        RemovalListener<String, JSONArray> removalListener = new RemovalListener<String, JSONArray>() {
            public void onRemoval(RemovalNotification<String,JSONArray> removal) {
                System.out.println("Certificate refreshed!");
            }
        };

        keyCache = CacheBuilder.newBuilder()
                .refreshAfterWrite(1, TimeUnit.MINUTES)
                .removalListener(removalListener)
                .build(
                        new CacheLoader<String, JSONArray>() {
                            @Override
                            public JSONArray load(String url) throws Exception {
                                return getCertificate(url);
                            }
                        }
                );
    }


    public static JSONArray getCertificate(String url){
        System.out.println("Getting the certificate from jwks");
        try{
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

            return myArray;
        }catch(Exception e){
            System.out.println(e);
        }

        return null;
    }

    public static LoadingCache<String,JSONArray> getLoadingCache(){
        return keyCache;
    }
}
