package org.example.battle;

import org.example.model.PlayerMage;
import org.example.model.Monster;

public interface BattleVisitor {
    void visitPlayerMage(PlayerMage target);
    void visitMonster(Monster target);
}
