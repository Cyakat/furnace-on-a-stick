package net.cyakat.furnaceonastick.item;

import net.cyakat.furnaceonastick.FurnaceOnAStick;
import net.cyakat.furnaceonastick.item.custom.FurnaceStickItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {



    public static Item FURNACE_ON_A_STICK = registerItem("furnace_on_a_stick", new FurnaceStickItem(new FabricItemSettings().maxCount(1).maxDamage(71)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FurnaceOnAStick.MOD_ID, name), item);
    }
    public static void registerModItems() {
        FurnaceOnAStick.LOGGER.debug("Registering Items for " + FurnaceOnAStick.MOD_ID);
    }
}
