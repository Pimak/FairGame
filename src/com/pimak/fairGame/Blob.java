package com.pimak.fairGame;

import java.util.Random;

public interface Blob {
    enum Type{
        CHEATING,
        FAIR
    }
    CoinSide flip();
    boolean guess(Type type);
    static Blob getInstance() {
        Random random = new Random();
        if (random.nextInt(2)==0){
            return new FairBlob();
        }
        return new CheatingBlob();
    }
}
