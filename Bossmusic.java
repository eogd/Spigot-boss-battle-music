package com.bigdick;

import org.bukkit.*;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

public class Bossmusic extends JavaPlugin implements Listener {
    
    private static final int CHECK_INTERVAL = 20 * 5; 
    private static final int COOLDOWN = 7200;
    private static final int RADIUS = 12 * 16; 
    private static final String SOUND_ID = "ender_dragon.music";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        
        new BukkitRunnable() {
            @Override
            public void run() {
                Set<UUID> activePlayers = new HashSet<>();
                
                Bukkit.getWorlds().forEach(world -> 
                    world.getEntitiesByClass(EnderDragon.class).forEach(dragon -> 
                        dragon.getNearbyEntities(RADIUS, RADIUS, RADIUS).stream()
                            .filter(e -> e instanceof Player)
                            .map(e -> (Player) e)
                            .forEach(p -> activePlayers.add(p.getUniqueId()))
                    )
                );

                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (!activePlayers.contains(player.getUniqueId())) {
                        player.stopSound(SOUND_ID, SoundCategory.MUSIC);
                        MusicCooldownManager.clearCooldown(player);
                    }
                });

                activePlayers.forEach(uuid -> {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null && !MusicCooldownManager.isOnCooldown(player)) {
                        player.playSound(
                            player.getLocation(),
                            SOUND_ID,
                            SoundCategory.MUSIC,
                            1.0f,
                            1.0f
                        );
                        MusicCooldownManager.setCooldown(player, COOLDOWN);
                    }
                });
            }
        }.runTaskTimer(this, 0, CHECK_INTERVAL);
    }

    @EventHandler
    public void onDragonSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
        }
    }
}
