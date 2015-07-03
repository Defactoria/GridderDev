package leclerc.zapper;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ViewAnimator;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.R;

public class ConversationsActivity extends Activity {

    ArrayAdapter<MessageItem> messagesAdapter;
    List<MessageItem> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        setContentView(R.layout.activity_conversations);

        messages = new ArrayList<MessageItem>();
        messages.add(new MessageItem(this));
        messages.add(new MessageItem(this));
        messages.add(new MessageItem(this));
        messages.add(new MessageItem(this));
        messages.add(new MessageItem(this));
        messages.add(new MessageItem(this));

        messages.get(0).update(0, "User1", "Hey !", true);
        messages.get(1).update(1, "User2", "Dude ! WTF ! #stupid");
        messages.get(2).update(2, "User3", "Too many hashtags #hashtag1 #hashtag2 #wtf #too #many #hashtags #doesitend #idontknow");
        messages.get(3).update(3, "User4", "#TWD", true);
        messages.get(4).update(4, "User5", "BLAL BLA BLA BLA BLA BALB LAB LA BLAB ALB AL", true);
        messages.get(5).update(5, "User6", "JFOIJOIJEWF OIEF OIWEF JWOEIFJ OIWJEFJWEPF");

        LinearLayout list = ((LinearLayout) findViewById(R.id.listMessages));
        list.removeAllViews();

        for(int i = 0; i < messages.size(); i++) {
            list.addView(messages.get(i));
        }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_conversations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}
