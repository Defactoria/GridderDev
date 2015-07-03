package leclerc.gridder.tools;

import android.app.ActionBar;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-05-16.
 */
public abstract class AdapterFactory {
    public static Integer[] Colors = new Integer[] {
            0xFFF44236,
            0xFFE91D62,
            0xFF000000,
            0xFF9C26B0,
            0xFF6739B6,
            0xFF3E50B4,
            0xFF2095F2,
            0xFF02A8F4,
            0xFF01BBD4,
            0xFF019587,
            0xFF4BAF4F,
            0xFF8BC24A,
            0xFFCCDB38,
            0xFFFFE93B,
            0xFFFEC107,
            0xFFFF9700,
            0xFFFF5521,
            0xFF795549,
            0xFF9D9D9D,
            0xFF607C8A
    };

    private static ColorAdapter colorAdapter = null;
    public static ColorAdapter getCustomColors(Context context) {
        if(colorAdapter == null)
            colorAdapter = new ColorAdapter(context, Colors);
        return colorAdapter;
    }

    public static class ColorAdapter extends ArrayAdapter<Integer> {
        private Map<Integer, RelativeLayout> dictionary = new HashMap<>();
        private int lastPosition = -1;

        public ColorAdapter(Context context, Integer[] colors) {
            super(context, 0, colors);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            RelativeLayout view = null;

            if(convertView != null) {
                view = (RelativeLayout)convertView;
            }
            else {
                view = dictionary.get(getItem(position));
                view.setLayoutParams(new ActionBar.LayoutParams(((GridView) parent).getColumnWidth(), ((GridView) parent).getColumnWidth()));
            }

            return view;
        }

        @Override
        public int getPosition(Integer item) {
            return super.getPosition(item);
        }

        public void updateSelection(int position) {
            if(lastPosition == position || position == -1)
                return;

            RelativeLayout layoutCurrentPosition = dictionary.get(getItem(position));
            if(layoutCurrentPosition != null) {
                View tickCurrent = layoutCurrentPosition.findViewById(R.id.fragment_color_tick);

                if (lastPosition != -1) {
                    RelativeLayout layoutLastPosition = dictionary.get(getItem(lastPosition));
                    View tickLast = layoutLastPosition.findViewById(R.id.fragment_color_tick);

                    tickLast.setVisibility(View.GONE);
                    tickCurrent.setVisibility(View.VISIBLE);
                } else {
                    tickCurrent.setVisibility(View.VISIBLE);
                }

                lastPosition = position;
            }
        }

        public void updateColor(int color) {
            updateSelection(getPosition(color));
        }

        public void load(GridView parent) {
            if(dictionary.size() == getCount())
                return;

            for(int i = 0; i < getCount(); i++) {
                RelativeLayout view = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.fragment_color_choice, null, false);
                view.setLayoutParams(new ActionBar.LayoutParams(parent.getColumnWidth(), parent.getColumnWidth()));

                View tick = view.findViewById(R.id.fragment_color_tick);
                tick.setVisibility(View.GONE);

                view.setBackgroundColor(getItem(i));

                final int position = i;
                Button btn = (Button) view.findViewById(R.id.fragment_color_button);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateSelection(position);
                    }
                });

                if (!dictionary.containsKey(getItem(i)))
                    dictionary.put(getItem(i), view);
            }
        }

        public int getCurrentColor() {
            if(lastPosition != -1)
                return getItem(lastPosition);
            else
                return 0;
        }
    }
}
