package leclerc.gridder.tools;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * Created by Antoine on 2015-10-29.
 */
@TargetApi(21)
public class TestingTransition extends Transition {
    private static final String STATUSBAR_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:statusBar";

    private static final String ANDROID_STATUSBAR_NAME = Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME;

    private static final String NAVIGATIONBAR_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:navigationBar";

    private static final String ANDROID_NAVIGATIONBAR_NAME = Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME;

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String transitionName = transitionValues.view.getTransitionName();

            if(transitionName == null)
                return;

            if (transitionName.equals(ANDROID_STATUSBAR_NAME)) {
                transitionValues.values.put(STATUSBAR_PROPERTY_NAME, transitionValues.view);
            } else if (transitionName.equals(ANDROID_NAVIGATIONBAR_NAME)) {
                transitionValues.values.put(NAVIGATIONBAR_PROPERTY_NAME, transitionValues.view);
            }
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if(startValues == null || endValues == null)
            return null;

        final View startStatus = (View)startValues.values.get(STATUSBAR_PROPERTY_NAME);
        final View endStatus = (View)endValues.values.get(STATUSBAR_PROPERTY_NAME);

        AnimatorSet set = new AnimatorSet();

        if(startStatus != null && endStatus != null) {
            Animator colorStatusValue = changeColor(endStatus, ((ColorDrawable)startStatus.getBackground()).getColor(), ((ColorDrawable)endStatus.getBackground()).getColor());

            set.play(colorStatusValue);
        }

        return set;
    }

    private Animator changeColor(final View view, int from_color, int to_color) {
        final float[] from = new float[3],
                        to = new float[3];

        String hexColor_from = String.format("#%06X", (0xFFFFFF & from_color));
        String hexColor_to = String.format("#%06X", (0xFFFFFF & to_color));

        Color.colorToHSV(Color.parseColor(hexColor_from), from);
        Color.colorToHSV(Color.parseColor(hexColor_to), to);



        ValueAnimator anim = ValueAnimator.ofFloat(0.0f, 1.0f);

        final float[] hsv = new float[3];
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Object value = animation.getAnimatedValue();
                if(value != null) {
                    float v = (float)value;

                    hsv[0] = from[0] + (to[0] - from[0]) * v;
                    hsv[1] = from[1] + (to[1] - from[1]) * v;
                    hsv[2] = from[2] + (to[2] - from[2]) * v;

                    view.setBackgroundColor(Color.HSVToColor(hsv));
                }
            }
        });

        return anim;
    }
}
