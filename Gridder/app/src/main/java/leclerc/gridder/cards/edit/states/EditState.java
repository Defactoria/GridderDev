package leclerc.gridder.cards.edit.states;

import leclerc.gridder.activities.grids.GridsActivity;

/**
 * Created by Antoine on 2015-05-17.
 */
public interface EditState {
    void init();
    void validate();
    void cancel();
    void extra();
    void show(GridsActivity gridsActivity);
}
