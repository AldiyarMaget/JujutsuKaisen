package org.example.app;

import org.example.battle.BattleEngine;
import org.example.builder.AttributesDirector;
import org.example.builder.DefMonsterAttributesBuilder;
import org.example.builder.DefPlayerAttributesBuilder;
import org.example.caster.ConsoleSpellCaster;
import org.example.db.BattleRepository;
import org.example.db.DataSourceProvider;
import org.example.model.Attributes;
import org.example.model.Monster;
import org.example.model.PlayerMage;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;

public final class BattleRunner {
    private final String WELCOME_MESSAGE = "=== Magic Battle Simulator ===";
    private final Scanner scanner;

    public BattleRunner() {
        this(new Scanner(System.in));
    }

    public BattleRunner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        System.out.println(WELCOME_MESSAGE);

        System.out.print("Enter player name [Nanami]: ");
        String playerName = readLineOrDefault("Nanami");

        System.out.print("Enter monster name [Mahito]: ");
        String monsterName = readLineOrDefault("Mahito");

        System.out.print("Enter seed (number) or press Enter for random: ");
        Long seed = parseSeed();

        Random rng = new Random(seed);
        System.out.println("Using seed: " + seed);

        Attributes playerAttrs = new AttributesDirector(new DefPlayerAttributesBuilder()).createPlayerAttributes();
        Attributes monsterAttrs = new AttributesDirector(new DefMonsterAttributesBuilder()).createMonsterAttributes();


        PlayerMage player = new PlayerMage(playerName, playerAttrs);
        Monster monster = new Monster(monsterName, monsterAttrs);

        ConsoleSpellCaster caster = new ConsoleSpellCaster();

        Optional<DataSource> ods = DataSourceProvider.fromEnv();
        BattleRepository repo = ods.map(BattleRepository::new).orElse(null);
        if (repo == null) {
            System.out.println("DB not configured (DB_URL/DB_USER/DB_PASSWORD env vars). Running without persistence.");
        } else {
            System.out.println("DB configured: events will be stored.");
        }

        BattleEngine engine = new BattleEngine(player, monster, caster, rng, repo);
        engine.runInteractive();
    }

    private String readLineOrDefault(String defaultValue) {
        try {
            String line = scanner.nextLine().trim();
            return line.isEmpty() ? defaultValue : line;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private Long parseSeed() {
        try {
            String seedLine = scanner.nextLine().trim();
            if (!seedLine.isEmpty()) {
                try {
                    return Long.parseLong(seedLine);
                } catch (NumberFormatException ex) {
                    return System.currentTimeMillis();
                }
            } else {
                return System.currentTimeMillis();
            }
        } catch (Exception e) {
            return System.currentTimeMillis();
        }
    }
}
