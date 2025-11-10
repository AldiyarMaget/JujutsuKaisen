package org.example.model;

import org.example.battle.BattleVisitor;
import java.util.Objects;

public abstract class Entity {
    private final String name;
    private final Attributes attrs;

    protected Entity(String name, Attributes attrs) {
        this.name = Objects.requireNonNull(name, "name");
        this.attrs = Objects.requireNonNull(attrs, "attrs");
    }

    public String getName() { return name; }
    public Attributes getAttributes() { return attrs; }

    public boolean isAlive() { return attrs.getHp() > 0; }

    public abstract void accept(BattleVisitor visitor);
}
