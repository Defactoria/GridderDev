package leclerc.gridder.cards.card.states;

import leclerc.gridder.cards.InterestCard;

/**
 * Created by Antoine on 2015-05-24.
 */
public class CardStateContext implements CardState {
    private CardState mState;
    private InterestCard mParent;

    public CardStateContext(InterestCard parent) {
        mParent = parent;
    }

    protected InterestCard getParent() {
        return mParent;
    }

    public CardState getState() { return mState; }

    public void setState(CardState state) {
        if(mState == null || (state != null && !mState.getClass().equals(state.getClass()))) {
            CardState tmpState = mState;
            mState = state;

            if(mState != null && tmpState != null) {
                mState.init();
            }
        }
    }

    @Override
    public void backAction() {
        if(mState != null)
            mState.backAction();
    }

    @Override
    public void nextAction() {
        if(mState != null)
            mState.nextAction();
    }

    @Override
    public void nextActionLong() {
        if(mState != null)
            mState.nextActionLong();
    }

    @Override
    public void init() {
        if(mState != null)
            mState.init();
    }
}
