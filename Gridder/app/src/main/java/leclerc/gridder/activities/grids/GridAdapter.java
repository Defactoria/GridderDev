package leclerc.gridder.activities.grids;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import leclerc.gridder.R;
import leclerc.gridder.data.GridderDatabase;
import leclerc.gridder.data.User;

/**
 * Created by Antoine on 2015-07-03.
 */
public class GridAdapter extends ArrayAdapter<Grid> {
    private Map<Integer, GridElement> elements = new HashMap<>();

    public GridAdapter(Context context, Grid[] objects) {
        super(context, 0, objects);

        for(int i = 0; i < objects.length; i++) {
            elements.put(i, new GridElement(context, objects[i]));
        }

        elements.put(objects.length, new AddGridElement(context));
    }

    public GridElement getElement(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        if (position < getCount() - 1)
            return getItem(position).getGridId();
        return -1;
    }

    @Override
    public int getCount() {
        return elements.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout v = elements.get(position);

        if (position < getCount() - 1)
            elements.get(position).setData(getItem(position).getName(), ((GridView) parent).getColumnWidth());
        else
            elements.get(position).setData(null, ((GridView)parent).getColumnWidth());

        return v;
    }

    public final int getCurrentIndex() {
        int index = -1;

        for(int i = 0; i <elements.size(); i++) {
            if(elements.get(i).isSelected()) {
                index = i;
                break;
            }
        }

        return index;
    }

    public static class GridElement extends RelativeLayout {
        protected TextView txtGridName;
        protected ImageView frmGridImage;
        protected RelativeLayout border;
        protected ImageView imgAdd;
        protected Grid grid;

        public GridElement(Context context, Grid parentGrid) {
            super(context);
            grid = parentGrid;

            LayoutInflater.from(context).inflate(R.layout.grids_element, this, true);

            createView();

            grid = parentGrid;
            if(parentGrid != null)
                setImage(parentGrid.getPreview());
        }

        public void createView() {
            txtGridName = (TextView)findViewById(R.id.grids_element_text);
            frmGridImage = (ImageView)findViewById(R.id.grids_element_grid);
            border = (RelativeLayout)findViewById(R.id.grids_element_border);
            imgAdd = (ImageView)findViewById(R.id.grids_element_add);

            frmGridImage.setVisibility(VISIBLE);
            imgAdd.setVisibility(GONE);
            imgAdd.setColorFilter(Color.WHITE);

            deselect();
        }

        public void setData(String text, int width) {
            txtGridName.setText(text);

            frmGridImage.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
        }

        public void setImage(Bitmap preview) {
            frmGridImage.setBackground(new BitmapDrawable(getContext().getResources(), preview));
        }

        public boolean canSelect() {
            return true;
        }

        public void select() {
            border.setVisibility(VISIBLE);
        }

        public void deselect() {
            border.setVisibility(GONE);
        }

        public boolean isSelected() {
            return border.getVisibility() == VISIBLE;
        }

        public Grid getGrid() {
            return grid;
        }

        public void doAction() {
            GridsActivity activity = (GridsActivity)getContext();
            User.getInstance().changeGrid(grid);
            activity.getState().validate();
        }
    }

    public static class AddGridElement extends GridElement {

        public AddGridElement(Context context) {
            super(context, null);
        }

        @Override
        public void createView() {
            super.createView();

            border.setVisibility(GONE);
            frmGridImage.setVisibility(GONE);
            imgAdd.setVisibility(VISIBLE);
            imgAdd.setColorFilter(Color.parseColor("#FF0091EA"));
        }

        @Override
        public void setData(String text, int width) {
            super.setData("Add Grid", width);
        }

        @Override
        public boolean canSelect() {
            return false;
        }

        @Override
        public void select() {

        }

        @Override
        public void deselect() {

        }

        @Override
        public boolean isSelected() {
            return false;
        }

        @Override
        public void doAction() {
            new AsyncTask<Void, Void, Grid>() {
                GridderDatabase database;

                @Override
                protected Grid doInBackground(Void... params) {
                    if(database == null)
                        return null;

                    ContentValues values = new ContentValues();
                    values.put("UserId", User.getInstance().getId());
                    values.put("Name", "NewGrid");

                    long id = database.Database.insert("Grid", null, values);
                    database.close();

                    if(id == -1)
                        return null;

                    return new Grid(getContext(), id, values.getAsString("Name"));
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    try {
                        database = new GridderDatabase(getContext());
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                protected void onPostExecute(Grid grid) {
                    super.onPostExecute(grid);

                    if(grid != null) {
                        grid.init();
                        User.getInstance().addGrid(grid);
                        ((GridsActivity)getContext()).updateGridList();
                    }
                }
            }.execute();
        }
    }
}