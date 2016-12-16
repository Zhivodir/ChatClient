package ua.kiev.prog;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public static int sendReq(String request){
        int result = -1;
        try {
            URL url = new URL(getURL() + request);
            HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            result = connection.getResponseCode();
        }catch(MalformedURLException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        return result;
    }
}
