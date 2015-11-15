package leclerc.gridder.tools;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntArrayEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.ViewAnimator;

import org.w3c.dom.Text;

/**
 * Created by Antoine on 2015-10-29.
 */
@TargetApi(21)
public class TestingTransition extends Transition {
    private static final String PADDING_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:padding";

    private static final String TEXTSIZE_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:textSize";

    private static final String BACKGROUND_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:background";

    public static final int MODE_ENTER = 0;
    public static final int MODE_EXIT = 1;
    private int currentMode = MODE_ENTER;

    public TestingTransition(int mode) {
        currentMode = mode;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    public void captureValues(TransitionValues transitionValues) {
        transitionValues.values.put(BACKGROUND_PROPERTY_NAME, transitionValues.view.getBackground());

        if(transitionValues.view instanceof TextView) {
            TextView v = (TextView)transitionValues.view;
            transitionValues.values.put(TEXTSIZE_PROPERTY_NAME, v.getTextSize());

            if(v.getTransitionName() != null && v.getTransitionName().equals("transition:INTEREST_NAME")) {
                int[] padding = {
                        transitionValues.view.getPaddingTop(),
                        transitionValues.view.getPaddingEnd(),
                        transitionValues.view.getPaddingBottom(),
                        transitionValues.view.getPaddingStart()};

                transitionValues.values.put(PADDING_PROPERTY_NAME, padding);
            }
        }
    }

    public Animator createSwitchColorAnimation(final View v, int toColor) {
        if(v.getBackground() instanceof ColorDrawable) {
            ColorDrawable colorDrawable = (ColorDrawable) v.getBackground();

            int c = colorDrawable.getColor();
            int r = Color.red(c);
            int g = Color.green(c);
            int b = Color.blue(c);

            int finalDestColor = currentMode == MODE_ENTER ? toColor : Color.argb(128, Color.red(toColor), Color.green(toColor), Color.blue(toColor));

            final ValueAnimator colorAnim = ObjectAnimator.ofArgb(Color.argb(currentMode == MODE_ENTER ? 180 : 255, r, g, b), finalDestColor);
            colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object value = animation.getAnimatedValue();
                    if (value != null) {
                        v.setBackgroundColor((int) value);
                    }
                }
            });

            return colorAnim;

        }
        return null;
    }

    public Animator createTextSizeAnimation(final TextView view, float destSize) {
        ValueAnimator a = ObjectAnimator.ofFloat(view.getTextSize(), destSize);

        a.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object value = animation.getAnimatedValue();
                if(value != null) {
                    view.setTextSize(((float)value)/(view.getContext().getResources().getDisplayMetrics().scaledDensity));
                }
            }
        });

        return a;
    }

    public Animator createPaddingAnimation(final TextView view, int[] padding) {
        int[] view_padding = {
                view.getPaddingTop(),
                view.getPaddingEnd(),
                view.getPaddingBottom(),
                view.getPaddingStart() };

        ValueAnimator anim = ObjectAnimator.ofObject(new IntArrayEvaluator(), view_padding, padding);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object value = animation.getAnimatedValue();
                if(value != null) {
                    int[] p = (int[])value;

                    view.setPadding(p[3],
                                    p[0],
                                    p[1],
                                    p[2]);
                }
            }
        });

        return anim;
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if(startValues != null && endValues != null) {
            AnimatorSet anim = new AnimatorSet();

            AnimatorSet colorAnim = new AnimatorSet();

            // BACKGROUND COLOR FOR HEADER LAYOUT IN EDITION ACTIVITY
            if(endValues.values.get(BACKGROUND_PROPERTY_NAME) instanceof ColorDrawable && endValues.view.getTransitionName() != null && endValues.view.getTransitionName().equals("transition:INTEREST_FRAME")) {
                ColorDrawable colorDrawable = (ColorDrawable) endValues.values.get(BACKGROUND_PROPERTY_NAME);

                if (colorDrawable != null) {
                    int c = colorDrawable.getColor();
                    int r = Color.red(c);
                    int g = Color.green(c);
                    int b = Color.blue(c);

                    int o = colorDrawable.getOpacity();
                    colorAnim.play(createSwitchColorAnimation(startValues.view, Color.argb(colorDrawable.getAlpha(), r, g, b)));
                }
            }

            AnimatorSet textSizeAnim = new AnimatorSet();
            if(endValues.values.get(TEXTSIZE_PROPERTY_NAME) != null) {
                textSizeAnim.play(createTextSizeAnimation((TextView) startValues.view, (float) endValues.values.get(TEXTSIZE_PROPERTY_NAME)));
            }

            AnimatorSet paddingAnim = new AnimatorSet();
            if(endValues.values.get(PADDING_PROPERTY_NAME) != null) {
                paddingAnim.play(createPaddingAnimation((TextView) startValues.view, (int[]) endValues.values.get(PADDING_PROPERTY_NAME)));
            }

            anim.playTogether(colorAnim, textSizeAnim, paddingAnim);

            return anim;
        }
        else {
            return null;
        }
    }
}
