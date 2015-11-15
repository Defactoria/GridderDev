package leclerc.gridder.activities.edition;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.DataSetObserver;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.graphics.Palette;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;

import java.util.List;

import leclerc.gridder.R;
import leclerc.gridder.activities.edition.CategoryInfoView;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.data.Interest;
import leclerc.gridder.data.User;
import leclerc.gridder.tools.AdapterFactory;
import leclerc.gridder.tools.AddingGridButton;
import leclerc.gridder.tools.PaletteColors;
import leclerc.gridder.tools.TestingTransition;

/**
 * Created by Antoine on 2015-09-07.
 */
public class EditionActivity extends Activity {
    public final static String ID = "key:Interest_ID";

    private FrameLayout layout;
    private FrameLayout backgroundLayout;
    private Button btnSwitchBackground;
    private Button btnModifyName;

    private TextView headerText;

    //private RelativeLayout Header;

    //private ExpandableListView categoriesList;

    ImageView imageView;

    private Interest CurrentInterest;

    private PaletteColors Colors;

    @Override
    public void onEnterAnimationComplete() {
        super.onEnterAnimationComplete();

        btnModifyName.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditionActivity instance = this;
        Colors = new PaletteColors();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TransitionSet setEnter = new TransitionSet();
            //setEnter.addTransition(new AutoTransition());
            setEnter.addTransition(new ChangeBounds()).addTransition(new TestingTransition(TestingTransition.MODE_ENTER));
            //set.addTransition(new Explode());
            setEnter.excludeTarget(android.R.id.statusBarBackground, true);
            setEnter.setOrdering(TransitionSet.ORDERING_TOGETHER);
            //setEnter.setDuration(10000);

            // Entering previous activity
            setEnter.addListener(new Transition.TransitionListener() {
                @TargetApi(21)
                @Override
                public void onTransitionStart(Transition transition) {
                    if (btnModifyName != null)
                        btnModifyName.setVisibility(View.GONE);

                    int app_primary_color = getResources().getColor(R.color.app_primary);
                    int color = Colors.Vibrant != null ? Colors.Vibrant.getRgb() : Colors.DarkMuted != null ? Colors.DarkMuted.getRgb() : app_primary_color;

                    ValueAnimator animColor = ValueAnimator.ofArgb(getWindow().getStatusBarColor(), color);
                    animColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Object value = animation.getAnimatedValue();
                            if(value != null) {
                                int c = (int)value;
                                getWindow().setStatusBarColor(c);
                            }
                        }
                    });
                    animColor.start();
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    AnimatorSet animBtnModifyNameIn = (AnimatorSet) AnimatorInflater.loadAnimator(instance, R.animator.anim_pop_in);
                    animBtnModifyNameIn.setTarget(btnModifyName);

                    AnimatorSet switchBackgroundAnim = (AnimatorSet) AnimatorInflater.loadAnimator(instance, R.animator.anim_pop_in);
                    switchBackgroundAnim.setTarget(btnSwitchBackground);

                    AnimatorSet buttonsAnim = new AnimatorSet();
                    buttonsAnim.playTogether(animBtnModifyNameIn, switchBackgroundAnim);
                    buttonsAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            btnModifyName.setVisibility(View.VISIBLE);
                            btnSwitchBackground.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    buttonsAnim.start();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

            TransitionSet setExit = new TransitionSet();
            //setExit.addTransition(new ChangeBounds());
            //setExit.addTransition(new AutoTransition());
            setExit.addTransition(new ChangeBounds()).addTransition(new TestingTransition(TestingTransition.MODE_EXIT));
            setExit.setOrdering(TransitionSet.ORDERING_TOGETHER);
            //setExit.setDuration(10000);

            setExit.addListener(new Transition.TransitionListener() {
                @TargetApi(21)
                @Override
                public void onTransitionStart(Transition transition) {
                    AnimatorSet animBtnModifyNameOut = (AnimatorSet) AnimatorInflater.loadAnimator(instance, R.animator.anim_pop_out);
                    animBtnModifyNameOut.setTarget(btnModifyName);

                    AnimatorSet switchBackgroundAnim = (AnimatorSet) AnimatorInflater.loadAnimator(instance, R.animator.anim_pop_out);
                    switchBackgroundAnim.setTarget(btnSwitchBackground);

                    AnimatorSet buttonsAnim = new AnimatorSet();
                    buttonsAnim.playTogether(animBtnModifyNameOut, switchBackgroundAnim);
                    buttonsAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            btnModifyName.setVisibility(View.VISIBLE);
                            btnSwitchBackground.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            btnModifyName.setVisibility(View.GONE);
                            btnSwitchBackground.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    buttonsAnim.start();

                    ValueAnimator animColor = ValueAnimator.ofArgb(getWindow().getStatusBarColor(), instance.getResources().getColor(R.color.app_status));
                    animColor.setInterpolator(new AccelerateDecelerateInterpolator());
                    animColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            Object value = animation.getAnimatedValue();
                            if (value != null) {
                                int c = (int) value;
                                getWindow().setStatusBarColor(c);
                            }
                        }
                    });
                    animColor.start();
                }

                @Override
                public void onTransitionEnd(Transition transition) {

                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });

            getWindow().setSharedElementExitTransition(setExit);
            getWindow().setSharedElementEnterTransition(setEnter);

            getWindow().setSharedElementReturnTransition(setExit);
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.preview_edit_activity);

        btnModifyName = (Button)findViewById(R.id.edit_header_modify);
        imageView = (ImageView)findViewById(R.id.edit_header_image);
        btnSwitchBackground = (Button)findViewById(R.id.edit_header_switch);
        layout = (FrameLayout) findViewById(R.id.edit_header_name_layout);
        backgroundLayout = (FrameLayout)findViewById(R.id.edit_header_image_layout);

        //categoriesList = (ExpandableListView)findViewById(R.id.edit_categories_list);

        btnModifyName.setVisibility(View.GONE);
        btnSwitchBackground.setVisibility(View.GONE);

        if(User.getInstance().getCurrentGrid() != null && getIntent().getExtras() != null) {
            Interest[] interests = User.getInstance().getCurrentGrid().getInterests(getIntent().getExtras().getLong(ID));
            if (interests.length > 0) {
                CurrentInterest = interests[0];

                ((TextView) (findViewById(R.id.edit_header_name))).setText(CurrentInterest.getName());
                imageView.setImageBitmap(CurrentInterest.getCustomImage());
                backgroundLayout.setBackgroundColor(CurrentInterest.getColor());
                Colors = CurrentInterest.getColors();
            }
        }

        CategoryInfoView categoryInfoView = (CategoryInfoView)findViewById(R.id.edit_categories_view);
        categoryInfoView.updateInfos(CurrentInterest);

        UpdateColorsWithPalette();
    }

    @TargetApi(21)
    private void UpdateColorsWithPalette() {
        if(Colors == null)
            return;

        int app_primary_color = getResources().getColor(R.color.app_primary);
        int color = Colors.Vibrant != null ? Colors.Vibrant.getRgb() : Colors.DarkMuted != null ? Colors.DarkMuted.getRgb() : app_primary_color;

        layout.setBackgroundColor(color);

        backgroundLayout.setBackgroundColor(color);

        Window w = getWindow();
        //w.setStatusBarColor(color);
        w.setNavigationBarColor(color);

        TextView t = (TextView) findViewById(R.id.edit_header_name);
        int textColor = Colors.Vibrant != null ? Colors.Vibrant.getBodyTextColor() :
                        Colors.DarkMuted != null ? Colors.DarkMuted.getBodyTextColor() : Color.parseColor("#FFFFFF");
        t.setTextColor(textColor);

        int buttonTextColor = Colors.Vibrant != null ? Colors.Vibrant.getTitleTextColor() : textColor;
        btnSwitchBackground.setBackgroundTintList(ColorStateList.valueOf(color));
        btnSwitchBackground.setTextColor(buttonTextColor);

        btnModifyName.setTextColor(buttonTextColor);
    }

    @Override
    public void onBackPressed() {
        btnModifyName.setVisibility(View.GONE);
        btnSwitchBackground.setVisibility(View.GONE);
        super.onBackPressed();
        //animShrinkHeader();
    }

    private boolean savedNotificationActive = false;
    // Show saved notification
    public void showSavedNotification() {
        if(!savedNotificationActive) {
            Animator notif = AnimatorInflater.loadAnimator(this, R.animator.anim_saved_prompt_in);
            final View target = findViewById(R.id.edition_saved_prompt);
            notif.setTarget(target);
            notif.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    savedNotificationActive = true;
                    target.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    savedNotificationActive = false;
                    target.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            notif.start();
        }
    }

    static boolean shrinked = false;
    @TargetApi(21)
    private void animShrinkHeader() {
        shrinked = !shrinked;
    }

    private void updateHeader() {

    }
}