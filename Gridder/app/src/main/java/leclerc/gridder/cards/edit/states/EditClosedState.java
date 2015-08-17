package leclerc.gridder.cards.edit.states;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.drawable.VectorDrawable;
import android.view.View;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.gridder.data.User;
import leclerc.gridder.tools.Animations;

/**
 * Created by Antoine on 2015-05-17.
 */
public class EditClosedState extends EditStateContext {

    public EditClosedState(CardEdit parent) {
        super(parent);
    }

    @Override
    public void validate() {
        // NOTHING TO DO HERE
    }

    @Override
    public void cancel() {
        // NOTHING TO DO HERE
    }

    @Override
    public void extra() {
        getParent().init(User.getInstance().getGrids());
        AnimatorSet goToGrids = Animations.getGoToGridsAnimation(Animations.CARD_EDIT);
        goToGrids.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getParent().setState(new EditGridsState(getParent()));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        goToGrids.start();
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        gridsActivity.setArrowsVisibility(View.VISIBLE);
        gridsActivity.setFooterUsesTick(false);
        if(User.getInstance().getCurrentGrid() == null)
            gridsActivity.setHeaderInfos("Loading...", true, R.drawable.ic_action_grids, false);
        else
            gridsActivity.setHeaderInfos(User.getInstance().getCurrentGrid().getName(), true, R.drawable.ic_action_grids, false);
    }
}
