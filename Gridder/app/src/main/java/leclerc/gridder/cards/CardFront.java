package leclerc.gridder.cards;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

import leclerc.gridder.data.Interest;
import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-05-10.
 */
public class CardFront extends RelativeLayout implements UpdateCard {
    private Interest mInterestData;

    private final int TEXT_COLOR_ADD;
    private final int TEXT_COLOR_DEFAULT = Color.WHITE;

    private View view;
    private ImageView imageView;
    private TextView txtName;
    private Button btnToBack;

    public CardFront(Context context) {
        super(context);

        TEXT_COLOR_ADD = context.getResources().getColor(R.color.text_color);
    }

    public Interest getInterest() {
        return mInterestData;
    }

    public AnimatorSet getInAnimation() {
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_right_in);
        set.setTarget(this);

        return set;
    }

    public AnimatorSet getOutAnimation() {
        AnimatorSet set = (AnimatorSet)AnimatorInflater.loadAnimator(getContext(), R.animator.card_flip_left_out);
        set.setTarget(this);

        return set;
    }

    public void init(Interest interest) {
        mInterestData = interest;

        update();
    }

    public void initButton(OnClickListener onClick, OnLongClickListener onLongClick) {
        btnToBack.setClickable(onClick != null);
        btnToBack.setLongClickable(onLongClick != null);

        btnToBack.setOnClickListener(onClick);
        btnToBack.setOnLongClickListener(onLongClick);
    }

    public void createView(LayoutInflater inflater) {
        removeAllViews();

        if(view == null)
            view = inflater.inflate(R.layout.fragment_card_front, this, true);

        if(imageView == null)
            imageView = (ImageView)view.findViewById(R.id.card_front_image);

        if(txtName == null) {
            txtName = (TextView) view.findViewById(R.id.card_front_hashtag);
            txtName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/calibri.ttf"));
            txtName.setShadowLayer(5, 3, 3, Color.BLACK);
        }

        if(btnToBack == null) {
            btnToBack = (Button)view.findViewById(R.id.card_front_button);
        }

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void update() {
        getInterest().addChangedListener(new Interest.ImageChangedListener() {
            @Override
            public void onChanged(Interest.InterestState state, Bitmap old, Bitmap newBmp) {
                if(state != Interest.InterestState.Add) {
                    imageView.setBackgroundColor(getInterest().getColor());
                    imageView.setImageBitmap(getInterest().getCustomImage());
                }
            }
        });

        getInterest().setStateChangedListener(new Interest.InterestStateChangedListener() {
            @Override
            public void onStateChanged(Interest.InterestState state) {
                if(state != Interest.InterestState.Add) {
                    imageView.setBackgroundColor(getInterest().getColor());
                    imageView.setImageBitmap(getInterest().getCustomImage());
                }
            }
        });

        Runnable update = new Runnable() {
            @Override
            public void run() {
                // Remove background color for "Add interest"
                if (getInterest().getState() != Interest.InterestState.Add) {
                    imageView.setBackgroundColor(getInterest().getColor());
                    imageView.setImageBitmap(getInterest().getCustomImage());
                }

                txtName.setText(getInterest().getHashtag());

                switch (getInterest().getState()) {
                    case Add:
                        imageView.setColorFilter(Color.parseColor("#FF0091EA"), PorterDuff.Mode.MULTIPLY);
                        txtName.setTextColor(TEXT_COLOR_ADD);
                        txtName.setShadowLayer(0, 0, 0, Color.BLACK);
                        break;
                    default:
                        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                        txtName.setTextColor(TEXT_COLOR_DEFAULT);
                        txtName.setShadowLayer(3, 3, 3, Color.BLACK);
                        break;
                }
            }
        };

        ((Activity) (getContext())).runOnUiThread(update);
    }
}
