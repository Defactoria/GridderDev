package leclerc.zapper;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

import leclerc.gridder.R;

public class HeaderView extends Activity {

    private final int TITLE = R.string.app_name;
    private final int SIMPLE_TITLE = R.string.app_name_simple;
    private final int BACKGROUND = android.R.drawable.dark_header;

    private String Title;
    private Image LeftIcon;
    private Image RightIcon;
    private ELayoutType LayoutType;

    private FrameLayout HeaderFrame;
    private RelativeLayout ContentLayout;

    enum ELayoutType
    {
        TitleOnly,
        All
    }

    public HeaderView(ELayoutType layoutType) {
        LayoutType = layoutType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setLayoutType(LayoutType);
    }

    public FrameLayout getHeader() {
        return HeaderFrame;
    }

    public void setLeftIcon(Image img) {
        LeftIcon = img;
    }

    public Image getLeftIcon() {
        return LeftIcon;
    }

    public void setRightIcon(Image img) {
        RightIcon = img;
    }

    public Image getRightIcon() {
        return RightIcon;
    }

    public void setLayoutType(ELayoutType type) {
        LayoutType = type;

        if(LayoutType == ELayoutType.All) {
            FrameLayout root = new FrameLayout(getApplicationContext());

        }
        else if(LayoutType == ELayoutType.TitleOnly) {
            LinearLayout root = new LinearLayout(getApplicationContext());
            root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            root.setOrientation(LinearLayout.VERTICAL);

            FrameLayout HeaderFrame = new FrameLayout(getApplicationContext());

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                HeaderFrame.setTransitionName("Header");
            }

            HeaderFrame.setBackground(getResources().getDrawable(BACKGROUND));
            //HeaderFrame.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.activity_header_height)));

            TextView textView = new TextView(getBaseContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            textView.setText(getApplicationContext().getResources().getText(R.string.app_name));
            textView.setTextAppearance(getApplicationContext(), R.style.HeaderText);
            textView.setGravity(Gravity.CENTER);

            HeaderFrame.addView(textView);

            root.addView(HeaderFrame);

            ContentLayout = new RelativeLayout(getApplicationContext());

            root.addView(ContentLayout);

            setContentView(root);

            setTheme(R.style.AppTheme);
        }
    }

    public ELayoutType getLayoutType() {
        return LayoutType;
    }

    public void setContent(int id) {
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());

        inflater.inflate(id, ContentLayout);
    }
}
