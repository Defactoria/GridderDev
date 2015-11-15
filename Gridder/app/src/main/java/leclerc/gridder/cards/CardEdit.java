package leclerc.gridder.cards;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Random;

import leclerc.gridder.activities.grids.Grid;
import leclerc.gridder.activities.grids.GridAdapter;
import leclerc.gridder.cards.edit.states.EditClosedState;
import leclerc.gridder.cards.edit.states.EditOptionsState;
import leclerc.gridder.cards.edit.states.EditPictureState;
import leclerc.gridder.cards.edit.states.EditState;
import leclerc.gridder.cards.edit.states.EditStateContext;
import leclerc.gridder.data.User;
import leclerc.gridder.tools.AdapterFactory;
import leclerc.gridder.tools.Animations;
import leclerc.gridder.data.Interest;
import leclerc.gridder.R;

/**
 * Created by Antoine on 2015-05-11.
 */
public class CardEdit extends FrameLayout implements UpdateCard {

    public interface EditChangedStateListener {
        void onChangedState(EditState state);
    }
    private EditChangedStateListener changedStateListener = null;

    private Interest mInterestData;

    private ImageView imgInterest;
    private TextView editHashtag;

    private Bitmap tmpCustomImage = null;
    private int tmpColor = Color.BLACK;

    private Interest.InterestState tmpState;
    private EditStateContext mCurrentState;
    private FrameLayout mOptionsView;
    private FrameLayout mPictureView;
    private RelativeLayout mLayout;

    private View viewEditCard;
    private View viewGrids;

    private FloatingActionButton btnFloatingModify;

    public CardEdit(Context context) {
        super(context);
    }

    public CardEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardEdit(Context context, AttributeSet attrs, int id) {
        super(context, attrs, id);
    }

    public Interest getInterest() {
        return mInterestData;
    }

    public void setChangedStateListener(EditChangedStateListener listener) {
        changedStateListener = listener;
    }

    // On changed state of the edit card. Has been opened or closed.
    public void onChangedState(EditState state) {
        if(changedStateListener == null)
            return;

        changedStateListener.onChangedState(state);
    }

    public void setState(EditState state) {
        if(mCurrentState != null)
            mCurrentState.setState(state);
    }

    public EditState getCurrentState() {
        if(mCurrentState == null)
            return null;
        return mCurrentState.getState();
    }

    public FrameLayout getOptionsView() {
        return mOptionsView;
    }

    public FrameLayout getPictureView() {
        return mPictureView;
    }

    public RelativeLayout getLayout() {
        return mLayout;
    }

    public void init(Interest interestData) {
        viewEditCard.setVisibility(View.VISIBLE);
        viewGrids.setVisibility(View.GONE);

        mInterestData = interestData;

        tmpState = getInterest().getState();
        tmpCustomImage = getInterest().getCustomImage();

        if(tmpState == Interest.InterestState.Add)
            tmpState = Interest.InterestState.Color;

        /*if(getInterest().getModifyBaseColor() == 0){
            btnFloatingModify.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.app_primary)));
        }
        else {
            btnFloatingModify.setBackgroundTintList(ColorStateList.valueOf(getInterest().getModifyBaseColor()));
        }*/

        update();
    }

    GridView gridList;
    GridAdapter gridAdapter;
    public void init(Grid[] grids) {
        viewEditCard.setVisibility(View.GONE);
        viewGrids.setVisibility(View.VISIBLE);

        gridAdapter = new GridAdapter(getContext(), grids);
        gridList = ((GridView)viewGrids.findViewById(R.id.card_grids_grid));
        gridList.setAdapter(gridAdapter);

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectGrid(position);
            }
        });
    }

    public final int getSelectGridIndex() {
        return gridAdapter.getCurrentIndex();
    }

    public void selectGrid(int currentIndex) {
        if(gridAdapter != null) {
            GridAdapter.GridElement el = gridAdapter.getElement(currentIndex);

            el.doAction();
            /*if(!el.canSelect()) {
                el.doAction();
                return;
            }

            for (int i = 0; i < gridAdapter.getCount(); i++)
                if (!gridAdapter.getElement(i).equals(el))
                    gridAdapter.getElement(i).deselect();

            el.select();*/
        }
    }

    public void updateGridList() {
        gridAdapter = new GridAdapter(getContext(), User.getInstance().getGrids());
        gridList.setAdapter(gridAdapter);
    }

    public void setDialogImage(Bitmap bmp) {
        tmpCustomImage = bmp;
        tmpState = Interest.InterestState.Custom;

        if(getCurrentState() != null)
            getCurrentState().init();
    }

    public void createView(LayoutInflater inflater) {
        final View v = inflater.inflate(R.layout.preview_card_edit, this, true);

        viewEditCard = v.findViewById(R.id.card_edit_view);
        viewGrids = v.findViewById(R.id.card_edit_grids_view);

        btnChangeImage = (ImageButton)v.findViewById(R.id.preview_card_edit_changeImageButton);
        btnChangeColors = (ImageButton)v.findViewById(R.id.preview_card_edit_editColors);
        imgInterest = (ImageView)v.findViewById(R.id.preview_card_edit_image);
        editHashtag = (TextView)v.findViewById(R.id.preview_card_edit_interest);

        // OPTIONS
        final View options = inflater.inflate(R.layout.fragment_card_options_color_categories, null, false);
        GridView gridView = (GridView)options.findViewById(R.id.card_edit_options_colorsGrid);
        gridView.setAdapter(AdapterFactory.getCustomColors(getContext()));
        gridView.deferNotifyDataSetChanged();

        AdapterFactory.getCustomColors(getContext()).load(gridView);

        Spinner spinner = (Spinner)options.findViewById(R.id.card_options_category);
        // TODO: Get every categories
        SpinnerAdapter adapter = new TestSpinnerCategoriesAdapter(getContext(), new String[] { "TV Series", "Book", "Movie" });
        spinner.setAdapter(adapter);
        spinner.setSelection(0);

        mOptionsView = (FrameLayout)v.findViewById(R.id.card_edit_options);
        mOptionsView.setVisibility(GONE);
        mOptionsView.addView(options);
        // END OPTIONS

        // PICTURE

        final View picture = inflater.inflate(R.layout.fragment_card_image, null, false);
        mPictureView = (FrameLayout)v.findViewById(R.id.card_edit_picture_dialog);
        mPictureView.setVisibility(GONE);
        mPictureView.addView(picture);

        // END PICTURE
        final CardEdit instance = this;

        /*btnFloatingModify = (FloatingActionButton)v.findViewById(R.id.card_edit_floating_modify);
        btnFloatingModify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) instance.getContext(), v, "VIEW_IMAGE");
                Bundle b = options.toBundle();

                ActivityCompat.startActivity((Activity) instance.getContext(), new Intent(instance.getContext(), LoginActivity.class), options.toBundle());
            }
        });*/

        mLayout = (RelativeLayout)v.findViewById(R.id.card_edit_layout);
        btnChangeColors.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet animation = new AnimatorSet();

                AnimatorSet set = Animations.scaleIn(getContext(), getOptionsView());
                if(set != null) {
                    set.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            getLayout().setEnabled(false);
                            setState(new EditOptionsState(instance));
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }

                AnimatorSet fade = Animations.getFadeOutAnimation(getLayout());

                animation.playTogether(set, fade);
                animation.start();
            }
        });

        btnChangeImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimatorSet animation = new AnimatorSet();

                AnimatorSet set = Animations.scaleIn(getContext(), getPictureView());
                if(set != null) {
                    set.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            getLayout().setEnabled(false);
                            setState(new EditPictureState(instance));
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }

                AnimatorSet fade = Animations.getFadeOutAnimation(getLayout());

                animation.playTogether(set, fade);
                animation.start();
            }
        });

        mCurrentState = new EditClosedState(this);
    }

    private class TestSpinnerCategoriesAdapter extends ArrayAdapter<String> implements SpinnerAdapter {

        public TestSpinnerCategoriesAdapter(Context context, String[] strings) {
            super(context, 0, strings);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;

            if(convertView != null) {
                textView = (TextView)convertView;
            }
            else {
                textView = new TextView(getContext());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                textView.setTextSize(24);
            }

            textView.setText(getItem(position));

            return textView;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getDropDownView(position, convertView, parent);
        }
    }

    public void setIsUsingCustom(boolean usesCustom, Bitmap bmp) {
        if(usesCustom) {
            tmpState = Interest.InterestState.Custom;
            tmpCustomImage = bmp;
        }
        else {
            tmpState = Interest.InterestState.Color;
        }

        updateImage();
    }

    public void setIsUsingColor(boolean usesColor, int color) {
        if(usesColor) {
            tmpState = Interest.InterestState.Color;
        }
        else {
            setIsUsingCustom(true, tmpCustomImage);
        }

        tmpColor = color == 0 ? tmpColor : color;

        updateImage();
    }

    @Override
    public void update() {
        if(getInterest() != null) {
            getInterest().addChangedListener(new Interest.ImageChangedListener() {
                @Override
                public void onChanged(Interest.InterestState state, Bitmap old, Bitmap bmp) {
                    if (state == Interest.InterestState.Custom) {
                        tmpCustomImage = old;
                        setDialogImage(bmp);
                    }
                }
            });

            editHashtag.setText(getInterest().getState() == Interest.InterestState.Add ?
                    "" : getInterest().getName());

            if(getInterest().getState() == Interest.InterestState.Add) {
                tmpColor = getRandomColor();
            }
            else {
                tmpColor = getInterest().getColor();
            }

            updateImage();
        }
    }

    public void validate() {
        if(getCurrentState() != null) {
            getCurrentState().validate();
        }
    }

    public void cancel() {
        if(getCurrentState() != null) {
            getCurrentState().cancel();
        }
    }

    private void updateImage() {
        if(tmpState != null) {
            if(tmpState == Interest.InterestState.Custom) {
                imgInterest.setImageBitmap(tmpCustomImage);
                imgInterest.setBackgroundColor(tmpColor);
            }
            else if(tmpState == Interest.InterestState.Add) {
                imgInterest.setImageBitmap(null);
                imgInterest.setBackgroundColor(tmpColor);
            }
            else {
                imgInterest.setImageBitmap(null);
                imgInterest.setBackgroundColor(tmpColor);
            }

        }
    }

    private int getRandomColor() {
        int r = new Random().nextInt(AdapterFactory.Colors.length - 1);

        return AdapterFactory.Colors[r];
    }

    public void resetTmpValues() {
        tmpCustomImage = null;
        tmpColor = Color.BLACK;
        tmpState = Interest.InterestState.Add;
    }

    public String getText() {
        return editHashtag.getText().toString();
    }

    public TextView getEditHashtag() {
        return editHashtag;
    }

    private ImageButton btnChangeImage;
    private ImageButton btnChangeColors;

    public ImageButton getLeftButton() {
        return btnChangeImage;
    }

    public ImageButton getRightButton() {
        return btnChangeColors;
    }

    public Bitmap getTmpCustomImage() {
        return tmpCustomImage;
    }

    public int getTmpColor() {
        return tmpColor;
    }

    public Interest.InterestState getTmpInterestState() {
        return tmpState;
    }

}
