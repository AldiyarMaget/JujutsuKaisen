package org.example.battle;

import org.example.caster.SpellCaster;
import org.example.model.Entity;
import org.example.model.PlayerMage;
import org.example.model.Monster;
import org.example.util.Sanitizer;

import java.util.Random;
import java.util.Objects;

public final class CastSpell implements BattleVisitor {
    private static final double CRIT_CHANCE = 0.10;
    private static final double CRIT_MULT = 2.0;
    private static final double VARIANCE = 0.10;

    private final Entity attacker;
    private final SpellType spell;
    private final SpellCaster caster;
    private final Random rng;

    private int lastValue = 0;
    private String lastMessage = "";

    public CastSpell(Entity attacker, SpellType spell, SpellCaster caster, Random rng) {
        this.attacker = Objects.requireNonNull(attacker);
        this.spell = Objects.requireNonNull(spell);
        this.caster = Objects.requireNonNull(caster);
        this.rng = Objects.requireNonNull(rng);
    }

    public int getLastValue() { return lastValue; }
    public String getLastMessage() { return lastMessage; }

    @Override
    public void visitPlayerMage(PlayerMage target) {
        applyTo(target);
    }

    @Override
    public void visitMonster(Monster target) {
        applyTo(target);
    }

    private void applyTo(Entity target) {
        switch (spell) {
            case HIT -> doHit(target);
            case HEAL -> doHeal(target);
            case SHIELD -> doShield(target);
            case FIRE -> doFire(target);
            default -> throw new IllegalStateException("Unknown spell " + spell);
        }
    }

    private double varianceFactor() {
        return 1.0 + ((rng.nextDouble() * 2.0 - 1.0) * VARIANCE);
    }

    private boolean isCrit() {
        return rng.nextDouble() < CRIT_CHANCE;
    }

    private void doHit(Entity target) {
        int base = attacker.getAttributes().getAttack();
        double power = base;
        double raw = Math.max(0, power - target.getAttributes().getDefense() * 0.5);
        boolean crit = isCrit();
        double mult = crit ? CRIT_MULT : 1.0;
        int dmg = (int)Math.round(raw * mult * varianceFactor());
        target.getAttributes().setHp(target.getAttributes().getHp() - dmg);
        lastValue = dmg;
        lastMessage = String.format("HIT %d%s", dmg, crit ? " (CRIT)" : "");
        caster.cast(attacker, target, Sanitizer.sanitizeForLog(lastMessage));
    }

    private void doHeal(Entity target) {
        int mag = attacker.getAttributes().getMagic();
        int heal = (int)Math.round(mag * 1.2 * varianceFactor());
        target.getAttributes().setHp(target.getAttributes().getHp() + heal);
        lastValue = heal;
        lastMessage = String.format("HEAL %d", heal);
        caster.cast(attacker, target, Sanitizer.sanitizeForLog(lastMessage));
    }

    private void doShield(Entity target) {
        int bonus = Math.max(1, attacker.getAttributes().getMagic() / 2);
        target.getAttributes().setDefense(target.getAttributes().getDefense() + bonus);
        lastValue = bonus;
        lastMessage = String.format("SHIELD +%d DEF", bonus);
        caster.cast(attacker, target, Sanitizer.sanitizeForLog(lastMessage));
    }

    private void doFire(Entity target) {
        int mag = attacker.getAttributes().getMagic();
        double raw = Math.max(0, mag * 1.5 - target.getAttributes().getDefense() * 0.3);
        boolean crit = isCrit();
        double mult = crit ? CRIT_MULT : 1.0;
        int dmg = (int)Math.round(raw * mult * varianceFactor());
        target.getAttributes().setHp(target.getAttributes().getHp() - dmg);
        lastValue = dmg;
        lastMessage = String.format("FIRE %d%s", dmg, crit ? " (CRIT)" : "");
        caster.cast(attacker, target, Sanitizer.sanitizeForLog(lastMessage));
    }
}
