package me.cjcrafter.doorrestore;

import me.cjcrafter.doorrestore.listeners.DoorInteractListener;
import me.deecaad.core.file.Configuration;
import me.deecaad.core.file.DuplicateKeyException;
import me.deecaad.core.file.FileCopier;
import me.deecaad.core.file.FileReader;
import me.deecaad.core.file.JarInstancer;
import me.deecaad.core.file.LinkedConfig;
import me.deecaad.core.file.Serializer;
import me.deecaad.core.utils.FileUtil;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;

public class DoorRestore extends JavaPlugin {

    private static DoorRestore plugin;
    private Configuration configuration;

    @Override
    public void onEnable() {
        plugin = this;
        configuration = new LinkedConfig();

        loadConfig();
        registerListeners();

        new Regenerator().runTaskTimer(this, 20, configuration.getInt("Interval", 400));
    }

    public void onReload() {
        onDisable();
        plugin = this;

        loadConfig();
        registerListeners();

        new Regenerator().runTaskTimer(this, 20, configuration.getInt("Interval", 400));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        getServer().getScheduler().cancelTasks(this);
        configuration.clear();
        plugin = null;
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new DoorInteractListener(), this);
    }

    private void loadConfig() {
        if (!getDataFolder().exists() || getDataFolder().listFiles() == null || getDataFolder().listFiles().length == 0)
            FileUtil.copyResourcesTo(getClass(), getClassLoader(), "resources/DoorRestore", getDataFolder());

        List<Serializer<?>> serializers = null;
        try {
            JarFile jar = new FileCopier().getJarFile(this, getFile());
            serializers = (List<Serializer<?>>) (List<?>) new JarInstancer(jar).createAllInstances(Serializer.class, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Configuration config = new FileReader(serializers, null).fillAllFiles(getDataFolder());
        try {
            this.configuration.add(config);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public static DoorRestore getPlugin() {
        return plugin;
    }
}
