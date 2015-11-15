package leclerc.gridder.cards.card.states;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.text.method.MovementMethod;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

import leclerc.gridder.R;
import leclerc.gridder.activities.edition.EditionActivity;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardFront;
import leclerc.gridder.cards.InterestCard;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.Animations;
import leclerc.gridder.data.User;

/**
 * Created by Antoine on 2015-05-24.
 */
public class CardFrontState extends CardStateContext {
    public CardFrontState(InterestCard parent) {
        super(parent);

        setState(this);
    }

    @Override
    public void backAction() {
        // DO NOTHING
    }

    @Override
    public void nextAction() {
        // If we are not cancelling a flip (a card is already flipped)
        if(!cancelFlip()) {
            if(getParent().getInterest() != null &&
                    getParent().getInterest().getState() == Interest.InterestState.Add) {
                Animations.getGoToEditAnimation(getParent().getContext(), getParent().getInterest().getIndex()).start();
            }
            else {
                //getParent().setState(new CardBackState(getParent()));
                CardFront front = getParent().Front;

                GridsActivity gridsActivity = (GridsActivity)getParent().getContext();
                View v = gridsActivity.findViewById(android.R.id.statusBarBackground);
                gridsActivity.setStatusBarView(v);

                Intent intent = new Intent(gridsActivity, EditionActivity.class);
                intent.putExtra(EditionActivity.ID, getParent().getInterest().getId());

                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        gridsActivity,
                        new Pair<View, String>(front.getFrameView(), gridsActivity.getString(R.string.transition_name_interest_frame)),
                        new Pair<View, String>(front.getImageView(), gridsActivity.getString(R.string.transition_name_interest_image)),
                        new Pair<View, String>(front.getHashtagView(), gridsActivity.getString(R.string.transition_name_interest_name)),
                        new Pair<View, String>((gridsActivity).getStatusBarView(), Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME)
                );
                ActivityCompat.startActivity(gridsActivity, intent, options.toBundle());
            }
        }
    }

    @Override
    public void nextActionLong() {
        if(!cancelFlip()) {
            Animations.getGoToEditAnimation(getParent().getContext(), getParent().getInterest().getIndex()).start();
        }
    }

    @Override
    public void init() {
        AnimatorSet flip = getParent().flipToFront();

        if(flip != null) {
            AnimatorSet set = new AnimatorSet();
            AnimatorSet fadeIn = Animations.getFadeInOtherCardsAnimation(getParent().getInterest().getIndex());

            set.playTogether(flip, fadeIn);

            set.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    InterestCard.IsAnimated = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    InterestCard.IsAnimated = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            set.start();
        }
    }

    private boolean cancelFlip()
    {
        Interest[] interests = User.getInstance().getCurrentGrid().getElements();

        boolean cancellingFlip = false;

        for(Interest i : interests)
        {
            InterestCard card = i.getCard();

            // Check if another card but this one is flipped
            if(card != null && !card.equals(getParent())) {
                CardState state = card.getState();
                if(state != null) {
                    if (state.getClass() == CardBackState.class) {
                        cancellingFlip = true;
                        card.setState(new CardFrontState(card));
                    }
                }
            }
        }

        return cancellingFlip;
    }
}
