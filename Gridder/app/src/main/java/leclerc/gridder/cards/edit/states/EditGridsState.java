package leclerc.gridder.cards.edit.states;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.gridder.data.User;
import leclerc.gridder.tools.Animations;

/**
 * Created by Lorraine on 2015-07-03.
 */
public class EditGridsState extends EditStateContext {
    public EditGridsState(CardEdit parent) {
        super(parent);
    }

    @Override
    public void validate() {
        extra();
    }

    @Override
    public void cancel() {
        extra();
    }

    @Override
    public void extra() {
        // RETURN TO CURRENT GRID

        AnimatorSet goToCurrent = Animations.getGoToCurrentGridAnimation(Animations.CARD_EDIT);
        goToCurrent.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getParent().setState(new EditClosedState(getParent()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        goToCurrent.start();
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        gridsActivity.setArrowsVisibility(View.GONE);
        gridsActivity.setFooterUsesTick(true);
        gridsActivity.setHeaderInfos("YOUR GRIDS", true, R.drawable.ic_action_arrow_left, false);
    }
}
