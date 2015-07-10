package leclerc.gridder.cards.edit.states;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.gridder.cards.InterestCard;
import leclerc.gridder.data.GridsLoader;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.Animations;
import leclerc.gridder.data.User;

/**
 * Created by Antoine on 2015-05-17.
 */
public class EditDefaultState extends EditStateContext {

    public EditDefaultState(CardEdit parent) {
        super(parent);
    }

    @Override
    public void init() {
        Animations.getEditCardFadeInButtonAnimation(getParent().getLeftButton()).start();
        Animations.getEditCardFadeInButtonAnimation(getParent().getRightButton()).start();
        Animations.getEditCardFadeInButtonAnimation(getParent().getEditHashtag()).start();
    }

    @Override
    public void validate() {
        if (getParent().getText().length() == 0) {
            // TODO: Show dialog saying that the interest's name is invalid.

            return;
        }

        long id = Interest.NEEDS_UPDATE_ID;

        if(getParent().getInterest().getState() != Interest.InterestState.Add)
            id = getParent().getInterest().getId();

        final Interest newInterest = new Interest(id, getParent().getText());
        newInterest.init(getParent().getInterest().getState() == Interest.InterestState.Custom,
                getParent().getTmpCustomImage(),
                getParent().getTmpColor());

        newInterest.setState(getParent().getTmpInterestState());
        // If the user validates with not using the custom image, update custom as null in DB.
        newInterest.setCustomImageSrc(newInterest.getState() == Interest.InterestState.Custom ? getParent().getInterest().getCustomImageSrc() : null);

        User.getInstance().getCurrentGrid().replaceElement(getParent().getInterest(), newInterest);

        InterestCard card = newInterest.getCard();
        if (card != null) {
            GridsActivity activity = (GridsActivity) getParent().getContext();
            activity.updateGrid();
        }

        getParent().resetTmpValues();

        // Update in DB
        GridsLoader.update(newInterest);

        AnimatorSet anim = Animations.getGoToGridAnimation(getParent().getContext(), newInterest.getIndex());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (newInterest.getCard() != null)
                    newInterest.getCard().onLoaded(newInterest);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        anim.start();
    }

    @Override
    public void cancel() {
        //getParent().getInterest().setState(getParent().getTmpInterestState());
        getParent().resetTmpValues();
        Animations.getGoToGridAnimation(getParent().getContext(), getParent().getInterest().getIndex()).start();
    }

    @Override
    public void extra() {
        // TODO: Delete all information from card
        AlertDialog.Builder builder = new AlertDialog.Builder(getParent().getContext());

        builder.setMessage("You are about to delete this interest. Do you really want to delete it?")
                .setTitle("Delete interest")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getParent().resetTmpValues();

                        Interest newInterest = new Interest();
                        User.getInstance().getCurrentGrid().replaceElement(getParent().getInterest(), newInterest);

                        getParent().init(newInterest);
                        Animations.getGoToGridAnimation(getParent().getContext(), newInterest.getIndex()).start();
                    }
                });

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        gridsActivity.setArrowsVisibility(View.GONE);
        gridsActivity.setFooterUsesTick(true);
        gridsActivity.setHeaderInfos(getParent().getInterest().isAddInterest(), true, R.drawable.ic_action_trash, true);
    }
}
