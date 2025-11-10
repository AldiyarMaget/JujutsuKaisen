package org.example.builder;

import org.example.model.Attributes;

import static java.lang.Math.*;

public class DefMonsterAttributesBuilder implements AttributesBuilder {
    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int attack;
    private int magic;
    private int defense;
    private int speed;

    public DefMonsterAttributesBuilder() {
        this.hp = 0; this.maxHp = 0; this.mana = 0; this.maxMana = 0;
        this.attack = 0; this.magic = 0; this.defense = 0; this.speed = 0;
    }

    @Override public DefMonsterAttributesBuilder hp(int hp) { this.hp = hp; return this; }
    @Override public DefMonsterAttributesBuilder maxHp(int maxHp) { this.maxHp = maxHp; return this; }
    @Override public DefMonsterAttributesBuilder mana(int mana) { this.mana = mana; return this; }
    @Override public DefMonsterAttributesBuilder maxMana(int maxMana) { this.maxMana = maxMana; return this; }
    @Override public DefMonsterAttributesBuilder attack(int attack) { this.attack = attack; return this; }
    @Override public DefMonsterAttributesBuilder magic(int magic) { this.magic = magic; return this; }
    @Override public DefMonsterAttributesBuilder defense(int defense) { this.defense = defense; return this; }
    @Override public DefMonsterAttributesBuilder speed(int speed) { this.speed = speed; return this; }

    @Override
    public AttributesBuilder withDefaults() {
        this.hp = 120;
        this.maxHp = 120;
        this.mana = 0;
        this.maxMana = 0;
        this.attack = 12;
        this.magic = 4;
        this.defense = 6;
        this.speed = 8;
        return this;
    }

    @Override
    public Attributes build() {
        if (maxHp <= 0) {
            maxHp = max(maxHp, hp);
            if (maxHp <= 0) throw new IllegalStateException("maxHp must be > 0");
        }
        if (hp <= 0) hp = maxHp;
        if (maxMana < 0) throw new IllegalStateException("maxMana must be >= 0");
        if (mana < 0) mana = 0;

        if (attack < 0 || magic < 0 || defense < 0 || speed < 0) {
            throw new IllegalStateException("attributes must be non-negative");
        }

        int finalHp = min(hp, maxHp);
        int finalMana = min(mana, maxMana);

        return new Attributes(finalHp, finalMana, attack, magic, defense, speed);
    }
}
