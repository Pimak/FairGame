package com.pimak.fairGame;

public class Game {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    private int flips;
    private int score;
    private Blob blob;
    private final Player player;
    private int heads;
    private int tails;
    private final Boolean talkative;

    public Game(int flips, int score, Player player, Boolean talkative) {
        this.flips = flips;
        this.score = score;
        this.blob = Blob.getInstance();
        this.player = player;
        this.heads = 0;
        this.tails = 0;
        this.talkative = talkative;
    }

    public Game(Player player, Boolean talkative) {
        this(100,0, player, talkative);
    }

    public Game(Player player){
        this(player,true);
    }

    public void playOnce() throws NoMoreFlipException {
        switch (this.player.whatsNext(score, flips, heads, tails)){
            case THROW_AGAIN -> {
                if (this.flips == 0){
                    throw new NoMoreFlipException();
                }
                CoinSide coinSide = blob.flip();
                this.flips -= 1;
                if (coinSide == CoinSide.HEAD){
                    this.heads += 1;
                } else {
                    this.tails +=1;
                }
            }
            case GUESS_CHEATING -> guess(Blob.Type.CHEATING);
            case GUESS_FAIR -> guess(Blob.Type.FAIR);
        }
    }

    private void guess(Blob.Type type){
        if (blob.guess(type)){
            if (talkative){
                System.out.println(ANSI_GREEN+"Guessed right : "+ type+" <-- "+heads+"-"+tails+ANSI_RESET);
            }
            this.score += 1;
            this.flips += 15;
        } else {
            if (talkative){
                System.out.println(ANSI_RED+"Guessed wrong : "+ type+" <-- "+heads+"-"+tails+ANSI_RESET);
            }
            this.flips -= 30;
        }
        blob = Blob.getInstance();
        this.heads = 0;
        this.tails = 0;
    }

    public int playFullGame(int maxScore){
        while (score<maxScore && flips>=0){
            try {
                playOnce();
            } catch (NoMoreFlipException e) {
                flips = -1;
            }
        }
        return score;
    }

}
