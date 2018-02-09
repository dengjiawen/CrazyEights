package logic;

import java.util.ArrayList;

/**
 * Created by freddeng on 2018-02-05.
 */

public class Hand extends Deck {

    ArrayList<Boolean> isSelected;
    ArrayList<Boolean> isPlayable;

    public Hand () {
        super(false);

        isSelected = new ArrayList<>();
        isPlayable = new ArrayList<>();
    }

    void play (Card card) {
        remove (card);
    }

    public void fill () {
        super.fill();
        update();
    }

    boolean isEmpty () {
        return size() == 0;
    }

    public void update () {
        for (int i = 0; i < size(); i ++) {
            isSelected.add(new Boolean(false));
            isPlayable.add(new Boolean(false));
        }
    }

    public void select (int index) {
        if (!isSelected.get(index)) {
            isSelected.set(index, true);
        } else {
            isSelected.set(index, false);
        }
    }

    public boolean isSelected (int index) {
        return isSelected.get(index);
    }

    public boolean isPlayable (int index) {return isPlayable.get(index);}

}




