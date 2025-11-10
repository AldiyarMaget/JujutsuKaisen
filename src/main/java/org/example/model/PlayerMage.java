package org.example.model;

public final class PlayerMage extends Entity {
    public PlayerMage(String name, Attributes attrs) {
        super(name, attrs);
    }

    @Override
    public void accept(org.example.battle.BattleVisitor visitor) {
        visitor.visitPlayerMage(this);
    }
}
