package com.bigdick;

import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.*;

public class MusicCooldownManager {
    private static final ConcurrentMap<UUID, Long> cooldowns = new ConcurrentHashMap<>();

    public static boolean isOnCooldown(Player player) {
        Long expire = cooldowns.get(player.getUniqueId());
        return expire != null && expire > System.currentTimeMillis();
    }

    public static void setCooldown(Player player, int ticks) {
        cooldowns.put(player.getUniqueId(), 
            System.currentTimeMillis() + (ticks * 50L));
    }

    public static void clearCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
}