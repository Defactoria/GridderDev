package leclerc.zapper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesUtil;

import java.lang.reflect.Field;

import leclerc.gridder.R;

public class GridElement extends RelativeLayout {
    private int BackgroundColor = Color.WHITE;
    private Drawable BackgroundImageSrc;

    private String Hashtag;

    private TextView text;
    private FrameLayout frame; // A simple frame for making images darker

    public GridElement(Context context) {
        super(context); initViews();
    }

    public GridElement(Context context, String hashtag, int backgroundColor) {
        this(context, hashtag, backgroundColor, true);
    }

    public GridElement(Context context, String hashtag, int backgroundColor, boolean isHashtag) {
        super(context);

        if(isHashtag)
            setHashtag(hashtag);
        else
            setText(hashtag);

        BackgroundColor = backgroundColor;
        try {
            BackgroundImageSrc = getResources().getDrawable(getResources().getIdentifier("bg_interest_" + hashtag.toLowerCase(), "drawable", context.getPackageName()));
        }
        catch(Exception e) {
        }
    }

   public GridElement(Context context, AttributeSet attrs)
    {
        super(context, attrs, 0);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GridElement, 0, 0);

        try {

            setHashtag(a.getString(R.styleable.GridElement_hashtag));

            BackgroundColor = a.getColor(R.styleable.GridElement_backgroundColor, Color.GRAY);
            setPaletteColor(a.getDrawable(R.styleable.GridElement_backgroundImage));
        }
        finally {
            a.recycle();
        }
    }

    public int getBackgroundColor() {
        return BackgroundColor;
    }


    public Drawable getBackgroundSrc() {
        return BackgroundImageSrc;
    }

    // TODO: CHANGE THAT !!
    public boolean isEmpty() {
        return true;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setPaletteColor(Drawable drawable) {
        if(drawable != null) {
            Palette.generateAsync(convertToBitmap(drawable), new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    Palette.Swatch s = palette.getDarkVibrantSwatch();
                    if(s != null) {

                        //BackgroundColor = s.getRgb();
                        //BackgroundColor = palette.getVibrantColor(Color.RED);
                        //txtHashtag.setTextColor(palette.getDarkVibrantSwatch().getTitleTextColor());
                    }
                }
            });
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTextPalette(TextView textView) {
        if(textView != null) {

            Bitmap bmp;

            if(BackgroundImageSrc == null || !User.getInstance().isPremium()) {
                int[] colors = new int[350 * 350];
                for (int i = 0; i < colors.length; i++) {
                    colors[i] = BackgroundColor;
                }
                bmp = Bitmap.createBitmap(colors, 350, 350, Bitmap.Config.ARGB_8888);
            }
            else {
                bmp = convertToBitmap(BackgroundImageSrc);
            }

            Palette.Swatch s = Palette.generate(bmp).getLightVibrantSwatch();

            if(s != null) {
                textView.setTextColor(s.getTitleTextColor());
            }
        }
    }

    public void setHashtag(String hashtag) {
        Hashtag = "#" + hashtag;
    }

    public String getHashtag() {
        return Hashtag;
    }

    public void setText(String text) {
        Hashtag = text;
    }

    private Bitmap convertToBitmap(Drawable drawable) {
        Bitmap mutableBitmap = Bitmap.createBitmap(350, 350, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    private int getResId(String resName, Class<?> c) {
        try{
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        }
        catch (Exception e) {
            return -1;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(text == null)
            initViews();

        updateView();
    }
    private void initViews() {
        text = new TextView(getContext());
        text.setTextAppearance(getContext(), R.style.TextAppearance_AppCompat_Small);
        text.setTextColor(Color.WHITE);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        text.setGravity(Gravity.CENTER);
        text.setShadowLayer(5, 3, 3, Color.BLACK);
        text.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/calibri.ttf"));

        frame = new FrameLayout(getContext());
        frame.setBackgroundColor(Color.parseColor("#4F000000"));
        frame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        if(getHashtag().equals("Ajouter intérêt")) {
            BackgroundImageSrc = getResources().getDrawable(R.drawable.bg_interest_add);
            BackgroundColor = Color.parseColor("#00000000");
            BackgroundImageSrc.setColorFilter(Color.parseColor("#FF0091EA"), PorterDuff.Mode.MULTIPLY);
            text.setTextColor(Color.BLACK);
        }
    }

    private void updateView() {
        removeAllViews();
        setBackgroundColor(BackgroundColor);

        if(User.getInstance().isPremium() && BackgroundImageSrc != null) {
            setBackground(BackgroundImageSrc);
            addView(frame);
        }

        text.setText(getHashtag());
        //setTextPalette(text);

        addView(text);
    }
}
