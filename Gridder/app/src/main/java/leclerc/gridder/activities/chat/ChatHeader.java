package leclerc.gridder.activities.chat;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.R;
import leclerc.gridder.data.Interest;

/**
 * Created by Antoine on 2015-06-02.
 */
public class ChatHeader extends ChatMessage {
    private LinearLayout mListView;
    private List<Interest> mInterests;

    public ChatHeader(Context context) {
        super(context);

        init();
    }

    public ChatHeader(Context context, View view) {
        super(context);

        currentView = view;
        init();
    }

    @Override
    protected void init() {
        LayoutInflater inflater = LayoutInflater.from(currentContext);

        if(currentView == null)
            currentView = inflater.inflate(R.layout.fragment_chat_header, null, false);

        mListView = (LinearLayout)currentView.findViewById(R.id.chat_header_commonSquares);

        mInterests = new ArrayList<>();
    }

    @Override
    public View getView() {
        return currentView;
    }

    public void setCommonInterests(Interest... interests) {
        mListView.removeAllViews();
        mInterests.clear();

        mListView.addView(new View(currentContext), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, currentContext.getResources().getDisplayMetrics()), 10);

        for(Interest i : interests) {
            if(!mInterests.contains(i)) {
                if(i.getCard() != null && !i.isAddInterest()) {
                    mInterests.add(i);

                    RelativeLayout root = new RelativeLayout(currentContext);
                    int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, currentContext.getResources().getDisplayMetrics());
                    root.setLayoutParams(new ViewGroup.LayoutParams(px, px));


                    RelativeLayout layout = (RelativeLayout)LayoutInflater.from(currentContext).inflate(R.layout.fragment_card_front, null, false);

                    ImageView bkgImage = (ImageView)layout.findViewById(R.id.card_front_image);
                    TextView txtHashtag = (TextView)layout.findViewById(R.id.card_front_hashtag);
                    Button btnCard = (Button)layout.findViewById(R.id.card_front_button);

                    btnCard.setVisibility(View.GONE);
                    btnCard.setEnabled(false);

                    layout.setBackgroundColor(i.getColor());
                    bkgImage.setImageBitmap(i.getCustomImage());
                    txtHashtag.setText(i.getHashtag());

                    txtHashtag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                    txtHashtag.setShadowLayer(3, 2, 2, Color.BLACK);

                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(px - 20, px - 20);
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

                    layout.setLayoutParams(layoutParams);

                    root.addView(layout);

                    FrameLayout frameLayout = new FrameLayout(currentContext);
                    int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, currentContext.getResources().getDisplayMetrics());

                    RelativeLayout.LayoutParams frameParams = new RelativeLayout.LayoutParams(width, px + 20);
                    frameParams.addRule(RelativeLayout.ALIGN_PARENT_END);

                    frameLayout.setLayoutParams(frameParams);

                    root.addView(frameLayout);

                    mListView.addView(root);
                }
            }
        }

        if(mInterests.size() == 0) {
            TextView txtNone = new TextView(currentContext);
            txtNone.setText("None");
            txtNone.setTextSize(20);
            txtNone.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            txtNone.setGravity(Gravity.CENTER);

            Resources r = currentContext.getResources();
            int padding = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());
            txtNone.setPadding(padding, padding, padding, padding);

            mListView.addView(txtNone);
        }
    }
}
