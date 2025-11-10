package org.example.caster;

import org.example.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleSpellCaster implements SpellCaster {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleSpellCaster.class);

    @Override
    public void cast(Entity actor, Entity target, String message) {
        String actorName = actor.getName();
        String targetName = target.getName();
        logger.info("[{} -> {}] {}", actorName, targetName, message);
    }
}
