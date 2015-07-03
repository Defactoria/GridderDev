package leclerc.gridder.cards;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import leclerc.gridder.data.Interest;
import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-05-11.
 */
public class CardBack extends RelativeLayout implements UpdateCard {
    private Interest mInterestData;
    private Button btnEdit;
    private TextView textView;

    private ImageView imgBackground;

    public CardBack(Context context) {
        super(context);

        setAlpha(0.0f);
    }

    public Interest getInterest() {
        return mInterestData;
    }

    public void init(Interest interest) {
        mInterestData = interest;
        update();
    }

    public void initButton(OnClickListener onClick) {
        btnEdit.setClickable(onClick != null);
        btnEdit.setOnClickListener(onClick);
    }

    public AnimatorSet getInAnimation() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_left_in);
        set.setTarget(this);

        return set;
    }

    public AnimatorSet getOutAnimation() {
        AnimatorSet set = (AnimatorSet)AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_out);
        set.setTarget(this);

        return set;
    }

    public void createView(LayoutInflater inflater) {
        removeAllViews();
        View v = inflater.inflate(R.layout.fragment_card_back, this, true);

        //setBackgroundColor(mInterestData.getBackgroundColor());

        textView = (TextView)v.findViewById(R.id.card_back_hashtag);
        textView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/calibri.ttf"));
        textView.setShadowLayer(5, 3, 3, Color.BLACK);

        if(btnEdit == null) {
            btnEdit = (Button)v.findViewById(R.id.card_back_edit);
        }

        imgBackground = (ImageView)v.findViewById(R.id.card_back_flipped_image);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void update() {
        if(getInterest() != null) {
            getInterest().addChangedListener(new Interest.ImageChangedListener() {
                @Override
                public void onChanged(Interest.InterestState state, Bitmap old, Bitmap newBmp) {
                    imgBackground.setImageBitmap(getInterest().getCustomImage());
                }
            });

            textView.setText(getInterest().getHashtag());
            imgBackground.setImageBitmap(getInterest().getCustomImage());
        }
    }
}