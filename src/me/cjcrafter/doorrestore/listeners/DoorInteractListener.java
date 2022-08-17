package me.cjcrafter.doorrestore.listeners;

import me.cjcrafter.doorrestore.DoorRestore;
import me.cjcrafter.doorrestore.RestoreData;
import me.deecaad.core.file.Configuration;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class DoorInteractListener implements Listener {

    private Configuration config;

    public DoorInteractListener() {
        config = DoorRestore.getPlugin().getConfiguration();
    }

    @EventHandler (ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Block block = e.getClickedBlock();
        BlockData data = block.getBlockData();

        if (!(data instanceof Openable)) {
            return;
        }

        Openable openable = (Openable) data;
        long restoreTime = System.currentTimeMillis() + config.getInt("Regenerate_After") * 20 / 1000;

        if (openable instanceof Door) {
            Door door = (Door) openable;

            // Get the bottom half of the door
            Block bottom = door.getHalf() == Bisected.Half.BOTTOM ? block : block.getRelative(BlockFace.DOWN);

            if (RestoreData.contains(bottom)) {
                RestoreData.dataMap.get(bottom).setRestoreTime(restoreTime);
            } else {
                new RestoreData(bottom, restoreTime);
            }
        } else {
            if (RestoreData.contains(block)) {
                RestoreData.dataMap.get(block).setRestoreTime(restoreTime);
            } else {
                new RestoreData(block, restoreTime);
            }
        }
    }
}
