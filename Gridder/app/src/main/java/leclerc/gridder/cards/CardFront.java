package leclerc.gridder.cards;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;

import java.util.Random;

import leclerc.gridder.data.Interest;
import leclerc.gridder.R;
import leclerc.gridder.tools.ILoading;

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
    private View frmNameWithImage;
    private Button btnToBack;

    public CardFront(Context context) {
        super(context);

        TEXT_COLOR_ADD = context.getResources().getColor(R.color.text_color);
        setDrawingCacheEnabled(true);
    }

    public Interest getInterest() {
        return mInterestData;
    }

    public TextView getHashtagView() {
        return txtName;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public View getFrameView() {
        return frmNameWithImage;
    }

    public View getBaseFrame() { return findViewById(R.id.card_front_base); }

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

        mInterestData.updateVisual();

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
        }
        txtName.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "fonts/calibri.ttf"));
        //txtName.setShadowLayer(5, 3, 3, Color.BLACK);

        // Frame for API 21+
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (frmNameWithImage == null) {
                frmNameWithImage = view.findViewById(R.id.card_front_frame);
            }
        }

        if(btnToBack == null) {
            btnToBack = (Button)view.findViewById(R.id.card_front_button);
        }

        updateFrameColor();

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void update() {
        getInterest().addChangedListener(new Interest.ImageChangedListener() {
            @Override
            public void onChanged(Interest.InterestState state, Bitmap old, Bitmap newBmp) {
                if (state != Interest.InterestState.Add) {
                    updateImages();
                    /*imageView.setBackgroundColor(getInterest().getColor());
                    imageView.setImageBitmap(getInterest().getCustomImage());*/
                }
            }
        });

        getInterest().setStateChangedListener(new Interest.InterestStateChangedListener() {
            @Override
            public void onStateChanged(Interest.InterestState state) {
                if(state != Interest.InterestState.Add) {
                    updateImages();
                    /*imageView.setBackgroundColor(getInterest().getColor());
                    imageView.setImageBitmap(getInterest().getCustomImage());*/
                }


            }
        });

        Runnable update = new Runnable() {
            @Override
            public void run() {
                // Remove background color for "Add interest"
                if (getInterest().getState() != Interest.InterestState.Add) {
                    updateImages();
                    /*imageView.setBackgroundColor(getInterest().getColor());
                    imageView.setImageBitmap(getInterest().getCustomImage());*/
                }

                txtName.setText(getInterest().getHashtag());

                switch (getInterest().getState()) {
                    case Add:
                        imageView.setColorFilter(Color.parseColor("#FF81D4FA"), PorterDuff.Mode.MULTIPLY);
                        imageView.setBackgroundColor(Color.WHITE);
                        txtName.setTextColor(TEXT_COLOR_ADD);
                        txtName.setShadowLayer(0, 0, 0, Color.BLACK);

                        /*if(loadingParent != null)
                            loadingParent.onLoaded(this);*/

                        break;
                    case Color:
                        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                        txtName.setTextColor(TEXT_COLOR_DEFAULT);
                        //txtName.setShadowLayer(3, 3, 3, Color.BLACK);
                        break;
                    case Custom:
                        imageView.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                        txtName.setTextColor(TEXT_COLOR_DEFAULT);
                        //txtName.setShadowLayer(3, 3, 3, Color.BLACK);
                        break;
                }
            }
        };

        ((Activity) (getContext())).runOnUiThread(update);
    }

    private void updateImages() {
        imageView.setBackgroundColor(getInterest().getColor());
        imageView.setImageBitmap(getInterest().getCustomImage());
        // LOADED

        getInterest().updateVisual();
        updateFrameColor();

        /*if(loadingParent != null)
            loadingParent.onLoaded(this);*/
    }

    @TargetApi(21)
    public void updateFrameColor() {
        if(frmNameWithImage != null && getInterest() != null) {
            frmNameWithImage.setBackgroundColor(getInterest().getFrameBaseColor());
        }
        if(txtName != null && getInterest() != null) {
            txtName.setTextColor(getInterest().getBodyTextColor());
        }
    }

    private ILoading loadingParent;
    public void setLoadingParent(ILoading parent) {
        loadingParent = parent;
    }
}
