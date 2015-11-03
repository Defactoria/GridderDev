package leclerc.gridder.tools;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Antoine on 2015-11-02.
 */
public class Tools {
    public static float getSpValue(Context c, float value) {
        return convertTo(TypedValue.COMPLEX_UNIT_SP, c, value);
    }

    public static float getDpValue(Context c, float value) {
        return convertTo(TypedValue.COMPLEX_UNIT_DIP, c, value);
    }

    private static float convertTo(int unit, Context c, float value) {
        return TypedValue.applyDimension(unit, value, c.getResources().getDisplayMetrics());
    }
}
