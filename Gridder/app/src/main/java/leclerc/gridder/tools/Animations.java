package leclerc.gridder.tools;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.R;
import leclerc.gridder.activities.grids.InterestGridAdapter;
import leclerc.gridder.cards.CardEdit;
import leclerc.gridder.cards.InterestCard;
import leclerc.gridder.cards.card.states.CardFrontState;
import leclerc.gridder.cards.edit.states.EditClosedState;
import leclerc.gridder.cards.edit.states.EditDefaultState;
import leclerc.gridder.data.Interest;
import leclerc.gridder.data.User;

/**
 * Created by Antoine on 2015-04-14.
 */
public abstract class Animations {

    public static CardEdit CARD_EDIT;
    public static void setCardEdit(CardEdit cardEditView) {
        CARD_EDIT = cardEditView;
        bFirstExpansion = true;
    }

    private static float AlphaTarget = 1.0f;
    private final static float ALPHA_FADE_OUT_ON_FLIP = 0.5f;
    private final static float ALPHA_DEFAULT_FADE_OUT = 0.2f;
    private final static float ALPHA_DEFAULT_FADE_IN = 1.0f;
    public static AnimatorSet getFadeOutOtherCardsAnimation(int currentIndex, boolean isOnFlip) {
        AlphaTarget = isOnFlip ? ALPHA_FADE_OUT_ON_FLIP : ALPHA_DEFAULT_FADE_OUT;
        return getFadeCardsAnimation(currentIndex);
    }

    public static AnimatorSet getFadeInOtherCardsAnimation(int currentIndex) {
        AlphaTarget = ALPHA_DEFAULT_FADE_IN;
        return getFadeCardsAnimation(currentIndex);
    }

    private static AnimatorSet getFadeCardsAnimation(int currentIndex) {
        InterestGridAdapter adapter = User.getInstance().getCurrentGrid().getAdapter();

        List<ObjectAnimator> anims = new ArrayList<>();
        for(int i = 0; i < adapter.getCount(); i++) {
            if(i != currentIndex) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(adapter.getItem(i).getCard(), "alpha", AlphaTarget);
                anim.setInterpolator(new AccelerateDecelerateInterpolator());
                anim.setDuration(250);
                anims.add(anim);
            }
        }

        // Fade out animation
        AnimatorSet set = new AnimatorSet();

        int modifiedSize = adapter.getCount() - 1;
        for(int i = 0; i < modifiedSize; i++) {
            if(i < modifiedSize - 1) {
                set.play(anims.get(i)).with(anims.get(i + 1));
            }
            else if(i == modifiedSize - 1) {
                set.play(anims.get(i));
            }
        }

        return set;
    }

    public static AnimatorSet getFadeInAnimation(Context context, View target) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(target, "alpha", ALPHA_DEFAULT_FADE_IN));

        final View t = target;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                t.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return set;
    }

    public static AnimatorSet getFadeOutAnimation(View target) {
        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(target, "alpha", 0.35f));

        final View t = target;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                t.setEnabled(false);
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

        return set;
    }

    private static boolean bFirstExpansion = true;
    public static AnimatorSet getExpansionAnimation(final Context context, final int currentIndex) {
        AnimatorSet fakeCardIn = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.card_fake_edit_in);

        View v = CARD_EDIT;

        Point size = new Point();

        // Need to get the display size because of the incorrect measured size by the view
        if(bFirstExpansion) {
            ((Activity) context).getWindow().getWindowManager().getDefaultDisplay().getSize(size);
            size.y -= (int)context.getResources().getDimension(R.dimen.margin_top_bottom_grid);
        }

        // Left Column
        if(currentIndex == 0 || currentIndex == 3 || currentIndex == 6) {
            v.setPivotX(0);
        }
        // Right Column
        else if(currentIndex == 2 || currentIndex == 5 || currentIndex == 8) {
            if(bFirstExpansion)
                v.setPivotX(size.x);
            else
                v.setPivotX(v.getWidth());
        }
        // Middle Column
        else {
            if(bFirstExpansion)
                v.setPivotX(size.x / 2);
            else
                v.setPivotX(v.getWidth() / 2);
        }

        // First Row
        if(currentIndex == 0 || currentIndex == 1 || currentIndex == 2) {
            v.setPivotY(0);
        }
        // Third Row
        else if(currentIndex == 6 || currentIndex == 7 || currentIndex == 8) {
            if(bFirstExpansion)
                v.setPivotY(size.y);
            else
                v.setPivotY(v.getHeight());
        }
        // Second Row
        else {
            if(bFirstExpansion)
                v.setPivotY((size.y) / 2);
            else
                v.setPivotY(v.getHeight() / 2);
        }

        if(bFirstExpansion)
            bFirstExpansion = false;

        fakeCardIn.setTarget(v);

        fakeCardIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                CARD_EDIT.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ((View)(CARD_EDIT.getParent().getParent())).findViewById(R.id.grids_gridInterests).setVisibility(View.GONE);
                CARD_EDIT.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return fakeCardIn;
    }

    public static AnimatorSet getReductionAnimation(final Context context) {
        AnimatorSet fakeCardOut = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.card_fake_edit_out);
        fakeCardOut.setTarget(CARD_EDIT);

        fakeCardOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ((View)(CARD_EDIT.getParent().getParent())).findViewById(R.id.grids_gridInterests).setVisibility(View.VISIBLE);
                CARD_EDIT.setClickable(false);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                CARD_EDIT.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return fakeCardOut;
    }

    public static AnimatorSet getGoToEditAnimation(final Context context, int currentIndex) {
        AnimatorSet set = new AnimatorSet();

        Interest interest = User.getInstance().getCurrentGrid().getAdapter().getItem(currentIndex);
        if(interest == null || interest.getCard() == null)
            return set;

        CARD_EDIT.init(interest);

        AnimatorSet fadeOut = Animations.getFadeOutOtherCardsAnimation(currentIndex, false);
        AnimatorSet pullUp = Animations.getPullOutAnimation(context, interest.getCard());
        AnimatorSet expansion = Animations.getExpansionAnimation(context, currentIndex);

        set.play(pullUp).after(fadeOut);
        set.play(expansion).after(200).with(pullUp);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //CARD_EDIT.onChangedState(CardEdit.EditState.Default);
                CARD_EDIT.setState(new EditDefaultState(CARD_EDIT));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return set;
    }

    public static AnimatorSet getGoToGridAnimation(final Context context, int currentIndex) {

        AnimatorSet set = new AnimatorSet();
        AnimatorSet reduction = getReductionAnimation(context);
        AnimatorSet fadeIn = getFadeInOtherCardsAnimation(currentIndex);

        final InterestCard card = User.getInstance().getCurrentGrid().getElementAt(currentIndex).getCard();
        AnimatorSet pushIn = Animations.getPushInAnimation(context, card);

        set.playTogether(pushIn, reduction);
        set.play(fadeIn).after(100).after(pushIn);

        //card.Front.updateImage();

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //CARD_EDIT.onChangedState(CardEdit.EditState.Closed);
                CARD_EDIT.setState(new EditClosedState(CARD_EDIT));
                card.setState(new CardFrontState(card));

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

        return set;
    }

    public static AnimatorSet getPullOutAnimation(Context context, View target) {
        AnimatorSet set = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.card_pull_out);
        set.setTarget(target);

        return set;
    }

    public static AnimatorSet getPushInAnimation(Context context, View target) {
        AnimatorSet set = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.card_push_in);
        set.setTarget(target);

        return set;
    }

    public static AnimatorSet scaleIn(Context context, View target) {
        AnimatorSet set = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.card_fake_edit_in);
        set.setTarget(target);

        final View t = target;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                t.setVisibility(View.VISIBLE);
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

        return set;
    }

    public static AnimatorSet scaleOut(Context context, View target) {
        AnimatorSet set = (AnimatorSet)AnimatorInflater.loadAnimator(context, R.animator.card_fake_edit_out);
        set.setTarget(target);

        final View t = target;
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                t.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return set;
    }

    public static AnimatorSet getEditCardFadeInButtonAnimation(final View target) {
        AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(target, "alpha", 1.0f));
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                target.setVisibility(View.VISIBLE);
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

        return set;
    }

    public static AnimatorSet getEditCardFadeOutButtonAnimation(final View target) {
        AnimatorSet set = new AnimatorSet();

        set.play(ObjectAnimator.ofFloat(target, "alpha", 0.0f));
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return set;
    }

    public static AnimatorSet getGoToGridsAnimation(final View target) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0.0f, 1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 1.0f);

        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(250);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                target.setVisibility(View.VISIBLE);
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

        return set;
    }

    public static AnimatorSet getGoToCurrentGridAnimation(final View target) {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 1.0f, 0.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 1.0f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 1.0f, 1.0f);

        set.playTogether(alpha, scaleX, scaleY);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setDuration(250);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return set;
    }
}
