package leclerc.gridder.activities.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-04-18.
 */
public class ChatMessagesAdapter extends ArrayAdapter<ChatMessage> {

    public ChatMessagesAdapter(Context context, ArrayList<ChatMessage> items) {
        super(context, 0, items);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = getItem(position);
        return message.getView();
    }
}
