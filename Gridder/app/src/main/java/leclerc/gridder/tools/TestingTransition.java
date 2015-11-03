package leclerc.gridder.tools;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;

/**
 * Created by Antoine on 2015-10-29.
 */
@TargetApi(19)
public class TestingTransition extends Transition {
    private static final String IMAGEVIEW_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:imageView";
    private static final String FRAMELAYOUT_PROPERTY_NAME =
            "leclerc.gridder.tools.testingtransition:TestingTransition:frameLayout";

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
    }

    private void captureValues(TransitionValues transitionValues) {
        if(transitionValues.view instanceof ImageView) {
            transitionValues.values.put(IMAGEVIEW_PROPERTY_NAME, transitionValues.view);
        }
        else if(transitionValues.view instanceof FrameLayout) {
            transitionValues.values.put(FRAMELAYOUT_PROPERTY_NAME, transitionValues.view);
        }
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {
        if(startValues == null || endValues == null)
            return null;

        final ImageView startImage = (ImageView)startValues.values.get(IMAGEVIEW_PROPERTY_NAME);
        final ImageView endImage = (ImageView)endValues.values.get(IMAGEVIEW_PROPERTY_NAME);

        final FrameLayout startLayout = (FrameLayout)startValues.values.get(FRAMELAYOUT_PROPERTY_NAME);
        final FrameLayout endLayout = (FrameLayout)endValues.values.get(FRAMELAYOUT_PROPERTY_NAME);

        AnimatorSet set = new AnimatorSet();



        return set;
    }
}
