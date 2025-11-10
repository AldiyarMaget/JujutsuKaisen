package org.example.model;

public final class Attributes {
    private int hp;
    private int maxHp;
    private int mana;
    private int maxMana;
    private int attack;
    private int magic;
    private int defense;
    private int speed;

    public Attributes(int hp, int mana, int attack, int magic, int defense, int speed) {
        this.maxHp = hp;
        this.hp = hp;
        this.maxMana = mana;
        this.mana = mana;
        this.attack = attack;
        this.magic = magic;
        this.defense = defense;
        this.speed = speed;
    }

    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = Math.max(0, Math.min(hp, maxHp)); }
    public int getMaxHp() { return maxHp; }

    public int getMana() { return mana; }
    public void setMana(int mana) { this.mana = Math.max(0, Math.min(mana, maxMana)); }
    public int getMaxMana() { return maxMana; }

    public int getAttack() { return attack; }
    public int getMagic() { return magic; }
    public int getDefense() { return defense; }
    public void setDefense(int defense) { this.defense = Math.max(0, defense); }
    public int getSpeed() { return speed; }

    @Override
    public String toString() {
        return String.format("HP:%d/%d M:%d ATK:%d MAG:%d DEF:%d SPD:%d",
                hp, maxHp, mana, attack, magic, defense, speed);
    }
}
