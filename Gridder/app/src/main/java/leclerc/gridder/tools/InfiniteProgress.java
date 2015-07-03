package leclerc.gridder.tools;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Antoine on 2015-05-27.
 */
public class InfiniteProgress {
    private ImageView imgProgress;

    private AnimatorSet currentAnimator;

    public InfiniteProgress(ImageView parent) {
        imgProgress = parent;

        init();
    }

    private void init() {
        ObjectAnimator firstHalf = ObjectAnimator.ofFloat(imgProgress, "rotation", 0.0f, 180.0f);
        ObjectAnimator lastHalf = ObjectAnimator.ofFloat(imgProgress, "rotation", 180.0f, 360.0f);

        currentAnimator = new AnimatorSet();
        currentAnimator.playSequentially(firstHalf, lastHalf);

        currentAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void start() {
        if(imgProgress != null) {
            imgProgress.setVisibility(View.VISIBLE);
            currentAnimator.start();
        }
    }

    public void stop() {
        if(imgProgress != null) {
            imgProgress.setVisibility(View.GONE);
            currentAnimator.cancel();
        }
    }
}
