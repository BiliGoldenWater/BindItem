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
