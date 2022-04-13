package com.pimak.fairGame;

import java.util.Random;

public class CheatingBlob implements Blob{

    private final Random random;

    public CheatingBlob(){
        this.random = new Random();
    }

    @Override
    public CoinSide flip() {
        if (this.random.nextInt(4) < 3){
            return CoinSide.HEAD;
        }
        return CoinSide.TAIL;
    }
    @Override
    public boolean guess(Type type) {
        return type == Type.CHEATING;
    }
}
