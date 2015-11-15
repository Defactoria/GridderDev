package leclerc.gridder.activities.edition;

import android.app.ActionBar;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.R;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.Tools;

/**
 * Created by Antoine on 2015-11-02.
 */
public class CategoryInfoView extends View {
    private List<String> categoriesList;

    private LinearLayout rootView;
    private View headerView;

    public CategoryInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);

        categoriesList = new ArrayList<>();

        View v = LayoutInflater.from(context).inflate(R.layout.edition_categories_header, null, true);

        rootView = (LinearLayout)v.getRootView();
        headerView = rootView.findViewById(R.id.edition_categories_header_frame);
    }

    public void updateInfos(Interest interest) {
        /*rootView.removeViews(1, categoriesList.size());
        categoriesList.clear();

        String[] categories = interest.getCategories();
        for(int i = 0; i < categories.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setText(categories[i]);
            textView.setTextSize(Tools.getSpValue(getContext(), 18));

            int padding = (int)Tools.getDpValue(getContext(), 10);
            textView.setPadding(padding, padding, padding, padding);


        }*/
    }
}
