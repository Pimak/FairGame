package com.pimak;

import com.pimak.fairGame.Game;
import com.pimak.fairGame.Player;
import com.pimak.players.MathPlayer;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        Player player = new MathPlayer();
        Game game;
        int gameAmount = 100000;

        HashMap<Integer, Integer> scores = new HashMap<>();
        for (int i = 0; i < gameAmount; i++) {
            game = new Game(player, false);
            int score = game.playFullGame(1000000);
            if (!scores.containsKey(score)){
                scores.put(score,0);
            }
            scores.put(score,scores.get(score)+1);
        }

        int average = 0;
        for (Integer key : scores.keySet().stream().sorted().toList()){
            System.out.println(key + " : " + scores.get(key));
            average+= key*scores.get(key);
        }
        System.out.println("Average : " + (average/gameAmount));
    }
}
