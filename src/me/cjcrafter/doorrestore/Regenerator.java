package me.cjcrafter.doorrestore;

import me.deecaad.core.file.Configuration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Regenerator extends BukkitRunnable {

    private Configuration config;

    public Regenerator() {
        config = DoorRestore.getPlugin().getConfiguration();
    }

    @Override
    public void run() {
        ArrayList<RestoreData> dataSet = new ArrayList<>(RestoreData.dataMap.values());

        long current = System.currentTimeMillis();
        int max = Math.min(dataSet.size(), config.getInt("Max_To_Regenerate"));
        for (int i = 0; i < max; i++) {

            RestoreData data = dataSet.get(i);
            if (current >= data.getRestoreTime()) {
                data.restore();
            }
        }
    }
}