package ua.kiev.prog;

import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    private Scanner scanner;
    private String login;
    private String pswd;

    public Utils() {
        scanner = new Scanner(System.in);
        try {
            authorization();
            System.out.println("Enter your message: ");
            while (true) {
                String text = scanner.nextLine();
                if (text.isEmpty()) break;
                Message m;
                int res = 200;
                if("get_users".equals(text)){
                    res = sendReqforUsers();
                }else if(text.contains("get_status")){
                    System.out.println("Enter need user: ");
                    String choiceLogin = scanner.nextLine();
                    res = getStatus(choiceLogin);
                }else if(text.contains("private")){
                    System.out.println("Enter need user: ");
                    String choiceLogin = scanner.nextLine();
                    System.out.println("Enter your message for(" + login +"): ");
                    text = scanner.nextLine();
                    m = new Message(login, text);
                    m.setTo(choiceLogin.trim());
                    res = m.send(getURL() + "/add");
                }else{
                    m = new Message(login, text);
                    res = m.send(getURL() + "/add");
                }

                if (res != 200) {
                    System.out.println("HTTP error occured: " + res);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    public void authorization(){
        do{
            System.out.println("Enter your login: ");
            login = scanner.nextLine();

            System.out.println("Enter your password: ");
            pswd = scanner.nextLine();
        }while(sendReq("/authorization?login=" + login + "&password=" + pswd) != 200);

        Thread thread = new Thread(new GetThread(login));
        thread.setDaemon(true);
        thread.start();
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

    public static int getStatus(String login){
        int result = 200;
        User user;
        HttpURLConnection connection;
        try {
            URL url = new URL(getURL() + "/getUserStatus?login=" + login);
            connection =  (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);

            InputStream is = connection.getInputStream();
            try {
                byte[] buf = requestBodyToArray(is);
                String strBuf = new String(buf, StandardCharsets.UTF_8);
                user = (User) new GsonBuilder().create().fromJson(strBuf, User.class);
                System.out.println(user);
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
