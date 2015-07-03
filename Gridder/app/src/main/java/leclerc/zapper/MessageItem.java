package leclerc.zapper;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.view.Gravity;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-03-28.
 */
public class MessageItem extends LinearLayout {

    private final int BACKGROUND_COLOR = android.R.drawable.dialog_holo_light_frame;

    private String Username;
    private String LastMessage;
    private boolean IsUserLastMessage;
    private int ID;

    private TextView txtUsername;
    private TextView txtLastMessage;
    private Color clrBackground;
    private Gravity messageGravity;
    private Button btnMessage;

    private FrameLayout root;
    private LinearLayout contentLayout;

    public MessageItem(Context context) {
        super(context);

        setOrientation(HORIZONTAL);
        setPaddingRelative(0, 0, 0, 0);
        setBackground(getResources().getDrawable(android.R.drawable.dialog_holo_dark_frame));
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        setRootElevation();

        LinearLayout options = new LinearLayout(context);
        options.setOrientation(LinearLayout.HORIZONTAL);
        options.setGravity(Gravity.START);
        options.setLayoutParams(new LayoutParams(30, LayoutParams.MATCH_PARENT));

        addView(options);

        contentLayout = new LinearLayout(context);
        contentLayout.setOrientation(LinearLayout.VERTICAL);
        contentLayout.setBackgroundColor(Color.WHITE);
        contentLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        txtUsername = new TextView(context);
        txtUsername.setTextAppearance(getContext(), android.R.style.TextAppearance_Medium);
        txtUsername.setPadding(8,0,8,0);
        txtUsername.setTextSize(20);
        txtUsername.setTextColor(getResources().getColor(R.color.primary_dark_material_light));
        txtUsername.setTypeface(null, Typeface.BOLD);
        txtUsername.setTypeface(null, Typeface.ITALIC);
        txtUsername.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        contentLayout.addView(txtUsername);

        txtLastMessage = new TextView(context);
        txtLastMessage.setTextColor(getResources().getColor(R.color.primary_text_default_material_light));
        txtLastMessage.setPadding(8, 6, 8, 6);
        txtLastMessage.setTypeface(null, Typeface.ITALIC);
        txtLastMessage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        contentLayout.addView(txtLastMessage);

        addView(contentLayout);

        //TODO: PUT CONTENT IN THE BUTTON (CHILD)

        btnMessage = new Button(context);
        btnMessage.setPadding(0,0,0,0);
        btnMessage.setBackgroundColor(Color.parseColor("#00000000"));
        btnMessage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        btnMessage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                playSelectedAnimation();
            }
        });
        addView(btnMessage);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();


    }

    // ============================================================
    //  GETs & SETs of properties
    // ============================================================

    private void setUsername(String username) {
        Username = username;

        if(txtUsername != null) {
            txtUsername.setText(Username);
        }
    }

    public String getUsername() {
        return Username;
    }

    private void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;

        if(txtLastMessage != null) {
            txtLastMessage.setText(LastMessage);
        }
    }

    public String getLastMessage() {
        return LastMessage;
    }

    private void setIsUserLastMessage(boolean isUserLastMessage) {
        IsUserLastMessage = isUserLastMessage;

        if(txtLastMessage != null) {
            if(IsUserLastMessage) {
                txtLastMessage.setGravity(Gravity.END);
                txtLastMessage.setTextAppearance(getContext(), R.style.TextFromUser);
            }
            else {
                txtLastMessage.setGravity(Gravity.START);
                txtLastMessage.setTextAppearance(getContext(), R.style.TextFromOthers);
            }
        }
    }

    public boolean getIsUserLastMessage() {
        return IsUserLastMessage;
    }

    // ============================================================
    //  Updates
    // ============================================================

    public void update(int id, String username, String lastMessage, boolean isUserLastMessage) {
        ID = id;

        setUsername(username);
        setLastMessage(lastMessage);
        setIsUserLastMessage(isUserLastMessage);
    }

    public void update(int id, String username, String lastMessage) {
        update(id, username, lastMessage, false);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setRootElevation() {
        setElevation(50);
        setTranslationZ(50);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void playSelectedAnimation() {
        //btnMessage.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.xml.selectedmessageanim));

        /*ViewPropertyAnimator anim = animate();
        anim.setDuration(1000);
        anim.z(1);

        anim.setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTranslationZ((float) animation.getAnimatedValue());
            }
        });

        anim.start();*/

        int cx = (getLeft() + getRight()) / 2;
        int cy = (getTop() + getBottom()) / 2;

        int finalRadius = Math.max(getWidth(), getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(contentLayout, cx, cy, 0, finalRadius);

        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void playMoveAway(int direction) {

    }
}
