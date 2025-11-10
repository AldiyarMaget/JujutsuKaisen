package org.example.battle;

import org.example.caster.SpellCaster;
import org.example.db.BattleRepository;
import org.example.model.Entity;
import org.example.model.PlayerMage;
import org.example.model.Monster;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public final class BattleEngine {
    private final PlayerMage player;
    private final Monster monster;
    private final SpellCaster caster;
    private final Random rng;
    private final BattleRepository repository;

    public BattleEngine(PlayerMage player, Monster monster, SpellCaster caster, Random rng, BattleRepository repository) {
        this.player = player;
        this.monster = monster;
        this.caster = caster;
        this.rng = rng;
        this.repository = repository;
    }

    public String runInteractive() {
        long battleId = repository != null ? repository.createBattle(Instant.now(), null, rng.nextLong(), null) : -1L;
        Scanner sc = new Scanner(System.in);
        int turn = 1;
        while (player.isAlive() && monster.isAlive()) {
            System.out.println("\n--- Turn " + turn + " ---");
            System.out.println(player.getName() + " " + player.getAttributes());
            System.out.println(monster.getName() + " " + monster.getAttributes());

            List<Entity> order = List.of(player, monster);
            order = order.stream().sorted(Comparator.comparingInt(e -> -e.getAttributes().getSpeed()))
                    .collect(Collectors.toList());
            if (order.get(0).getAttributes().getSpeed() == order.get(1).getAttributes().getSpeed()) {
                if (rng.nextBoolean()) Collections.reverse(order);
            }

            for (Entity actor : order) {
                if (!player.isAlive() || !monster.isAlive()) break;
                if (actor == player) {
                    System.out.println("\nYour turn. Choose action:");
                    System.out.println("1) HIT");
                    System.out.println("2) HEAL (self)");
                    System.out.println("3) SHIELD (self)");
                    System.out.println("4) FIRE");
                    System.out.print("Enter choice [1-4]: ");
                    int choice = 1;
                    try {
                        String line = sc.nextLine();
                        if (line != null && !line.isBlank()) choice = Integer.parseInt(line.trim());
                    } catch (Exception ex) {
                        choice = 1;
                    }
                    SpellType st = switch (choice) {
                        case 2 -> SpellType.HEAL;
                        case 3 -> SpellType.SHIELD;
                        case 4 -> SpellType.FIRE;
                        default -> SpellType.HIT;
                    };
                    Entity target = (st == SpellType.HEAL || st == SpellType.SHIELD) ? player : monster;
                    CastSpell spell = new CastSpell(player, st, caster, rng);
                    target.accept(spell);
                    if (repository != null) repository.insertEvent(battleId, turn, actor.getClass().getSimpleName(), actor.getName(),
                            target.getClass().getSimpleName(), target.getName(), st.name(), spell.getLastValue());
                } else {
                    SpellType st = rng.nextDouble() < 0.2 ? SpellType.FIRE : SpellType.HIT;
                    CastSpell spell = new CastSpell(monster, st, caster, rng);
                    Entity target = player;
                    target.accept(spell);
                    if (repository != null) repository.insertEvent(battleId, turn, actor.getClass().getSimpleName(), actor.getName(),
                            target.getClass().getSimpleName(), target.getName(), st.name(), spell.getLastValue());
                }
            }

            turn++;
        }

        String winner = player.isAlive() ? player.getName() : monster.getName();
        System.out.println("\nBattle finished. Winner: " + winner);
        if (repository != null) repository.finishBattle(battleId, Instant.now(), winner);
        return winner;
    }
}
