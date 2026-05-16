package Structs;

import java.awt.Color;
import java.io.Serializable;

public record OrganismData (Types type, int str, int init, int moveSpeed, Color color) implements Serializable {
    private static final long serialVersionUID = 1L;
}
