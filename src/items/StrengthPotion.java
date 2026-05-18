package items;

import BaseClasses.Organism;
import GUI.TextPrinter;

public class StrengthPotion extends Item{
    final int STR_INCREASE;
    final int PER_TURN_DECREASE = 1;

    public StrengthPotion(int strIncrease, int duration) {
        super(duration);
        STR_INCREASE = strIncrease;
    }

    @Override
    public void applyEffect(Organism o) {
        o.setStr(o.getData().str() + STR_INCREASE);
//        printer.print("Strength Potion used: +" + STR_INCREASE);
        System.out.println("Strength Potion used: +" + STR_INCREASE);
    }

    @Override
    public void decayEffect(Organism o) {
        if (duration <= 0) return;

        o.setStr(o.getData().str() - PER_TURN_DECREASE);
        System.out.println("Strength Potion is decaying: -" + PER_TURN_DECREASE);
        duration--;
    }

    @Override
    public String toString() {
        return "+" + STR_INCREASE + " POTION STR";
    }
}
