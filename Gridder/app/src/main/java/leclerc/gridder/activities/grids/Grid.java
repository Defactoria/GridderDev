package leclerc.gridder.activities.grids;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.R;
import leclerc.gridder.cards.InterestCard;
import leclerc.gridder.cards.edit.states.EditClosedState;
import leclerc.gridder.data.Interest;
import leclerc.gridder.data.User;
import leclerc.gridder.tools.ILoading;

public class Grid extends View implements ILoading {
    private Interest[] elements = new Interest[9];

    private long Id;
    private String Name;
    private Bitmap GridPreview;

    private InterestGridAdapter mAdapter;
    private View mPreviewView;

    public Grid(Context context, long id, String name) {
        super(context);

        Id = id;
        Name = name;
        elements = new Interest[9];

        for(int i = 0; i < elements.length; i++) {
            elements[i] = new Interest();
            elements[i].setName(getResources().getString(R.string.interest_default_hashtag));
        }
    }

    public final long getGridId() {
        return Id;
    }

    public final String getName() {
        return Name;
    }

    public void createPreview(Bitmap bitmap) {
        GridPreview = bitmap;
    }

    public void setElementAt(int index, Interest element) {
        replaceElement(elements[index], element);
    }

    public boolean addGridElement(Interest element) {
        for(int i = 0; i < elements.length; i++) {
            if(getElementAt(i) != null) {
                elements[i] = element;
                return true;
            }
        }
        return false;
    }

    public void removeGridElementAt(int index) {
        if(index < 0 || index >= elements.length)
            return;

        elements[index] = null;
    }

    public Interest getElementAt(int index) {
        if(index < 0 || index >= elements.length)
            return null;

        return elements[index];
    }

    public int getElementIndex(Interest interest) {
        for(int i = 0; i < elements.length; i++)
        {
            if(elements[i].equals(interest)) {
                return i;
            }
        }

        return -1;
    }

    public Interest[] getElements() {
        if(elements == null || elements.length == 0) {
            if(getAdapter() != null) {
                int count = getAdapter().getCount();
                elements = new Interest[count];
                for(int i = 0; i < count; i++) {
                    elements[i] = getAdapter().getItem(i);
                }
            }
        }
        return elements;
    }

    public Interest[] getElementsBut(Interest interest) {
        List<Interest> list = new ArrayList<>();
        for(Interest i : elements) {
            list.add(i);
        }

        list.remove(getElementIndex(interest));

        return (Interest[])list.toArray();
    }

    public void replaceElement(Interest oldInterest, Interest newInterest) {
        int index = getElementIndex(oldInterest);

        if(index != -1) {
            elements[index] = newInterest;

            if(getAdapter() != null)
                getAdapter().remove(oldInterest);

            oldInterest.setIndex((int)Interest.NEEDS_UPDATE_ID);
            newInterest.setIndex(index);
            newInterest.setCard(oldInterest.getCard());

            if(getAdapter() != null) {
                getAdapter().insert(newInterest, index);
                getAdapter().notifyDataSetInvalidated();
                oldInterest.delete();
            }
        }
    }

    public void init() {
        mAdapter = new InterestGridAdapter(getContext(), elements);
        mAdapter.setLoadingParent(this);
    }

    public final InterestGridAdapter getAdapter() {
        return mAdapter;
    }

    public Interest[] getInterests(Long[] ids) {

        List<Interest> cards = new ArrayList<>();
        Interest c;
        for(int i = 0; i < getAdapter().getCount(); i++) {
            c = getAdapter().getItem(i);

            for(long id : ids) {
                if(c.getId() == id) {
                    cards.add(c);
                    break;
                }
            }
        }

        Interest[] interests = new Interest[cards.size()];
        for(int i = 0; i < interests.length; i++) {
            interests[i] = cards.get(i);
        }

        return interests;
    }

    public void setPreviewView(View v) {
        mPreviewView = v;
    }

    public View getPreviewView() {
        return mPreviewView;
    }

    public void setPreview(Bitmap preview) {
        GridPreview = preview;
    }

    public Bitmap getPreview() {
        return GridPreview;
    }

    @Override
    public void onLoaded(Object caller) {

        // IF HERE, IT IS ON THE SCREEN

        GridsActivity activity = (GridsActivity) getContext();

        Bitmap bmp = activity.loadPreview(this);

        if(bmp != null) {
            setPreview(bmp);
        }

        activity.removePreview(this);

        if (User.getInstance().getCurrentGrid().equals(this))
            activity.updateState();

        System.out.println(String.format("Grid \"%s\" Loaded", getName()));
        //setPreview(activity.loadPreview(this));
        //activity.removePreview(this);


    }
}
