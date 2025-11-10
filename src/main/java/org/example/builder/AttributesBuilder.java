package org.example.builder;

import org.example.model.Attributes;

public interface AttributesBuilder {
    AttributesBuilder hp(int hp);
    AttributesBuilder maxHp(int maxHp);
    AttributesBuilder mana(int mana);
    AttributesBuilder maxMana(int maxMana);
    AttributesBuilder attack(int attack);
    AttributesBuilder magic(int magic);
    AttributesBuilder defense(int defense);
    AttributesBuilder speed(int speed);
    AttributesBuilder withDefaults();

    Attributes build();
}
