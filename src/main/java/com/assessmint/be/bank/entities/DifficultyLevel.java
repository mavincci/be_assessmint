package com.assessmint.be.bank.entities;

import com.assessmint.be.global.exceptions.EnumIllegalArgumentException;

import java.util.Arrays;

public enum DifficultyLevel {
    EASY("EASY"),
    MEDIUM("MEDIUM"),
    HARD("HARD");

    DifficultyLevel(String d) {
        this.d = d;
    }

    private final String d;

    // from value to enum
    public static DifficultyLevel fromValue(String d) {
        for (final DifficultyLevel level : DifficultyLevel.values())
            if (level.d.equals(d))
                return level;

        throw new EnumIllegalArgumentException(
                "DIFFICULTY_LEVEL",
                d,
                Arrays.toString(DifficultyLevel.values())
        );
    }
}
