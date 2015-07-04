package leclerc.gridder.data;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.activities.grids.Grid;

public class User {
    private static User Instance;

    private boolean bIsPremium;
    private final List<Grid> grids = new ArrayList<Grid>();
    private int gridIndex;
    private String mUsername;
    private String mRegId;
    private long mId;
    private String mOtherUserId;
    private Long mOtherUserGridId;

    private User() {
        setIsPremium(true);
        setUsername("TEST_USER");
        setId(1);
    }

    public static User getInstance() {
        if(Instance == null)
            Instance = new User();
        return Instance;
    }

    public boolean isPremium() {
        return bIsPremium;
    }

    public void setIsPremium(boolean isPremium) {
        bIsPremium = isPremium;
    }

    public final String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public void setRegId(String regId) {
        mRegId = regId;
    }

    public final String getRegId() {
        return mRegId;
    }

    public void setId(long id) {
        mId = id;
    }

    public final long getId() {
        return mId;
    }

    public void setOtherUserId(String id) {
        mOtherUserId = id;
    }

    public final String getOtherUserId() {
        return mOtherUserId;
    }

    public void setOtherUserGridId(Long gridId) {
        mOtherUserGridId = gridId;
    }

    public final Long getOtherUserGridId() {
        return mOtherUserGridId;
    }

    // =====================================================================
    //  GRID
    // =====================================================================

    public Grid getNextGrid() {
        int index = (gridIndex + 1) % grids.size();

        return grids.get(index);
    }

    public Grid getPrevGrid() {
        int index = (gridIndex - 1 + grids.size()) % grids.size();

        return grids.get(index);
    }

    public Grid getCurrentGrid() {
        if(grids.size() == 0)
            return null;
        return grids.get(gridIndex);
    }

    public Grid getGrid(int index) {
        if(index < 0 || index >= grids.size())
            return null;

        return grids.get(index);
    }

    public Grid[] getGrids() {
        Grid[] g = new Grid[getGridsCount()];
        grids.toArray(g);
        return g;
    }

    public final int getGridsCount() {
        return grids.size();
    }

    public final int getCurrentGridIndex() {
        return gridIndex;
    }

    public void addGrid(Grid g) {
        grids.add(g);
    }

    public final Grid getGridById(int id) {
        for(Grid g : grids) {
            if(g.getId() == id) {
                return g;
            }
        }
        return null;
    }

    // =====================================================================
    //  END GRID
    // =====================================================================
}
