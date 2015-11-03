package leclerc.gridder.data;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.graphics.Palette;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.R;
import leclerc.gridder.cards.InterestCard;


/**
 * Created by Antoine on 2015-04-12.
 */
public class Interest {
    private final int SUBTITLE_COLOR_DEFAULT = Color.parseColor(("#CF444444"));

    public interface ImageChangedListener {
        void onChanged(InterestState state, Bitmap old, Bitmap newBmp);
    }

    public interface InterestStateChangedListener {
        void onStateChanged(InterestState state);
    }

    public interface InterestDeletedListener {
        void onInterestDeleted();
    }

    public enum InterestState {
        NONE,
        Add,
        Custom,
        Color
    }

    private InterestState mState;
    public InterestState getState() {
        return mState;
    }

    public boolean isAddInterest() {
        return getState() == InterestState.Add;
    }

    // Change state of the interest
    public void changeState(InterestState state) {
        if(mState == null || !mState.equals(state)) {
            mState = state;

            onStateChanged(state);
        }
    }

    // Call the listener if any
    private void onStateChanged(InterestState state) {
        if(mStateChangedListener != null) {
            mStateChangedListener.onStateChanged(state);
        }
    }

    private InterestStateChangedListener mStateChangedListener;
    public void setStateChangedListener(InterestStateChangedListener listener) {
        mStateChangedListener = listener;
    }

    private String mCustomImageSrc;
    private Bitmap mCustomImage;
    // Get custom image of the interest
    public Bitmap getCustomImage() {
        return mCustomImage;
    }

    public String getCustomImageSrc() {
        return mCustomImageSrc;
    }
    public void setCustomImageSrc(String src) {
        mCustomImageSrc = src;
        if(mCustomImageSrc == null)
            setCustomImage(null);
    }

    // Set custom image for the interest
    public void setCustomImage(Bitmap bitmap) {
        if(getCustomImage() == null || !getCustomImage().equals(bitmap)) {
            Bitmap oldImage = mCustomImage;
            mCustomImage = bitmap;

            // Update Frame & Modify Button Base Color
            setBaseColors();

            for (ImageChangedListener listener : imageChangedListeners) {
                listener.onChanged(InterestState.Custom, oldImage, mCustomImage);
            }
        }
    }

    private int mFrameBaseColor = SUBTITLE_COLOR_DEFAULT;
    private int mModifyBaseColor = 0;
    private int mBodyTextColor = Color.WHITE;
    private int mTitleTextColor = Color.WHITE;

    public int getFrameBaseColor() { return mFrameBaseColor; }
    public int getModifyBaseColor() { return mModifyBaseColor; }
    public int getBodyTextColor() { return mBodyTextColor; }
    public int getTitleTextColor() { return mTitleTextColor; }

    @TargetApi(21)
    private void setBaseColors() {
        if(getCard() != null) {
            final int baseModifyColor = getCard().getResources().getColor(R.color.app_primary);

            if (getCustomImage() != null) {
                final int alpha = 255;

                Palette p = Palette.generate(getCustomImage());
                int color = p.getVibrantColor(SUBTITLE_COLOR_DEFAULT);

                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);

                mFrameBaseColor = Color.HSVToColor(alpha, hsv);
                color = color == SUBTITLE_COLOR_DEFAULT ? baseModifyColor : color;
                mModifyBaseColor = color;

                if(p.getVibrantSwatch() != null) {
                    mBodyTextColor = p.getVibrantSwatch().getBodyTextColor();
                    mTitleTextColor = p.getVibrantSwatch().getTitleTextColor();
                }
                else {
                    mBodyTextColor = Color.WHITE;
                    mTitleTextColor = Color.WHITE;
                }

            } else {
                mFrameBaseColor = SUBTITLE_COLOR_DEFAULT;
                mModifyBaseColor = baseModifyColor;
                mBodyTextColor = Color.WHITE;
                mTitleTextColor = Color.WHITE;
            }
        }
        else {
            mFrameBaseColor = SUBTITLE_COLOR_DEFAULT;
            mModifyBaseColor = 0;
            mBodyTextColor = Color.WHITE;
            mTitleTextColor = Color.WHITE;
        }
    }

    private int mColor;
    // Get background color of the interest
    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }

    public Interest() {
        mId = -1;
        changeState(InterestState.Add);
        mColor = Color.BLACK;
    }

    public Interest(long id, String name) {
        mId = id;
        setName(name);
        changeState(InterestState.Color);
    }

    // Init the interest (usually used when loading from database)
    public void init(boolean usingDefault, String customSrc, int color) {
        mCustomImageSrc = customSrc;
        mColor = color;

        loadCustomImage();

        if(usingDefault)
            setState(InterestState.Custom);
    }

    public void init(boolean usingDefault, Bitmap customImage, int color) {
        mCustomImage = customImage;
        mColor = color;

        if(usingDefault)
            setState(InterestState.Custom);
    }

    // Load custom image from initial source
    private void loadCustomImage() {
        if(mCustomImageSrc == null) {
            setState(InterestState.Color);
            return;
        }

        try {
            InterestImage.getImage(this, mCustomImageSrc, CARD_RES_WIDTH, CARD_RES_HEIGHT);
        }
        catch (FileNotFoundException e) {
            mCustomImageSrc = null;
            setState(InterestState.Color);
        }
    }

    public void setState(InterestState state) {
        //mState = state;
        changeState(state);
    }

    public void delete() {
        setState(InterestState.Add);
        setCustomImageSrc(null);
        setCustomImage(null);
        setColor(0);

        if(deletedListener != null)
            deletedListener.onInterestDeleted();
    }

    private InterestDeletedListener deletedListener;
    public void setOnDeletedListener(InterestDeletedListener listener) {
        deletedListener = listener;
    }

    // =======================================================================================
    // =======================================================================================
    // =======================================================================================

    public static final int CARD_RES_WIDTH = 512;
    public static final int CARD_RES_HEIGHT = 512;
    public static final long NEEDS_UPDATE_ID = -1;

    private long mId;
    private int mIndex;
    private String mName;

    private InterestCard mCard;
    private List<ImageChangedListener> imageChangedListeners = new ArrayList<>();

    public final long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public void setIndex(int index) {
        mIndex = index;
    }

    public final int getIndex() {
        return mIndex;
    }

    public String getHashtag() {
        return getState() == InterestState.Add ? getName() : "#" + getName();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setCard(InterestCard card) {
        if(mCard != null && mCard.equals(card))
            return;

        mCard = card;

        if(mCard != null) {
            mCard.updateData(this);
        }
    }

    public InterestCard getCard() {
        return mCard;
    }

    public void addChangedListener(ImageChangedListener listener) {
        if(!imageChangedListeners.contains(listener))
            imageChangedListeners.add(listener);
    }

    // ============================================
    //  IMAGE
    // ============================================
    public static class InterestImage {
        public final static int RESULT_LOAD_IMAGE = 1;

        public static void getImage(Interest interest, String path, final int desiredWidth, final int desiredHeight) throws FileNotFoundException {
            System.gc();

            new AsyncTask<Object, Void, Object[]>() {
                @Override
                protected Object[] doInBackground(Object... params) {
                    BitmapFactory.Options o = new BitmapFactory.Options();
                    o.inJustDecodeBounds = true;

                    BitmapFactory.decodeFile((String)params[1], o);

                    BitmapFactory.Options o2 = new BitmapFactory.Options();
                    o2.inSampleSize = Math.max(o.outWidth / desiredWidth, o.outHeight / desiredHeight);

                    ((Interest)params[0]).setCustomImageSrc((String)params[1]);

                    return new Object[] { params[0], BitmapFactory.decodeFile((String)params[1], o2) };
                }

                @Override
                protected void onPostExecute(Object[] objects) {
                    super.onPostExecute(objects);

                    ((Interest)objects[0]).setCustomImage((Bitmap) objects[1]);
                }
            }.execute(interest, path);
        }
    }
}
