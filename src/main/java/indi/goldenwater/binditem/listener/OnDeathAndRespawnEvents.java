/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: OnDeadAndRespawnEvents.java
 * Author: Golden_Water
 * Email: 2439577268@qq.com
 * LastModified: 2020/12/17 15:59:17
 *
 * Commercial use is prohibited without permission.
 * Author must be noted for use,
 * The signature must be in a place where everyone using it can easily see it.
 * Any consequence arising out of the use of the code shall be borne by itself.
 *
 * Using or downloading code represents agreement to the protocol.
 *
 * 未经允许禁止商业使用。
 * 使用必须注明作者，
 * 署名必须在每个使用的人能都简单地看到的地方。
 * 使用代码造成的任何后果均自行承担。
 *
 * 使用或下载代码则代表同意该协议。
 */

package indi.goldenwater.binditem.listener;

import indi.goldenwater.binditem.BindItem;
import indi.goldenwater.binditem.module.ItemBindAndUnbind;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OnDeathAndRespawnEvents implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public static void onPlayerDeathEvent(PlayerDeathEvent event){
        if (!event.getKeepInventory()){
            event.setKeepInventory(true);
            Player player = event.getEntity();
            Inventory inv = player.getInventory();
            ItemStack[] items = inv.getContents();
            Location loc = player.getLocation();
            World world = player.getWorld();
            List<ItemStack> itemsNeedKeep = new ArrayList<>();

            Configuration config = BindItem.getPluginConfig();
            boolean isKeepBindingItemsOnDeath = config.getBoolean("keepBindingItemsOnDeath");
            boolean isKeepItemsNotTheirsOnDeath = config.getBoolean("keepItemsNotTheirsOnDeath");

            for (ItemStack item: items){
                if(item==null || item.getType() == Material.AIR) continue;

                List<String> bindType = ItemBindAndUnbind.bindType(item);

                if (bindType.get(0).equals("bindToPlayer")) {
                    if (bindType.get(1).equals(player.getName())){
                        if(isKeepBindingItemsOnDeath &&
                                player.hasPermission("binditem.protect.bindingitemprotect")){
                            itemsNeedKeep.add(item);
                            inv.remove(item);
                            continue;
                        }
                } else {
                        if(isKeepItemsNotTheirsOnDeath){
                            itemsNeedKeep.add(item);
                            inv.remove(item);
                            continue;
                        }
                    }
                }

                inv.remove(item);
                world.dropItemNaturally(loc,item);
            }
            if(!itemsNeedKeep.isEmpty()){
                BindItem.deathPlayersItems.put(player.getName(), itemsNeedKeep);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public static void onPlayerRespawnEvent(PlayerRespawnEvent event){
        List<ItemStack> itemsNeedKeep = BindItem.deathPlayersItems.get(event.getPlayer().getName());
        if (itemsNeedKeep != null && !itemsNeedKeep.isEmpty()){
            Inventory inv = event.getPlayer().getInventory();
            for (ItemStack item: itemsNeedKeep) {
                inv.addItem(item);
            }
            BindItem.deathPlayersItems.remove(event.getPlayer().getName());
        }
    }
}
