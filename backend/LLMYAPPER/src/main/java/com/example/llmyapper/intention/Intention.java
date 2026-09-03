package com.example.llmyapper.intention;

import com.example.llmyapper.schema.PlayerIntention;

public enum Intention {
    ATTACK(true),
    CLIMB (true),
    EXAMINE(true),
    GRAPPLE(true),
    HIDE(true),
    INSIGHT(true),
    JUMP(true),
    PERCEIVE(true),
    PULL(true),
    PUSH(true),
    SEARCH(true),
    SHOVE(true),
    SNEAK(true),
    STEAL(true),
    PERSUADE(true),
    INTIMIDATE(true),
    TALK(false),
    MOVE(false),
    TAKE(false),
    DROP(false),
    USE(false),
    INTERACT(false),
    OPEN(false),
    CLOSE(false),
    READ(false),
    EQUIP(false),
    UNEQUIP(false),
    HELP(false),
    GIVE(false),
    FOLLOW(false),
    WAIT(false),
    REST(false),
    LOOK(false),
    OTHER(false);

    private final boolean requiresRoll;

    Intention(boolean requiresRoll) {
        this.requiresRoll = requiresRoll;
    }

    public boolean requiresRoll() {
        return requiresRoll;
    }
}
