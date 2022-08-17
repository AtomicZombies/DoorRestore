package me.cjcrafter.doorrestore;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Door;

import java.util.HashMap;
import java.util.Map;

public class RestoreData {

    public static Map<Block, RestoreData> dataMap = new HashMap<>();

    private long restoreTime;
    private final boolean isOpen;
    private final Openable data;
    private final Block block;

    public RestoreData(Block block, long restoreTime) {
        Openable openable = (Openable) block.getBlockData();

        this.restoreTime = restoreTime;
        this.isOpen = openable.isOpen();
        this.data = openable;
        this.block = block;

        dataMap.put(block, this);
    }

    public long getRestoreTime() {
        return restoreTime;
    }

    public void setRestoreTime(long restoreTime) {
        this.restoreTime = restoreTime;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public Openable getData() {
        return data;
    }

    public void restore() {
        data.setOpen(isOpen);
        block.setBlockData(data, false);

        if (data instanceof Door) {
            Block top = block.getRelative(BlockFace.UP);
            Openable topData = (Openable) top.getBlockData();
            topData.setOpen(isOpen);

            top.setBlockData(topData, false);
        }

        dataMap.remove(block);
    }

    public static boolean contains(Block block) {
        return dataMap.containsKey(block);
    }
}
