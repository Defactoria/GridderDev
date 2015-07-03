package leclerc.gridder.cards.card.states;

/**
 * Created by Antoine on 2015-05-24.
 */
public interface CardState {
    void backAction();
    void nextAction();
    void nextActionLong();

    /* On state changed */
    void init();
}
