package leclerc.gridder.cards;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import leclerc.gridder.cards.card.states.CardFrontState;
import leclerc.gridder.cards.card.states.CardState;
import leclerc.gridder.cards.card.states.CardStateContext;
import leclerc.gridder.tools.Animations;
import leclerc.gridder.data.Interest;
import leclerc.gridder.tools.ILoading;

public class InterestCard extends FrameLayout implements ILoading {
    public static boolean IsAnimated = false;

    private interface OnCardFlippedHandler {
        void onCardFlipped(InterestCard card, boolean isShowingBack);
    }

    private static List<OnCardFlippedHandler> handlers = new ArrayList<>();
    private boolean mShowingBack;

    private int mImageSrc;

    private Interest InterestData;

    public CardFront Front;
    public CardBack Back;

    private CardStateContext mState;

    private final LayoutInflater inflater;

    public InterestCard(Context context) {
        super(context);

        inflater = LayoutInflater.from(context);
    }

    public void setDimension(int width, int height) {
        setLayoutParams(new ViewGroup.LayoutParams(width, height));
    }

    public Interest getInterest() {
        return InterestData;
    }

    private boolean isShowingBack() {
        return mShowingBack;
    }

    private void setIsShowingBack(boolean showingBack) {
        mShowingBack = showingBack;

        for(OnCardFlippedHandler handler : handlers) {
            handler.onCardFlipped(this, isShowingBack());
        }
    }

    public void setState(CardState state) {
        if(!IsAnimated)
            mState.setState(state);
    }

    public CardState getState() {
        if(mState != null)
        {
            return mState.getState();
        }

        return null;
    }

    /*public void onViewClicked(View v) {
        flipCard();
    }*/

    /*public void flipCard() {
        AnimatorSet flip;

        if(isShowingBack())
            flip = flipToFront();
        else
            flip = flipToBack();

        if(flip != null) {
            flip.start();
        }
    }*/

    public AnimatorSet flipToFront() {
        if(IsAnimated)
            return null;

        AnimatorSet set = new AnimatorSet();
        set.playTogether(Back.getOutAnimation(), Front.getInAnimation(), Animations.getFadeInOtherCardsAnimation(getInterest().getIndex()));
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Back.setEnabled(false);
                Front.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO: VERIFY SET ENABLED = SET BUTTON ENABLE ?
                Front.setEnabled(true);
                setIsShowingBack(false);
                Back.setVisibility(GONE);
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

    public AnimatorSet flipToBack() {
        if(IsAnimated)
            return null;

        AnimatorSet set = new AnimatorSet();
        set.playTogether(Front.getOutAnimation(), Back.getInAnimation(), Animations.getFadeOutOtherCardsAnimation(getInterest().getIndex(), true));

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                Front.setEnabled(false);
                Back.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Back.setEnabled(true);
                setIsShowingBack(true);
                Front.setVisibility(GONE);
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

    public void init(ILoading parent) {
        if(InterestData == null)
            return;

        removeAllViews();

        Back = new CardBack(getContext());
        Back.createView(inflater);
        //Back.init(InterestData);
        addView(Back);

        Front = new CardFront(getContext());
        Front.setLoadingParent(this);
        Front.createView(inflater);
        //Front.init(InterestData);
        addView(Front);

        if(Front != null) {
            OnClickListener onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onFrontClick(v);
                }
            };
            OnLongClickListener onLongClickListener = new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) { return onFrontLongClick();  }
            };

            Front.initButton(onClickListener, onLongClickListener);
        }

        if(Back != null) {
            OnClickListener onClick = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackClick();
                }
            };

            Back.initButton(onClick);
        }

        // Needs to be after initializing Front and Back cards
        mState = new CardFrontState(this);

        loadingParent = parent;

        updateData(InterestData);
    }

    private void onFrontClick(View v) {
        if(getState() != null)
            getState().nextAction();
    }

    private boolean onFrontLongClick() {
        if(getState() != null)
            getState().nextActionLong();
        return true;
    }

    private void onBackClick() {
        if(getState() != null)
            getState().nextAction();
    }

    public void updateData(Interest data) {
        InterestData = data;
        InterestData.setCard(this);

        InterestData.setOnDeletedListener(new Interest.InterestDeletedListener() {
            @Override
            public void onInterestDeleted() {
                Front.update();
                Back.update();
            }
        });

        if(Front != null)
            Front.init(InterestData);

        if(Back != null)
            Back.init(InterestData);
    }

    private ILoading loadingParent;
    @Override
    public void onLoaded() {
        System.out.println(String.format("InterestCard \"%s\" Loaded", InterestData.getName()));

        if(loadingParent != null)
            loadingParent.onLoaded();
    }
}
