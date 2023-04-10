package net.cyakat.furnaxe.item.custom;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BurningIronItem extends Item {

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if (selected && !world.isClient) {
            entity.setFireTicks(20);

        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    public BurningIronItem(Settings settings) {
        super(settings);
    }
}
