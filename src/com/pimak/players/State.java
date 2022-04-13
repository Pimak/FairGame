package com.pimak.players;

import com.pimak.fairGame.CoinAction;

import java.util.HashMap;

public class State {
    private final int heads;
    private final int throwCount;
    private double fairKnowingThis = -1;
    private double cheatingKnowingThis = -1;
    private double fairGainKnowingThis = -1;
    private double cheatGainKnowingThis = -1;

    private final static HashMap<String,State> states = new HashMap<>();

    private State(int heads, int throwCount) {
        this.heads = heads;
        this.throwCount = throwCount;
    }

    public static State getInstance(int heads, int throwCount){
        String key = heads+","+throwCount;
        if (states.containsKey(key)){
            return states.get(key);
        }
        State state = new State(heads,throwCount);
        states.put(key,state);
        return state;
    }

    public int getHeads() {
        return heads;
    }

    public int getThrowCount() {
        return throwCount;
    }

    public double getFairKnowingThis() {
        if (fairKnowingThis == -1){
            fairKnowingThis = Math.pow(2,throwCount)/
                    (Math.pow(2,throwCount) + Math.pow(3,heads));
        }
        return fairKnowingThis;
    }

    public double getCheatingKnowingThis() {
        if (cheatingKnowingThis == -1){
            cheatingKnowingThis = Math.pow(3,heads)/
                    (Math.pow(2,throwCount) + Math.pow(3,heads));
        }
        return cheatingKnowingThis;
    }

    public double getFairGainKnowingThis(){
        if (fairGainKnowingThis == -1){
            fairGainKnowingThis = (15*Math.pow(2,throwCount) - 30*Math.pow(3,heads))/
                    (Math.pow(2,throwCount) + Math.pow(3,heads))
                    - throwCount;
        }
        return fairGainKnowingThis;
    }

    public double getCheatGainKnowingThis(){
        if (cheatGainKnowingThis == -1){
            cheatGainKnowingThis = (-30*Math.pow(2,throwCount) + 15*Math.pow(3,heads))/
                    (Math.pow(2,throwCount) + Math.pow(3,heads))
                    - throwCount;
        }
        return cheatGainKnowingThis;
    }

    public double getMaxGainKnowingThis(){
        return Math.max(getCheatGainKnowingThis(),getFairGainKnowingThis());
    }

    public double getHeadKnowingThis(){
        return getFairKnowingThis()/2 + 3*getCheatingKnowingThis()/4;
    }
    public double getTailKnowingThis(){
        return getFairKnowingThis()/2 + getCheatingKnowingThis()/4;
    }

    public CoinAction whatToDoNext(){

        if (getMaxGainKnowingThis() < 0 && throwCount < 6){
            return CoinAction.THROW_AGAIN;
        }

        if (getMaxGainKnowingThis() < getNextFlipMaxGain()){
            return CoinAction.THROW_AGAIN;
        }
        if (getMaxGainKnowingThis() < getNMoreFlipMaxGain(2)){
            return CoinAction.THROW_AGAIN;
        }
        if (getMaxGainKnowingThis() < getNMoreFlipMaxGain(3)){
            return CoinAction.THROW_AGAIN;
        }
        if (getCheatGainKnowingThis()>getFairGainKnowingThis()){
            return CoinAction.GUESS_CHEATING;
        }
        return CoinAction.GUESS_FAIR;
    }

    public double getNextFlipMaxGain(){
        State headState = State.getInstance(heads + 1, throwCount + 1);
        State tailState = State.getInstance(heads, throwCount + 1);
        return headState.getMaxGainKnowingThis()*getHeadKnowingThis()
                + tailState.getMaxGainKnowingThis()*getTailKnowingThis();
    }

    public double getNMoreFlipMaxGain(int n){
        if (n<=0){
            return getMaxGainKnowingThis();
        }
        if (n == 1){
            return getNextFlipMaxGain();
        }
        State headState = State.getInstance(heads + 1, throwCount + 1);
        State tailState = State.getInstance(heads, throwCount + 1);
        return headState.getNMoreFlipMaxGain(n-1)*getHeadKnowingThis()
                + tailState.getNMoreFlipMaxGain(n-1)*getTailKnowingThis();
    }

    public CoinAction whatToDoNextNoMoreFlips() {
        if (getCheatGainKnowingThis()>getFairGainKnowingThis()){
            return CoinAction.GUESS_CHEATING;
        }
        return CoinAction.GUESS_FAIR;
    }

    public State throwHead(){
        return getInstance(getHeads()+1, getThrowCount()+1);
    }
    public State throwTail(){
        return getInstance(getHeads(), getThrowCount()+1);
    }

    public String toString(){
        return heads + " - " + (throwCount - heads);
    }

    public static void main(String[] args) {
        System.out.println(State.getInstance(24,36).getCheatingKnowingThis());
    }

}
