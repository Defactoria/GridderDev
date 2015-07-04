package leclerc.gridder.activities.grids;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-07-03.
 */
public class GridAdapter extends ArrayAdapter<Grid> {
    private Map<Integer, GridElement> elements = new HashMap<>();

    public GridAdapter(Context context, Grid[] objects) {
        super(context, 0, objects);

        for(int i = 0; i < objects.length; i++) {
            elements.put(i, new GridElement(context, ((GridsActivity)context).getGridPreview()));
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout v = elements.get(position);

        elements.get(position).setData(getItem(position).getName(), ((GridView)parent).getColumnWidth());

        return v;
    }
}

class GridElement extends RelativeLayout {
    private TextView txtGridName;
    private FrameLayout frmGridImage;
    private Bitmap gridPreview;

    public GridElement(Context context, Bitmap preview) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.grids_element, this, true);
        gridPreview = preview;

        createView();
    }

    public void createView() {
        txtGridName = (TextView)findViewById(R.id.grids_element_text);
        frmGridImage = (FrameLayout)findViewById(R.id.grids_element_grid);
    }

    public void setData(String text, int width) {
        txtGridName.setText(text);

        frmGridImage.setLayoutParams(new RelativeLayout.LayoutParams(width, width));
        frmGridImage.setBackground(new BitmapDrawable(getContext().getResources(), gridPreview));
    }
}