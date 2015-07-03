package leclerc.gridder.cards.card.states;

import android.animation.Animator;
import android.animation.AnimatorSet;

import leclerc.gridder.cards.InterestCard;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.Animations;
import leclerc.zapper.User;

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
                getParent().setState(new CardBackState(getParent()));
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
