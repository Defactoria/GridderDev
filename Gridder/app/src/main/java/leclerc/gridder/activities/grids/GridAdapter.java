package leclerc.gridder.activities.grids;

import android.content.Context;
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
    private View rootView;
    private Map<Integer, Grid> dictionary = new HashMap<>();

    public GridAdapter(Context context, Grid[] objects) {
        super(context, 0, objects);

        rootView = LayoutInflater.from(context).inflate(R.layout.grids_element, null, false);

        for(int i = 0; i < objects.length; i++) {
            dictionary.put(i, objects[i]);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if(convertView == null) {
            v = rootView;
        }
        else {
            v = convertView;
        }



        int width = ((GridView)parent).getColumnWidth();
        v.setLayoutParams(new ViewGroup.LayoutParams(width, width));
        ((TextView)v.findViewById(R.id.grids_element_text)).setText(getItem(position).getName());
        // Set Image HERE (R.id.grids_element_grid)
        FrameLayout frameLayout = (FrameLayout)v.findViewById(R.id.grids_element_grid);

        return v;
    }
}
