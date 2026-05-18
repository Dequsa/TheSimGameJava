package items;

import BaseClasses.Organism;
import GUI.TextPrinter;

import java.io.Serializable;

public abstract class Item implements Serializable {
    int duration = 0;
    public Item(int duration) {
        this.duration = duration;
    }

    public abstract void applyEffect(Organism o);
    public abstract void decayEffect(Organism o);

    public int getDuration() { return duration; }
}
