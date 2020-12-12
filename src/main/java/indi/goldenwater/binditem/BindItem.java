package indi.goldenwater.binditem;

import indi.goldenwater.binditem.command.BindItemExecutor;
import indi.goldenwater.binditem.enchant.RegisterEnchantBindItem;
import indi.goldenwater.binditem.module.DBOperator;
import org.bukkit.plugin.java.JavaPlugin;

public final class BindItem extends JavaPlugin {
    private static BindItem instance;
    private static boolean useUUID;
    private static DBOperator enchantLvlDatabase;
    private static String pluginPath;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        useUUID = getServer().getOnlineMode();
        pluginPath = ".\\"+getServer().getPluginManager().getPlugin("BindItem").getDataFolder().getPath()+"\\";
        saveDefaultConfig();
        enchantLvlDatabase = new DBOperator(pluginPath + "data.db");

//        getServer().getPluginManager().registerEvents(,this);

        RegisterEnchantBindItem.registerEnchantBindItem();
        BindItemExecutor.registerCommandBindItem(this);
        getLogger().info("BindItem Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
        getLogger().info("BindItem Disabled");
    }

    public static BindItem getInstance(){
        return instance;
    }

    public static boolean isUsingUUID(){
        return useUUID;
    }

    public static DBOperator getEnchantLvlDatabase(){
        return enchantLvlDatabase;
    }

    public static String getPluginPath(){
        return pluginPath;
    }
}
