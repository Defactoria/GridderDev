package leclerc.gridder.cards.edit.states;

import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;

/**
 * Created by Antoine on 2015-05-17.
 */
public class EditStateContext implements EditState {
    private EditState mState;
    private CardEdit mParent;

    public EditStateContext(CardEdit parent) {
        mParent = parent;
    }

    public CardEdit getParent() {
        return mParent;
    }

    public void setState(EditState state) {
        if(mState == null || (state != null && !mState.getClass().equals(state.getClass()))) {
            mState = state;

            EditStateContext tmpState = (EditStateContext)mState;
            if(tmpState != null && tmpState.getParent() != null) {
                tmpState.init();
                tmpState.getParent().onChangedState(mState);
            }
        }
    }

    public EditState getState() {
        return mState;
    }

    @Override
    public void init() {
        if(mState != null)
            mState.init();
    }

    @Override
    public void validate() {
        if(mState != null)
            mState.validate();
    }

    @Override
    public void cancel() {
        if(mState != null)
            mState.cancel();
    }

    @Override
    public void extra() {
        if(mState != null)
            mState.extra();
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        if(mState != null)
            mState.show(gridsActivity);
    }
}
