package leclerc.gridder.tools;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.widget.ImageButton;

import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-11-11.
 */
@TargetApi(21)
public class AddingGridButton extends ImageButton {
    private RippleDrawable backgroundDrawable;
    private Drawable foregroundDrawable;

    public AddingGridButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        backgroundDrawable = new RippleDrawable(ColorStateList.valueOf(Color.WHITE), context.getDrawable(R.drawable.circle), null);
        foregroundDrawable = context.getDrawable(R.drawable.ic_action_add);
        setBackground(backgroundDrawable);
        setBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.app_status)));
        setImageDrawable(foregroundDrawable);
    }

    public void setColors(int color) {
        int opposite = Color.rgb(255 - Color.red(color),
                                 255 - Color.green(color),
                                 255 - Color.blue(color));

        backgroundDrawable = new RippleDrawable(ColorStateList.valueOf(color), getContext().getDrawable(R.drawable.circle), null);
        foregroundDrawable = getContext().getDrawable(R.drawable.ic_action_add);

        setBackground(backgroundDrawable);
        setBackgroundTintList(ColorStateList.valueOf(opposite));
        setImageDrawable(foregroundDrawable);
    }
}
