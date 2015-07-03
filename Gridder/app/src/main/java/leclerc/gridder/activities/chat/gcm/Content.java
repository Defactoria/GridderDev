package leclerc.gridder.activities.chat.gcm;

import android.util.Pair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Content implements Serializable {
    public Map<String, String> data;

    public String from;
    public String to;
    public Map<String, String> notification;

    public void setDestination(String id) {
        to = id;
    }

    public void setSender(String id) {
        from = id;
    }

    public void setNotification(String title, String body) {
        if(notification == null)
            notification = new HashMap<>();
        notification.put("title", title);
        notification.put("body", body);
    }

    public void createData(String from, String message) {
        if(data == null)
            data = new HashMap<>();

        data.put("from", from);
        data.put("message", message);
    }
}
