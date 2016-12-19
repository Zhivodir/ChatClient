package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public Utils() {
    }

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public int sendReq(String request){
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

    public int sendReqforUsers(){
        int result = 200;
        JsonUsersList usersList;
        HttpURLConnection connection;
        try {
            URL url = new URL(getURL() + "/getUsersList");
            connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            try {
                byte[] buf = requestBodyToArray(is);
                String strBuf = new String(buf, StandardCharsets.UTF_8);
                usersList = (JsonUsersList) new GsonBuilder().create().fromJson(strBuf, JsonUsersList.class);
                System.out.println(usersList);
            } finally {
                is.close();
            }

            result = connection.getResponseCode();
        }catch(MalformedURLException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
        return result;
    }

    public static byte[] requestBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }
}
