package leclerc.gridder.activities.grids;

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

import java.util.HashMap;
import java.util.Map;

import leclerc.gridder.R;
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
    }

    public GridElement getElement(int position) {
        return elements.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout v = elements.get(position);

        elements.get(position).setData(getItem(position).getName(), ((GridView) parent).getColumnWidth());

        return v;
    }

    public static class GridElement extends RelativeLayout {
        private TextView txtGridName;
        private ImageView frmGridImage;
        private RelativeLayout border;
        private Grid grid;

        public GridElement(Context context, Grid parentGrid) {
            super(context);
            grid = parentGrid;

            LayoutInflater.from(context).inflate(R.layout.grids_element, this, true);

            createView();

            setImage(parentGrid.getPreview());
        }

        public void createView() {
            txtGridName = (TextView)findViewById(R.id.grids_element_text);
            frmGridImage = (ImageView)findViewById(R.id.grids_element_grid);
            border = (RelativeLayout)findViewById(R.id.grids_element_border);

        }

        public void setData(String text, int width) {
            txtGridName.setText(text);

            frmGridImage.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
        }

        public void setImage(Bitmap preview) {
            frmGridImage.setBackground(new BitmapDrawable(getContext().getResources(), preview));
        }

        public Grid getGrid() {
            return grid;
        }
    }
}