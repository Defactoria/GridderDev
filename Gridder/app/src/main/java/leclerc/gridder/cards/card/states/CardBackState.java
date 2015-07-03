package leclerc.gridder.cards.card.states;

import android.animation.Animator;
import android.animation.AnimatorSet;

import leclerc.gridder.cards.InterestCard;
import leclerc.gridder.tools.Animations;

/**
 * Created by Antoine on 2015-05-24.
 */
public class CardBackState extends CardStateContext {
    public CardBackState(InterestCard parent) {
        super(parent);
        setState(this);
    }

    @Override
    public void backAction() {
        getParent().setState(new CardFrontState(getParent()));
    }

    @Override
    public void nextAction() {
        //go to Edit card
        Animations.getGoToEditAnimation(getParent().getContext(), getParent().getInterest().getIndex()).start();
    }

    @Override
    public void nextActionLong() {
        nextAction();
    }

    @Override
    public void init() {
        AnimatorSet flip = getParent().flipToBack();

        if(flip != null) {
            AnimatorSet set = new AnimatorSet();
            AnimatorSet fadeIn = Animations.getFadeOutOtherCardsAnimation(getParent().getInterest().getIndex(), true);

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
}
