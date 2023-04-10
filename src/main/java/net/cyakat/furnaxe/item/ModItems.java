package net.cyakat.furnaxe.item;

import net.cyakat.furnaxe.FurnAxe;
import net.cyakat.furnaxe.item.custom.BurningIronItem;
import net.cyakat.furnaxe.item.custom.FurnAxeItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {



    public static Item FURN_AXE = registerItem("furn_axe", new FurnAxeItem(ToolMaterials.IRON, 6f, -3.1f, new FabricItemSettings().maxDamage(500)));
    public static Item BURNING_IRON = registerItem("burning_iron", new BurningIronItem(new FabricItemSettings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(FurnAxe.MOD_ID, name), item);
    }
    public static void registerModItems() {
        FurnAxe.LOGGER.debug("Registering Items for " + FurnAxe.MOD_ID);
    }
}
