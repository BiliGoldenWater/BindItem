package indi.goldenwater.binditem;

import indi.goldenwater.binditem.command.BindItemExecutor;
import indi.goldenwater.binditem.enchant.RegisterEnchantBindItem;
import org.bukkit.plugin.java.JavaPlugin;

public final class BindItem extends JavaPlugin {
    public static BindItem instance;
    public static boolean useUUID;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        useUUID = getServer().getOnlineMode();

//        getServer().getPluginManager().registerEvents(,this);

        RegisterEnchantBindItem.registerEnchantBindItem();
        BindItemExecutor.registerCommandBindItem(this);
        getLogger().info("BindItem Enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("BindItem Disabled");
    }

    public static BindItem getInstance(){
        return instance;
    }
}
