package com.devramisha.handsomelook.makeup.utils;

/**
 * Developer:Md Ramish
 * time:2021-10
 * function: default function
 */
public enum Region {

    FOUNDATION("Foundation"),
    BLUSH("Blush"),
    LIP("Lip gloss"),
    BROW("Eyebrow"),

    EYE_LASH("eyelash"),
    EYE_CONTACT("Cosmetic contact lenses"),
    EYE_DOUBLE("Double eyelid"),
    EYE_LINE("Eyeliner"),
    EYE_SHADOW("Eye shadow");

    private String name;

    Region(String name) {
        this.name = name;
    }
}
