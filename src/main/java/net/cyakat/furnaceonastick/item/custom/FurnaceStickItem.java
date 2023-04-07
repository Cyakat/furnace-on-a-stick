package net.cyakat.furnaceonastick.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class FurnaceStickItem extends Item {

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Hand hand = context.getHand();

        if (!world.isClient && hand == Hand.MAIN_HAND) {
            Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(getItemStackOfActivatedBlock(context)), world);
            if (recipe.isPresent()) {
                PlayerEntity playerEntity = context.getPlayer();
                BlockPos blockPos = context.getBlockPos();
                ItemStack recipeOutput = recipe.get().getOutput(world.getRegistryManager());
                ItemStack heldItem = context.getStack();
                Item recipeOutputItem = recipeOutput.getItem();

                if (itemHasBlockVariant(recipeOutput.getItem())) {
                    BlockState smeltedBlock = Block.getBlockFromItem(recipeOutputItem).getDefaultState();


                    world.setBlockState(blockPos, smeltedBlock);
                    heldItem.damage(1, playerEntity, player -> player.sendToolBreakStatus(context.getHand()));
                } else {
                    ItemEntity smeltedItem = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(recipeOutputItem));

                    world.breakBlock(blockPos, false);
                    world.spawnEntity(smeltedItem);
                    heldItem.damage(1, playerEntity, player -> player.sendToolBreakStatus(hand));
                }

                return ActionResult.success(true);
            }
        }

        return super.useOnBlock(context);
    }

    public FurnaceStickItem(Settings settings) {
        super(settings);
    }

    private static boolean itemHasBlockVariant(Item item) {
            return Block.getBlockFromItem(item) != Blocks.AIR;
    }

    private static ItemStack getItemStackOfActivatedBlock(ItemUsageContext context) {

        return new ItemStack(context.getWorld().getBlockState(context.getBlockPos()).getBlock().asItem());
    }
}
