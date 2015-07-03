package leclerc.gridder.cards.edit.states;

import android.opengl.Visibility;
import android.view.View;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.CardEdit;
import leclerc.zapper.User;

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
        // TODO: Show grids
    }

    @Override
    public void show(GridsActivity gridsActivity) {
        gridsActivity.setArrowsVisibility(View.VISIBLE);
        gridsActivity.setFooterUsesTick(false);
        gridsActivity.setHeaderInfos(User.getInstance().getCurrentGrid().getName(), true, R.drawable.ic_action_tiles_large, false);
    }
}
