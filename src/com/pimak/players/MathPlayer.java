package com.pimak.players;

import com.pimak.fairGame.CoinAction;
import com.pimak.fairGame.Player;

public class MathPlayer implements Player {

    @Override
    public CoinAction whatsNext(int score, int flips, int heads, int tails) {
        State state = State.getInstance(heads, heads+tails);
        if (flips == 0){
            return state.whatToDoNextNoMoreFlips();
        }
        return state.whatToDoNext();
    }
}
