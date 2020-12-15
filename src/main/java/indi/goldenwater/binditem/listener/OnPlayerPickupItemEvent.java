/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: OnPlayerPickupItemEvent.java
 * Author: Golden_Water
 * Email: 2439577268@qq.com
 * LastModified: 2020/12/15 15:10:15
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

import indi.goldenwater.binditem.module.ItemBindAndUnbind;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.util.Vector;

import java.util.List;

public class OnPlayerPickupItemEvent implements Listener {
    @EventHandler(ignoreCancelled = true)
    public static void onPlayerPickupItemEvent(PlayerPickupItemEvent event){
        List<String> bindType = ItemBindAndUnbind.bindType(event.getItem().getItemStack());
        switch (bindType.get(0)){
            case "none":
                break;
            case "bindToPlayer":
                if (!bindType.get(1).equals(event.getPlayer().getName()) &&
                        !event.getPlayer().hasPermission("binditem.bypass.pickuplimit")){
//                    event.setCancelled(true);
                    event.getPlayer().sendMessage("§c你没有权限捡起这个物品.");
                    event.setCancelled(true);
                    event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(),event.getItem().getItemStack()).setVelocity(new Vector().setY(0.01));
                }
                break;
            case "bindOnPickup":
                ItemBindAndUnbind.bind(event.getPlayer().getName(), event.getItem().getItemStack());
                break;
            case "needRemove":
                event.getItem().getItemStack().removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }
}
