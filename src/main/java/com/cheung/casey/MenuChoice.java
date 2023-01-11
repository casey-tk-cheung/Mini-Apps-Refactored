package com.cheung.casey;

public enum MenuChoice {
    KEEP_COUNTING (1),
    SQUARE_ROOT (2),
    CHECK_DIGIT_GENERATOR (3),
    CHECK_DIGIT_CHECKER (4),
    QUIT (9),
    INVALID (-1);

    private int optionNumber;

    MenuChoice (int optionNumber) {
        this.optionNumber = optionNumber;
    }

    // method for converting int to menu choice
    public static MenuChoice fromInt (int choiceNum) {
        for (MenuChoice choice:values()) { // loops through the values in enum
            if (choice.optionNumber == choiceNum) { // checks if current option number matches the user input
                return choice;
            }
        }
        return INVALID;
    }
}