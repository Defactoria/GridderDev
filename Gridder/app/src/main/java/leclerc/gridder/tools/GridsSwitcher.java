package leclerc.gridder.tools;

import leclerc.gridder.activities.grids.Grid;
import leclerc.gridder.data.User;

/**
 * Created by Antoine on 2015-05-01.
 */
public class GridsSwitcher {

    private Grid PreviousGrid;
    private Grid CurrentGrid;
    private Grid NextGrid;

    public Grid getPreviousGrid() {
        return PreviousGrid;
    }

    public void setPreviousGrid(Grid previousGrid) {
        PreviousGrid = previousGrid;
    }

    public Grid getCurrentGrid() {
        return CurrentGrid;
    }

    public void setCurrentGrid(Grid currentGrid) {
        CurrentGrid = currentGrid;
    }

    public Grid getNextGrid() {
        return NextGrid;
    }

    public void setNextGrid(Grid nextGrid) {
        NextGrid = nextGrid;
    }

    private User mUser;
    public void setContext() {
        mUser = User.getInstance();
    }

    public void switchToNext() {
        /*AnimatorSet currentToLeft = Animations.getGridOutFromRightAnimation();
        currentToLeft.setTarget(getCurrentGrid());

        AnimatorSet nextToCurrent = Animations.getGridInFromRightAnimation();
        nextToCurrent.setTarget(getNextGrid());

        AnimatorSet anim = new AnimatorSet();
        anim.play(currentToLeft).with(nextToCurrent);

        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Grid current = getCurrentGrid();
                setPreviousGrid(current);
                setCurrentGrid(getNextGrid());
                mUser.setCurrentGrid(getCurrentGrid());
                setNextGrid(mUser.getNextGrid());
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });*/
    }
}
