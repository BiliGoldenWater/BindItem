/*
 * Copyright (c) 2020 by Golden_Water. All Right Reserved.
 * ProjectName: BindItem
 * FileName: RegisterEnchantBindItem.java
 * Author: Golden_Water
 * Email: 2439577268@qq.com
 * LastModified: 2020/12/15 00:31:15
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

package indi.goldenwater.binditem.enchant;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class RegisterEnchantBindItem {
    public static void registerEnchantBindItem(){
        Enchantment bind_item = new Enchantment(63) {
            @Override
            public String getName() {
                return "bind_item";
            }

            @Override
            public int getMaxLevel() {
                return Integer.MAX_VALUE;
            }

            @Override
            public int getStartLevel() {
                return 0;
            }

            @Override
            public EnchantmentTarget getItemTarget() {
                return EnchantmentTarget.ALL;
            }

            @Override
            public boolean conflictsWith(Enchantment enchantment) {
                return false;
            }

            @Override
            public boolean canEnchantItem(ItemStack itemStack) {
                return true;
            }
        };

        Enchantment.registerEnchantment(bind_item);
    }
}
