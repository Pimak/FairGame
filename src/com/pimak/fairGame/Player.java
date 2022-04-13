package com.pimak.fairGame;

public interface Player {
    CoinAction whatsNext(int score, int flips, int heads, int tails);
}
