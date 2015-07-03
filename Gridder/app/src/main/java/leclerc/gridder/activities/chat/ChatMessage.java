package leclerc.gridder.activities.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.zip.Inflater;

import leclerc.gridder.R;
import leclerc.zapper.User;

/**
 * Created by Antoine on 2015-04-18.
 */
public class ChatMessage {
    private User mUser;
    private String mMessage;
    private Bitmap mImage;
    private int mId;

    protected Context currentContext;

    // View parts
    protected View currentView;
    private TextView txtUsername;
    private TextView txtMessage;
    private ImageView imgJoined;
    private ImageView imgAvatar;

    public ChatMessage(Context context) {
        currentContext = context;
    }

    public ChatMessage(Context context, int id, User user, String message) {
        this(context, id, user, message, null);
    }

    public ChatMessage(Context context, int id, User user, String message, Bitmap image) {
        this(context, id, user, image);
        setMessage(message);
    }

    public ChatMessage(Context context, int id, User user, Bitmap image) {
        currentContext = context;
        mId = id;
        setUser(user);
        setImage(image);

        init();
    }

    public final int getId() {
        return mId;
    }

    private void setUser(User user) {
        mUser = user;
    }

    public final User getUser() {
        return mUser;
    }

    private void setMessage(String message) {
        mMessage = message;
    }

    public final String getMessage() {
        return mMessage;
    }

    private void setImage(Bitmap image) {
        mImage = image;
    }

    public final Bitmap getImage() {
        return mImage;
    }

    protected void init() {
        LayoutInflater inflater = LayoutInflater.from(currentContext);

        if (getUser() == null) {
            currentView = inflater.inflate(R.layout.fragment_chat_message_user, null, false);
        } else {
            currentView = inflater.inflate(R.layout.fragment_chat_message_other, null, false);
            txtUsername = (TextView) currentView.findViewById(R.id.chat_other_username);
            imgAvatar = (ImageView) currentView.findViewById(R.id.chat_other_avatar);
        }

        txtMessage = (TextView) currentView.findViewById(R.id.chat_message);
        imgJoined = (ImageView) currentView.findViewById(R.id.chat_joined_image);
    }

    public View getView() {
        if(txtMessage != null)
            txtMessage.setText(getMessage());

        if(imgJoined != null)
            imgJoined.setVisibility(View.GONE);

        if(getUser() != null && txtUsername != null) {
            txtUsername.setText(getUser().getUsername());
        }

        return currentView;
    }
}
