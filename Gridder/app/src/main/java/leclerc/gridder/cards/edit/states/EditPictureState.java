package leclerc.gridder.cards.edit.states;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.Animations;

/**
 * Created by Antoine on 2015-05-25.
 */
public class EditPictureState extends EditStateContext {
    private CheckBox cbUseCustom = null;
    private ImageView imgCustom = null;
    private GridsActivity gridActivity = null;

    public EditPictureState(CardEdit parent) {
        super(parent);
    }

    @Override
    public void init() {
        if(getParent().getPictureView().getChildCount() > 0) {
            RelativeLayout layout = (RelativeLayout) getParent().getPictureView().getChildAt(0);
            if(layout != null) {
                cbUseCustom = (CheckBox) layout.findViewById(R.id.card_edit_picture_checkbox);
                imgCustom = (ImageView) layout.findViewById(R.id.card_edit_picture_image);

                cbUseCustom.setChecked(getParent().getTmpInterestState() == Interest.InterestState.Custom);
                cbUseCustom.setEnabled(getParent().getTmpCustomImage() != null);
                imgCustom.setImageBitmap(getParent().getTmpCustomImage());
            }
        }

        Animations.getEditCardFadeOutButtonAnimation(getParent().getLeftButton()).start();
        Animations.getEditCardFadeOutButtonAnimation(getParent().getRightButton()).start();
        Animations.getEditCardFadeOutButtonAnimation(getParent().getEditHashtag()).start();
    }

    @Override
    public void validate() {
        getParent().setIsUsingCustom(cbUseCustom.isChecked(), ((BitmapDrawable) imgCustom.getDrawable()).getBitmap());
        hide();
    }

    @Override
    public void cancel() {
        hide();
    }

    @Override
    public void extra() {
        // TODO: Import picture from phone
        gridActivity.startPickImage(getParent());
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        gridsActivity.setArrowsVisibility(View.GONE);
        gridsActivity.setFooterUsesTick(true);
        gridsActivity.setHeaderInfos("Interest's image", true, R.drawable.ic_action_import, true);

        gridActivity = gridsActivity;
    }

    private void hide() {
        AnimatorSet animation = new AnimatorSet();

        AnimatorSet set = Animations.scaleOut(getParent().getContext(), getParent().getPictureView());
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
