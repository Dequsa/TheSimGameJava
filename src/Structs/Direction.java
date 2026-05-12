package Structs;

public enum Direction {
    UP_LEFT,    // index 0: (-speed, -speed)
    UP,         // index 1: (0, -speed)
    UP_RIGHT,   // index 2: (speed, -speed)
    LEFT,       // index 3: (-speed, 0)
    RIGHT,      // index 4: (speed, 0)
    DOWN_LEFT,  // index 5: (-speed, speed)
    DOWN,       // index 6: (0, speed)
    DOWN_RIGHT, // index 7: (speed, speed)
    NONE,       // index 8: (0, 0)
    SPECIAL;    // index 9: (No corresponding vector)
}
