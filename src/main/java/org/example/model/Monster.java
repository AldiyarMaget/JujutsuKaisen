package org.example.model;

public final class Monster extends Entity {
    public Monster(String name, Attributes attrs) {
        super(name, attrs);
    }

    @Override
    public void accept(org.example.battle.BattleVisitor visitor) {
        visitor.visitMonster(this);
    }
}
