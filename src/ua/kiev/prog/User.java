package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by User on 18.12.2016.
 */
public class User {
    private String name;
    private String pswd;
    private UserStatus userStatus;

    public User(String name, String pswd, UserStatus userStatus) {
        this.name = name;
        this.pswd = pswd;
        this.userStatus = userStatus;
    }

    public String getName() {
        return name;
    }

    public String getPswd() {
        return pswd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public static User fromJSON(String s) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(s, User.class);
    }

    @Override
    public String toString() {
        return name + "   " + userStatus;
    }
}
