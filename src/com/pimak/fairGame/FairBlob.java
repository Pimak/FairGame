package com.pimak.fairGame;

import java.util.Random;

public class FairBlob implements Blob {

    private final Random random;

    public FairBlob(){
        this.random = new Random();
    }

    @Override
    public CoinSide flip() {
        if (this.random.nextInt(2) < 1){
            return CoinSide.HEAD;
        }
        return CoinSide.TAIL;
    }
    @Override
    public boolean guess(Type type) {
        return type == Type.FAIR;
    }
}
