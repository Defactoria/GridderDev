package leclerc.gridder.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leclerc.gridder.R;
import leclerc.gridder.activities.chat.ChatActivity;
import leclerc.gridder.activities.grids.Grid;
import leclerc.gridder.activities.grids.GridsActivity;
import leclerc.gridder.cards.InterestCard;

/**
 * Created by Antoine on 2015-05-01.
 */
public class GridsLoader {
    private static Context currentContext;
    private static GridderDatabase database;

    private static final String VIEW_CARDS = "view_get_cards_from_users";
    private static final String VIEW_GRIDS = "view_get_grids_from_users";

    private static final String[] GRIDS_COLUMNS = new String[] {
            "GridId",
            "GridName"
    };

    private static final String[] CARDS_COLUMNS = new String[] {
            "GridId",
            "CardId",
            "GridIndex",
            "UseDefault",
            "CustomSrc",
            "Hashtag",
            "Color"
    };

    private static final String[] HASHTAG_COLUMNS = new String[] {
            "Id",
            "Name",
            "DisplayName"
    };

    public static void init(Context context) {
        currentContext = context;
        GridsActivity.PROGRESS_BAR.start();
        try {
            database = new GridderDatabase(currentContext);
        }
        catch (IOException e) {
            e.printStackTrace();
            GridsActivity.PROGRESS_BAR.stop();
        }
    }

    public static void insert(Grid grid, Interest interest) {
        //new TaskInsertCard().execute(grid, interest);
    }

    public static void remove(final InterestCard card) {

    }

    public static void update(final Interest interest) {
        new TaskUpdateCard().execute(interest);
    }

    private static int getGridIndexById(Grid[] grids, int id) {
        for(int i = 0; i < grids.length; i++) {
            if(grids[i].getGridId() == id) {
                return i;
            }
        }
        return -1;
    }

    public static class TaskInitGrids extends AsyncTask<View, Void, Grid[]> {
        private Grid[] grids = new Grid[1];
        private View initialView;

        @Override
        protected Grid[] doInBackground(View... params) {

            if(database != null && database.Database != null) {

                // ==========================================
                //  GET GRIDS
                // ==========================================
                Cursor c = database.Database.query(VIEW_GRIDS, GRIDS_COLUMNS, "Id=?", new String[] { String.valueOf(User.getInstance().getId()) }, null, null, null);
                c.moveToFirst();

                // Init array
                grids = new Grid[c.getCount()];

                // Create grids
                for (int i = 0; i < c.getCount(); i++) {
                    int gridId = c.getInt(c.getColumnIndex(GRIDS_COLUMNS[0]));
                    String gridName = c.getString(c.getColumnIndex(GRIDS_COLUMNS[1]));

                    Grid g = new Grid(currentContext, gridId, gridName);
                    grids[i] = g;

                    c.moveToNext();
                }
                c.close();

                // ==========================================
                //  GET CARDS
                // ==========================================
                /*c = database.Database.query(VIEW_CARDS, CARDS_COLUMNS, "UserId=" + String.valueOf(USER_ID), null, null, null, null);
                c.moveToFirst();

                // SET CARDS TO GRIDS
                for (int i = 0; i < c.getCount(); i++) {
                    int gridId = c.getInt(c.getColumnIndex(CARDS_COLUMNS[0]));

                    int index = getGridIndexById(grids, gridId);

                    if (index != -1 && grids[index] != null) {
                        int cardId = c.getInt(c.getColumnIndex(CARDS_COLUMNS[1]));
                        int gridIndex = c.getInt(c.getColumnIndex(CARDS_COLUMNS[2]));
                        boolean useDefault = c.getInt(c.getColumnIndex(CARDS_COLUMNS[3])) != 0;
                        String customSrc = c.getString(c.getColumnIndex(CARDS_COLUMNS[4]));
                        String hashtag = c.getString(c.getColumnIndex(CARDS_COLUMNS[5]));
                        int color = c.getInt(c.getColumnIndex(CARDS_COLUMNS[6]));

                        Interest interest = new Interest(cardId, hashtag);
                        interest.init(useDefault, customSrc, color);

                        grids[index].setElementAt(gridIndex, interest);
                    }

                    c.moveToNext();
                }
                c.close();*/

                // Init adapters for each grid
                for (Grid g : grids) {
                    g.init();
                }

                initialView = params[0];
            }

            return grids;
        }

        @Override
        protected void onPreExecute() {
            GridsActivity.PROGRESS_BAR.start();
            try {
                database = new GridderDatabase(currentContext);
            }
            catch (IOException e) {
                e.printStackTrace();
                GridsActivity.PROGRESS_BAR.stop();
            }
        }

        @Override
        protected void onPostExecute(Grid[] grids) {
            super.onPostExecute(grids);

            if(initialView == null)
                return;

            final GridView grid = (GridView)initialView.findViewById(R.id.grids_gridInterests);

            if(grids.length > 0) {
                //grid.setAdapter(grids[0].getAdapter());
                //grid.deferNotifyDataSetChanged();

                //TODO: CHECK
                /*TextView textView = (TextView)initialView.findViewById(R.id.grids_txtGridName);
                textView.setText(grids[0].getName());*/
            }

            for(Grid g : grids) {
                User.getInstance().addGrid(g);
            }

            ///////////////
            // LOAD CARDS
            ///////////////
            new AsyncTask<Grid, Void, Void>() {
                @Override
                protected Void doInBackground(Grid... grids) {
                    // ==========================================
                    //  GET CARDS
                    // ==========================================
                    Cursor c = database.Database.query(VIEW_CARDS, CARDS_COLUMNS, "UserId=?", new String[] { String.valueOf(User.getInstance().getId()) }, null, null, null);
                    c.moveToFirst();

                    // SET CARDS TO GRIDS
                    for (int i = 0; i < c.getCount(); i++) {
                        int gridId = c.getInt(c.getColumnIndex(CARDS_COLUMNS[0]));

                        int index = getGridIndexById(grids, gridId);

                        if (index != -1 && grids[index] != null) {
                            int cardId = c.getInt(c.getColumnIndex(CARDS_COLUMNS[1]));
                            int gridIndex = c.getInt(c.getColumnIndex(CARDS_COLUMNS[2]));
                            boolean useDefault = c.getInt(c.getColumnIndex(CARDS_COLUMNS[3])) != 0;
                            String customSrc = c.getString(c.getColumnIndex(CARDS_COLUMNS[4]));
                            String hashtag = c.getString(c.getColumnIndex(CARDS_COLUMNS[5]));
                            int color = c.getInt(c.getColumnIndex(CARDS_COLUMNS[6]));

                            Interest interest = new Interest(cardId, hashtag);
                            interest.init(useDefault, customSrc, color);

                            grids[index].setElementAt(gridIndex, interest);
                        }

                        c.moveToNext();
                    }
                    c.close();

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Grid g = User.getInstance().getCurrentGrid();
                    if(g != null) {
                        g.init();

                        if (grid != null) {
                            grid.setAdapter(g.getAdapter());
                        }
                    }

                    for(Grid uGrid : User.getInstance().getGrids()) {
                        if(!uGrid.equals(g)) {
                            ((GridsActivity)currentContext).addPreview(uGrid);
                        }
                    }

                    GridsActivity.PROGRESS_BAR.stop();
                }
            }.execute(User.getInstance().getGrids());
        }
    }

    static class TaskUpdateCard extends AsyncTask<Interest, Void, Void> {

        @Override
        protected Void doInBackground(Interest... params) {
            Interest interest = params[0];
            boolean isNewCard = interest.getId() == Interest.NEEDS_UPDATE_ID;

            // GetHashtag
            long hashtagId = -1;

            // GET HASHTAG
            {
                Cursor hashtagCursor = database.Database.query("Hashtag", HASHTAG_COLUMNS, "Name=?", new String[] { interest.getName().toLowerCase() }, null, null, null);
                if (hashtagCursor.getCount() > 0) {
                    hashtagCursor.moveToFirst();
                    hashtagId = hashtagCursor.getInt(hashtagCursor.getColumnIndex(HASHTAG_COLUMNS[0]));
                }
                hashtagCursor.close();
            }
            // END GET HASHTAG

            // INSERT NEW HASHTAG IF APPLICABLE
            {
                boolean isNewHashtag = hashtagId == -1;
                if (isNewHashtag) {
                    ContentValues insertHashtagValues = new ContentValues();
                    insertHashtagValues.put("Name", interest.getName().toLowerCase());
                    insertHashtagValues.put("DisplayName", interest.getName());

                    hashtagId = database.Database.insert("Hashtag", null, insertHashtagValues);
                }
            }
            // END INSERT NEW HASHTAG


            // INSERT OR UPDATE CARD
            {
                ContentValues cardValues = new ContentValues();
                cardValues.put("GridId", User.getInstance().getCurrentGrid().getGridId());
                cardValues.put("HashtagId", hashtagId);
                cardValues.put("GridIndex", interest.getIndex());
                cardValues.put("UseDefaultImage", interest.getState() == Interest.InterestState.Custom ? 1 : 0);
                cardValues.put("ImageSrc", interest.getCustomImageSrc());
                cardValues.put("Color", interest.getColor());

                if (isNewCard) {
                    interest.setId(database.Database.insert("Card", null, cardValues));
                }
                else {
                    database.Database.updateWithOnConflict("Card", cardValues, "Id=?", new String[] { String.valueOf(interest.getId()) }, SQLiteDatabase.CONFLICT_REPLACE);
                }
            }
            // END INSERT OR UPDATE CARD

            //TODO: Add Category

            return null;
        }
    }

    public static class TaskFindUserId extends AsyncTask<String, Void, ContentValues> {
        @Override
        protected ContentValues doInBackground(String... params) {

            //Cursor c = database.Database.query("User", null, "DeviceId=?", new String[] { params[0] }, null, null, null);
            Cursor c = database.Database.query("User", null, "Id=?", new String[] { String.valueOf(User.getInstance().getId()) }, null, null, null);
            c.moveToFirst();

            Long userId;
            String username = "";
            if(c.getCount() > 0) {
                userId = c.getLong(c.getColumnIndex("Id"));
                username = c.getString(c.getColumnIndex("Username"));
            }
            else {
                ContentValues values = new ContentValues();
                // TODO: Change new user infos
                username = "TEST_USER";
                values.put("Username", username);
                values.put("Password", "test_password");
                values.put("DeviceId", params[0]);
                userId = database.Database.insert("User", null, values);
            }
            c.close();

            ContentValues userValues = new ContentValues();
            userValues.put("Id", userId);
            userValues.put("Username", username);

            if(params.length > 1)
                userValues.put("LoadGrid", Boolean.valueOf(params[1]));
            else
                userValues.put("LoadGrid", false);

            return userValues;
        }

        @Override
        protected void onPostExecute(ContentValues values) {
            super.onPostExecute(values);

            User.getInstance().setId((Long) values.get("Id"));
            User.getInstance().setUsername((String) values.get("Username"));

            if((boolean)values.get("LoadGrid")) {
                new GridsLoader.TaskInitGrids().execute(GridsActivity.GridsRoot);
            }
        }
    }

    public static class TaskFindPersonChat extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            String[] whereArgs = new String[] {
                    String.valueOf(User.getInstance().getId()),
                    String.valueOf(User.getInstance().getCurrentGrid().getGridId()),
                    String.valueOf(User.getInstance().getId())
            };

            Cursor c = database.Database.query("view_get_commons", null, "SearcherId=? AND SearcherGridId=? AND OtherId<>?", whereArgs, null, null, null, "90");
            c.moveToFirst();

            Map<String, Integer> data = new HashMap<>();
            Map<String, Long> dataGrid = new HashMap<>();

            String id = "";
            for(int i = 0; i < c.getCount(); i++) {
                id = c.getString(c.getColumnIndex("OtherDeviceId"));

                if(!data.containsKey(id)) {
                    data.put(id, 1);
                    dataGrid.put(id, c.getLong(c.getColumnIndex("OtherGridId")));
                }
                else {
                    int count = data.get(id) + 1;
                    data.remove(id);
                    data.put(id, count);
                }

                c.moveToNext();
            }

            c.close();

            int max = 0;
            int maxIndex = -1;
            Integer[] values = new Integer[0];

            values = data.values().toArray(values);
            for(int i = 0; i < values.length; i++) {
                if(values[i] > max) {
                    max = values[i];
                    maxIndex = i;
                }
            }

            if(maxIndex == -1)
                return null;

            String[] userIds = new String[0];
            userIds = data.keySet().toArray(userIds);

            Long[] gridIds = new Long[0];
            gridIds = dataGrid.values().toArray(gridIds);

            User.getInstance().setOtherUserId(userIds[maxIndex]);
            User.getInstance().setOtherUserGridId(gridIds[maxIndex]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            String id = User.getInstance().getOtherUserId();
            Long gridId = User.getInstance().getOtherUserGridId();
            new TaskFindCommons().execute(id, String.valueOf(gridId));
        }
    }

    public static class TaskFindCommons extends AsyncTask<String, Void, Interest[]> {
        @Override
        protected Interest[] doInBackground(String... params) {

            String[] columns = new String[] {
                    "SearcherGridId",
                    "SearcherCardId"
            };

            String[] whereArgs = new String[] {
                    String.valueOf(User.getInstance().getCurrentGrid().getGridId()),
                    String.valueOf(User.getInstance().getId()),
                    params[0], // Device Id of other user
                    params[1] // Id of other user's grid
            };

            //Cursor c = database.Database.query("view_get_commons", columns, "SearcherGridId=? AND SearcherId=? AND OtherDeviceId=? AND OtherGridId=?", whereArgs, null, null, null);
            Cursor c = database.Database.query("view_get_commons", columns, " OtherGridId=? AND SearcherId=? AND SearcherGridId=?", new String[] { String.valueOf(1), String.valueOf(User.getInstance().getId()), String.valueOf(User.getInstance().getCurrentGrid().getGridId()) }, null, null, null);
            c.moveToFirst();

            List<Long> cardIds = new ArrayList<>();
            for(int i = 0; i < c.getCount(); i++) {
                cardIds.add(c.getLong(c.getColumnIndex(columns[1])));

                c.moveToNext();
            }
            c.close();

            Long[] idsArray = new Long[cardIds.size()];
            for(int i = 0; i < idsArray.length; i++) {
                idsArray[i] = cardIds.get(i);
            }

            return User.getInstance().getCurrentGrid().getInterests(idsArray);
        }

        @Override
        protected void onPostExecute(Interest[] interests) {
            ChatActivity.chatHeader.setCommonInterests(interests);

            super.onPostExecute(interests);
        }
    }
}
