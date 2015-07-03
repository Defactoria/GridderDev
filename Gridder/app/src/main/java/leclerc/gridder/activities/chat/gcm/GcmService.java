package leclerc.gridder.activities.chat.gcm;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.concurrent.atomic.AtomicInteger;

import leclerc.gridder.R;
import leclerc.gridder.activities.chat.ChatActivity;

public class GcmService extends GcmListenerService {
    public static final int MESSAGE_NOTIFICATION_ID = new AtomicInteger().incrementAndGet();

    public GcmService() {

    }

    /*@Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        String messageId = extras.getString("message_id");
        String from = extras.getString("from");
        String title = extras.getString("title");
        String body = extras.getString("message");

        createNotification(title, body);
        createMessageInChat(body);

        Log.i("GCM", "Received : (" + messageType + ")  " + title + " | " + body);

        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }*/

    @Override
    public void onMessageReceived(String from, Bundle data) {
        System.out.println("\nOnMessageReceived: FROM " + from);

        String title = data.getString("title");
        String body = data.getString("message");

        createNotification(title, body);
        createMessageInChat(body);
    }

    @Override
    public void onMessageSent(String msgId) {
        System.out.println("\nOnMessageSent: " + msgId);
        super.onMessageSent(msgId);
    }

    @Override
    public void onDeletedMessages() {
        System.out.println("\nOnDeletedMessages");

        super.onDeletedMessages();
    }

    @Override
    public void onSendError(String msgId, String error) {
        System.out.println("\nERROR: " + error);
        super.onSendError(msgId, error);
    }

    private void createNotification(String title, String body) {
        Context context = getBaseContext();
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_template_icon_bg)
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[] { 500, 100, 500 });

        NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }

    private void createMessageInChat(String body) {
        if(ChatActivity.staticContext == null)
            return;

        final String message = body;
        Handler mainHandler = new Handler(ChatActivity.staticContext.getMainLooper());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ChatActivity.createMessageInChat(message);
            }
        };

        mainHandler.post(runnable);
    }
}