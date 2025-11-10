package org.example.db;

import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;

public final class BattleRepository {
    private final DataSource ds;

    public BattleRepository(DataSource ds) {
        this.ds = ds;
        init();
    }

    private void init() {
        try (Connection c = ds.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS battles (
                  id BIGSERIAL PRIMARY KEY,
                  started_at TIMESTAMP WITH TIME ZONE NOT NULL,
                  finished_at TIMESTAMP WITH TIME ZONE,
                  seed BIGINT,
                  winner VARCHAR(128)
                );
                """);
            s.execute("""
                CREATE TABLE IF NOT EXISTS battle_events (
                  id BIGSERIAL PRIMARY KEY,
                  battle_id BIGINT REFERENCES battles(id) ON DELETE CASCADE,
                  turn INT,
                  actor_type VARCHAR(64),
                  actor_name VARCHAR(128),
                  target_type VARCHAR(64),
                  target_name VARCHAR(128),
                  action VARCHAR(64),
                  value INT,
                  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
                );
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public long createBattle(Instant startedAt, Instant finishedAt, long seed, String winner) {
        String sql = "INSERT INTO battles(started_at, finished_at, seed, winner) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.from(startedAt));
            if (finishedAt != null) ps.setTimestamp(2, Timestamp.from(finishedAt)); else ps.setNull(2, Types.TIMESTAMP);
            ps.setLong(3, seed);
            if (winner != null) ps.setString(4, winner); else ps.setNull(4, Types.VARCHAR);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    return id;
                } else {
                    throw new SQLException("Insert returned no id.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void insertEvent(long battleId, int turn, String actorType, String actorName,
                            String targetType, String targetName, String action, int value) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO battle_events(battle_id, turn, actor_type, actor_name, target_type, target_name, action, value) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps.setLong(1, battleId);
            ps.setInt(2, turn);
            ps.setString(3, actorType);
            ps.setString(4, actorName);
            ps.setString(5, targetType);
            ps.setString(6, targetName);
            ps.setString(7, action);
            ps.setInt(8, value);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void finishBattle(long battleId, Instant finishedAt, String winner) {
        try (Connection c = ds.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE battles SET finished_at = ?, winner = ? WHERE id = ?")) {
            ps.setTimestamp(1, Timestamp.from(finishedAt));
            ps.setString(2, winner);
            ps.setLong(3, battleId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
