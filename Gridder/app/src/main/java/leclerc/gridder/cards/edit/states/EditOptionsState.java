package leclerc.gridder.cards.edit.states;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.AdapterFactory;
import leclerc.gridder.tools.Animations;

/**
 * Created by Antoine on 2015-05-17.
 */
public class EditOptionsState extends EditStateContext {
    private CheckBox cbUsingColor = null;
    private int color = Color.BLACK;

    public EditOptionsState(CardEdit parent) {
        super(parent);
    }

    @Override
    public void init() {
        if(getParent().getOptionsView().getChildCount() > 0) {
            RelativeLayout layout = (RelativeLayout)getParent().getOptionsView().getChildAt(0);
            cbUsingColor = (CheckBox)layout.findViewById(R.id.card_edit_options_checkbox);
            color = getParent().getTmpColor();

            cbUsingColor.setChecked(getParent().getTmpInterestState() == Interest.InterestState.Add ||
                    getParent().getTmpInterestState() == Interest.InterestState.Color);

            cbUsingColor.setEnabled(getParent().getTmpCustomImage() != null);

            AdapterFactory.getCustomColors(getParent().getContext()).updateColor(getParent().getTmpColor());
        }

        Animations.getEditCardFadeOutButtonAnimation(getParent().getLeftButton()).start();
        Animations.getEditCardFadeOutButtonAnimation(getParent().getRightButton()).start();
        Animations.getEditCardFadeOutButtonAnimation(getParent().getEditHashtag()).start();
    }

    @Override
    public void validate() {
        if(cbUsingColor != null) {
            getParent().setIsUsingColor(cbUsingColor.isChecked(), AdapterFactory.getCustomColors(getParent().getContext()).getCurrentColor());
        }

        hide();
    }

    @Override
    public void cancel() {
        hide();
    }

    @Override
    public void extra() {
        // DO NOTHING
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        gridsActivity.setArrowsVisibility(View.GONE);
        gridsActivity.setFooterUsesTick(true);
        gridsActivity.setHeaderInfos("Interest's options", false, 0, true);
    }

    private void hide() {
        AnimatorSet animation = new AnimatorSet();

        AnimatorSet set = Animations.scaleOut(getParent().getContext(), getParent().getOptionsView());
        if(set != null) {
            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    getParent().setState(new EditDefaultState(getParent()));
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
        }

        AnimatorSet fade = Animations.getFadeInAnimation(getParent().getContext(), getParent().getLayout());

        animation.playTogether(set, fade);
        animation.start();
    }
}
