package leclerc.gridder.activities.grids;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.FileNotFoundException;
import java.io.IOException;

import leclerc.gridder.cards.edit.states.EditClosedState;
import leclerc.gridder.cards.edit.states.EditState;
import leclerc.gridder.tools.Animations;
import leclerc.gridder.data.GridsLoader;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.InfiniteProgress;
import leclerc.gridder.tools.MediaLoader;
import leclerc.gridder.R;
import leclerc.gridder.activities.chat.ChatActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.zapper.User;

public class GridsActivity extends AppCompatActivity {

    private static String STR_ADD_INTEREST = "[GRID_ADD_INTEREST]";
    private static String STR_MODIFY_INTEREST = "[GRID_MODIFY_INTEREST]";

    public static InfiniteProgress PROGRESS_BAR = null;
    public static View GridsRoot = null;

    private ImageView arrowLeft, arrowRight;
    private TextView footerText;
    private ImageView footerTick;
    private Button footerButton;
    private RelativeLayout footerLayout;

    private TextView headerText;
    private ImageButton headerImgLeft, headerImgRight;

    private boolean isEditCardOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grids);

        initHeader();
        initFooter();
        initEditCard();

        // Progress ring
        ImageView imgProgress = (ImageView)findViewById(R.id.progressBar);
        PROGRESS_BAR = new InfiniteProgress(imgProgress);
        PROGRESS_BAR.stop();

        GridsRoot = findViewById(R.id.grids_root);

        GridsLoader.init(this);
        initGridGestures();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(User.getInstance().getRegId() != null)
            return;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    InstanceID instanceID = InstanceID.getInstance(GridsActivity.this);
                    String token = instanceID.getToken(GridsActivity.this.getResources().getString(R.string.project_number), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    System.out.println("TOKEN: " + token);
                    User.getInstance().setRegId(token);

                    String error = GoogleCloudMessaging.getInstance(GridsActivity.this).register(token);
                    if(error.equals(GoogleCloudMessaging.ERROR_SERVICE_NOT_AVAILABLE)) {
                        Toast.makeText(GridsActivity.this, "An error has occurred. Please restart the application.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        new GridsLoader.TaskFindUserId().execute(User.getInstance().getRegId(), String.valueOf(true));
                    }

                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(runnable).start();
    }

    private void initHeader() {
        try {
            ActionBar bar = getSupportActionBar();
            bar.setDisplayShowCustomEnabled(true);
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.layout_actionbar);
            View v = bar.getCustomView();
            TextView txtPrimaryTitle = (TextView)v.findViewById(R.id.txtPrimaryTitle);
            txtPrimaryTitle.setText(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
            txtPrimaryTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf"));

            ImageButton imgChat = (ImageButton)v.findViewById(R.id.header_goto_chat);
            final Context currentContext = this;
            imgChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(currentContext, ChatActivity.class);
                    startActivity(intent);
                }
            });
        }
        catch(PackageManager.NameNotFoundException e) {

        }
        catch(NullPointerException e) {

        }

        STR_ADD_INTEREST = getResources().getString(R.string.grid_add_interest);
        STR_MODIFY_INTEREST = getResources().getString(R.string.grid_modify_interest);

        headerText = (TextView)findViewById(R.id.grids_txtGridName);
        headerImgLeft = (ImageButton)findViewById(R.id.grids_header_imgLeft);
        headerImgRight = (ImageButton)findViewById(R.id.grids_header_imgRight);

        headerImgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentEditCard != null)
                    CurrentEditCard.cancel();
            }
        });

        headerImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentEditCard != null)
                    CurrentEditCard.getCurrentState().extra();
            }
        });
    }

    private void initFooter() {
        // Init arrows (start a conversation)
        arrowLeft = (ImageView)findViewById(R.id.grids_arrow_left);
        arrowLeft.setColorFilter(Color.parseColor("#FF81D4FA"), PorterDuff.Mode.MULTIPLY);

        arrowRight = (ImageView)findViewById(R.id.grids_arrow_right);
        arrowRight.setColorFilter(Color.parseColor("#FF81D4FA"), PorterDuff.Mode.MULTIPLY);
        // End init arrows

        footerText = (TextView)findViewById(R.id.grids_txtSwipeUpConversation);
        footerTick = (ImageView)findViewById(R.id.grids_imgSwipeUpConversation);
        footerButton = (Button)findViewById(R.id.grids_btnConfirm);

        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CurrentEditCard != null)
                    CurrentEditCard.validate();
            }
        });

        footerTick.setColorFilter(Color.parseColor("#94CC66"), PorterDuff.Mode.MULTIPLY);

        footerLayout = (RelativeLayout)findViewById(R.id.grids_footer);
    }

    private void initEditCard() {
        // Init the edit layout
        final CardEdit cardEdit = new CardEdit(this);
        cardEdit.createView(getLayoutInflater());
        cardEdit.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cardEdit.setVisibility(View.GONE);
        cardEdit.setAlpha(0.0f);

        cardEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        final GridsActivity activityInstance = this;
        cardEdit.setChangedStateListener(new CardEdit.EditChangedStateListener() {
            @Override
            public void onChangedState(EditState state) {
                isEditCardOpen = state.getClass() != EditClosedState.class;
                if(CurrentEditCard != null && CurrentEditCard.getCurrentState() != null)
                    CurrentEditCard.getCurrentState().show(activityInstance);
            }
        });

        Animations.setCardEdit(cardEdit);
        CurrentEditCard = cardEdit;
        // End init edit layout

        FrameLayout frame = (FrameLayout)findViewById(R.id.grids_frameGrid);
        frame.addView(cardEdit);
    }

    public void setArrowsVisibility(int visibility) {
        arrowLeft.setVisibility(visibility);
        arrowRight.setVisibility(visibility);
    }

    public void setFooterUsesTick(boolean usesTick) {
        footerTick.setVisibility(usesTick ? View.VISIBLE : View.GONE);
        footerText.setVisibility(usesTick ? View.GONE : View.VISIBLE);
        footerButton.setVisibility(usesTick ? View.VISIBLE : View.GONE);
        footerButton.setEnabled(usesTick);
    }

    public void setHeaderInfos(boolean isAddInterest, boolean usesLeftImg, int resLeftImg, boolean usesRightImg) {
        setHeaderInfos(isAddInterest ? STR_ADD_INTEREST : STR_MODIFY_INTEREST, usesLeftImg, resLeftImg, usesRightImg);
    }

    public void setHeaderInfos(String txt, boolean usesLeftImg, int resLeftImg, boolean usesRightImg) {
        headerText.setText(txt);
        headerImgLeft.setVisibility(usesLeftImg ? View.VISIBLE : View.GONE);
        if(usesLeftImg) {
            headerImgLeft.setImageDrawable(getResources().getDrawable(resLeftImg));
        }
        else
            headerImgLeft.setImageBitmap(null);

        headerImgRight.setVisibility(usesRightImg ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onBackPressed() {
        if(isEditCardOpen) {
            if(CurrentEditCard != null && CurrentEditCard.getCurrentState() != null)
                CurrentEditCard.getCurrentState().cancel();
        }
        else {
            Interest[] interests = User.getInstance().getCurrentGrid().getElements();

            for(Interest i : interests) {
                if(i.getCard() != null && i.getCard().getState() != null)
                i.getCard().getState().backAction();
            }
        }
    }

    private CardEdit CurrentEditCard;
    public void startPickImage(CardEdit editCard) {
        CurrentEditCard = editCard;
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), Interest.InterestImage.RESULT_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Interest.InterestImage.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            if(CurrentEditCard != null) {
                try {
                    String path = MediaLoader.getPath(this, selectedImage);
                    if(path != null) {
                        Interest interest = CurrentEditCard.getInterest();
                        Interest.InterestImage.getImage(interest, path, Interest.CARD_RES_WIDTH, Interest.CARD_RES_HEIGHT);
                    }
                }
                catch (FileNotFoundException e) {

                }
            }
        }
    }

    private void hideKeyboard() {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (Exception e) {

        }
    }

    public void updateGrid() {
        hideKeyboard();

        Runnable retrieveCards = new Runnable() {
            @Override
            public void run() {
                if(User.getInstance().getCurrentGrid().getAdapter() != null)
                    User.getInstance().getCurrentGrid().getAdapter().notifyDataSetChanged();
            }
        };

        runOnUiThread(retrieveCards);
    }

    private void initGridGestures() {
        /*final FrameLayout g = (FrameLayout)findViewById(R.id.grids_swap_frame);
        OnSwipeListener swipeListener = new OnSwipeListener(this) {
            @Override
            public void onSwipeLeft() {
                //headerText.setText(User.getInstance().getPrevGrid().getName());
                Toast.makeText(g.getContext(), "Left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeRight() {
                //headerText.setText(User.getInstance().getNextGrid().getName());
                Toast.makeText(g.getContext(), "Right", Toast.LENGTH_SHORT).show();
            }
        };

        g.setOnTouchListener(swipeListener);*/
    }
}
