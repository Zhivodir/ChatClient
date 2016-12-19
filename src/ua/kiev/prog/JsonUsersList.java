package ua.kiev.prog;

import java.util.Collections;
import java.util.List;

/**
 * Created by User on 19.12.2016.
 */
public class JsonUsersList {
    private final List<User> usersList;

    public JsonUsersList(List<User> list) {
        this.usersList = list;
    }

    public List<User> getList() {
        return  Collections.unmodifiableList(usersList);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(System.lineSeparator() + "List of users:");
        for(User user:usersList){
            sb.append(user);
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }
}
