package leclerc.gridder.activities.chat.gcm;

import android.os.Bundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpConnection;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import leclerc.gridder.R;
import leclerc.gridder.activities.chat.ChatActivity;

public class POST2GCM {
    private static final String API_KEY = "AIzaSyCzLRd4J5PIH6VpI7T5rY2dddx4y7Va4IE";
    public static void post(Content content) {
        //test2(content);
        test3(content.to);
        /*try {
            URL url = new URL("https://android.googleapis.com/gcm/send");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Sender", "id=" + "1031721267946");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

            mapper.writeValue(wr, content);
            System.out.println(mapper.writeValueAsString(content));

            wr.flush();
            wr.close();

            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'POST' request to URL: " + url);
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println(response.toString());
        }
        catch(MalformedURLException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }*/
    }

    private static void test(Content content) {
        try {
            URL url = new URL("https://gcm-http.googleapis.com/gcm/send");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            ObjectMapper mapper = new ObjectMapper();
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());

            mapper.writeValue(outputStream, content);

            int ResponseCode = conn.getResponseCode();
            System.out.println("Response Code: " + ResponseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            System.out.println(response.toString());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void test2(Content content) {
        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            JSONObject jNotification = new JSONObject();

            jData.put("from", content.data.get("from"));
            jData.put("message", content.data.get("message"));

            jNotification.put("title", content.notification.get("title"));
            jNotification.put("body", content.notification.get("body"));

            // Where to send GCM message.

            jGcmData.put("to", content.to);
            jGcmData.put("message_id", String.valueOf(new AtomicInteger().getAndIncrement()));

            // What to send in GCM message.
            jGcmData.put("data", jData);
            jGcmData.put("notification", jNotification);

            // Create connection to send GCM Message request.
            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            System.out.println("JSON DATA: " + jGcmData.toString());

            String resp = "" + conn.getResponseCode();//IOUtils.toString(inputStream);
            System.out.println(resp);

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();


            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
    }

    private static void test3(String senderId) {
        try {
            Bundle data = new Bundle();
            data.putString("my_message", "Hello World");
            data.putString("my_action",
                    "com.google.android.gcm.demo.app.ECHO_NOW");
            String id = Integer.toString(new AtomicInteger().incrementAndGet());
            GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(ChatActivity.staticContext);

            gcm.send(senderId + "@gcm.googleapis.com", id, data);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
