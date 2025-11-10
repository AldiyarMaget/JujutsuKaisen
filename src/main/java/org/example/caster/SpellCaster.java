package org.example.caster;

import org.example.model.Entity;

public interface SpellCaster {
    void cast(Entity actor, Entity target, String message);
}
