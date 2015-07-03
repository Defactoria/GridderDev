package leclerc.gridder.activities.grids;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import leclerc.gridder.data.Interest;
import leclerc.gridder.cards.InterestCard;

/**
 * Created by Antoine on 2015-04-07.
 */
public class InterestGridAdapter extends ArrayAdapter<Interest> {
    private static final String TAG = InterestGridAdapter.class.getSimpleName();
    private Map<Integer, Interest> dictionary = new HashMap<>();

    public InterestGridAdapter(Context context, Interest[] interests) {
        super(context, 0, new ArrayList<Interest>());

        addAll(interests);

        for(int i = 0; i < interests.length; i++) {
            dictionary.put(i, interests[i]);
        }
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        InterestCard card;

        if(convertView == null) {
            card = null;

            Interest i = dictionary.get(position);
            if(i != null) {
                card = i.getCard();
            }

            if(card == null) {
                getItem(position).setIndex(position);
                card = new InterestCard(parent.getContext());
                int width = ((GridView) parent).getColumnWidth();
                card.setDimension(width, width);
                card.updateData(getItem(position));
                card.init();
            }
            else {
                card.updateData(getItem(position));
                dictionary.remove(position);
                dictionary.put(position, card.getInterest());
            }
        }
        else {
            card = (InterestCard)convertView;
            card.updateData(getItem(position));
        }

        return card;
    }
}
