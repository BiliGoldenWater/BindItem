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

import indi.goldenwater.binditem.BindItem;
import indi.goldenwater.binditem.module.ItemBindAndUnbind;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class OnItemEvents implements Listener {
    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onPlayerPickupItemEvent(PlayerPickupItemEvent event){
        List<String> bindType = ItemBindAndUnbind.bindType(event.getItem().getItemStack());
        Player player = event.getPlayer();
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (!bindType.get(1).equals(player.getName()) &&
                        !player.hasPermission("binditem.bypass.pickuplimit")){
                    player.sendMessage("§c你没有权限捡起这个物品.");
                    event.setCancelled(true);
                    player.getWorld().dropItem(player.getLocation(),event.getItem().getItemStack()).setVelocity(new Vector().setY(0.01));
                }
                break;
            case "bindOnPickup":
                ItemBindAndUnbind.bind(player.getName(), event.getItem().getItemStack());
                break;
            case "needRemove":
                event.getItem().getItemStack().removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItem(event.getNewSlot());
        if(item==null || item.getType() == Material.AIR) return;
        List<String> bindType = ItemBindAndUnbind.bindType(item);
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (!bindType.get(1).equals(player.getName()) &&
                        !player.hasPermission("binditem.bypass.uselimit")){
                    player.sendMessage("§c你没有权限使用这个物品.");
                    event.setCancelled(true);
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onPlayerInteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item==null || item.getType() == Material.AIR) return;
        List<String> bindType = ItemBindAndUnbind.bindType(item);
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (!bindType.get(1).equals(player.getName()) &&
                        !player.hasPermission("binditem.bypass.uselimit")){
                    player.sendMessage("§c你没有权限使用这个物品.");
                    event.setCancelled(true);
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item ==null || item.getType() == Material.AIR) return;
        List<String> bindType = ItemBindAndUnbind.bindType(item);
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (!bindType.get(1).equals(player.getName()) &&
                        !player.hasPermission("binditem.bypass.uselimit")){
                    player.sendMessage("§c你没有权限使用这个物品.");
                    event.setCancelled(true);
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onInventoryClickEvent(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if(event.getClick().isKeyboardClick()){
            switch (event.getAction()){
                case HOTBAR_SWAP:
                    if(BindItem.getPluginConfig().getBoolean("disableHotBarSwap")){
                        event.setCancelled(true);
                    }
                    break;
                case DROP_ONE_SLOT:
                case DROP_ALL_SLOT:
                    return;
            }
        }

        if(item==null || item.getType() == Material.AIR) return;
        List<String> bindType = ItemBindAndUnbind.bindType(item);
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (!bindType.get(1).equals(player.getName()) &&
                        !player.hasPermission("binditem.bypass.movelimit")){
                    player.sendMessage("§c你没有权限移动这个物品.");
                    event.setCancelled(true);
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(event.getDamager().getType() != EntityType.PLAYER) return;

        Player player = (Player) event.getDamager();
        ItemStack item = player.getItemInHand();

        if(item==null || item.getType() == Material.AIR) return;

        List<String> bindType = ItemBindAndUnbind.bindType(item);
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (!bindType.get(1).equals(player.getName()) &&
                        !player.hasPermission("binditem.bypass.uselimit")){
                    player.sendMessage("§c你没有权限使用这个物品.");
                    event.setCancelled(true);
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }

    @EventHandler(ignoreCancelled = true , priority = EventPriority.HIGHEST)
    public static void onPlayerDropItemEvent(PlayerDropItemEvent event){
        Player player = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if(item==null || item.getType() == Material.AIR) return;
        List<String> bindType = ItemBindAndUnbind.bindType(item);
        switch (bindType.get(0)){
            case "bindToPlayer":
                if (/*bindType.get(1).equals(player.getName()) &&*/
                        !player.hasPermission("binditem.bypass.droplimit") &&
                        BindItem.getInstance().getConfig().getBoolean("denyPlayerDropBindingItems")){
                    player.sendMessage("§c你不能丢弃这个物品.");
                    event.setCancelled(true);
                    break;
                }
                break;
            case "needRemove":
                item.removeEnchantment(Enchantment.getByName("bind_item"));
                break;
        }
    }
}
