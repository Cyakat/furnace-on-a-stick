package net.cyakat.furnaxe.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class FurnAxeItem extends AxeItem {

    public FurnAxeItem(ToolMaterial material, float attackDamage, float attackSpeed, Settings settings) {
        super(material, attackDamage, attackSpeed, settings);
    }

    /* TODO:
        1. Allow the player to get the wax-off achievement
     */

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        Hand hand = context.getHand();

        if (context.getPlayer().isSneaking()) {
            return super.useOnBlock(context);
        }


        if (!world.isClient && hand == Hand.MAIN_HAND) {
            Optional<SmeltingRecipe> recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(getItemStackOfActivatedBlock(context.getWorld().getBlockState(context.getBlockPos()))), world);
            if (recipe.isPresent()) {
                PlayerEntity playerEntity = context.getPlayer();
                BlockPos blockPos = context.getBlockPos();
                ItemStack recipeOutput = recipe.get().getOutput(world.getRegistryManager());
                ItemStack heldItem = context.getStack();
                Item recipeOutputItem = recipeOutput.getItem();
                ServerWorld serverWorld = Objects.requireNonNull(context.getWorld().getServer()).getWorld(context.getWorld().getRegistryKey());

                if (itemHasBlockVariant(recipeOutput.getItem())) {
                    BlockState smeltedBlock = Block.getBlockFromItem(recipeOutputItem).getDefaultState();

                    world.setBlockState(blockPos, smeltedBlock);
                    heldItem.damage(1, playerEntity, player -> player.sendToolBreakStatus(context.getHand()));
                }
                else {
                    ItemEntity smeltedItem = new ItemEntity(world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(recipeOutputItem));

                    world.breakBlock(blockPos, false);
                    world.spawnEntity(smeltedItem);
                    heldItem.damage(1, playerEntity, player -> player.sendToolBreakStatus(hand));
                }

                dropExperience(serverWorld, blockPos, 1, recipe.get().getExperience());
                createParticles(world, blockPos, 20);
                playSound(world, blockPos);
                return ActionResult.success(true);

            }
        }

        return ActionResult.PASS;
    }

    private static void dropExperience(ServerWorld world, BlockPos blockPos, int multiplier, float experience) {
        int i = MathHelper.floor((float)multiplier * experience);
        float f = MathHelper.fractionalPart((float)multiplier * experience);
        if (f != 0.0f && Math.random() < (double)f) {
            ++i;
        }
        ExperienceOrbEntity.spawn(world, blockPos.toCenterPos(), i);
    }

    private static boolean itemHasBlockVariant(Item item) {
            return Block.getBlockFromItem(item) != Blocks.AIR;
    }

    private static ItemStack getItemStackOfActivatedBlock(BlockState blockState) {

        return new ItemStack(blockState.getBlock().asItem());
    }

    public static void createParticles(World world, BlockPos blockPos, int count) {
        double a, b, c;
        for (int i = 0; i < count; i++) {

            a = world.random.nextDouble();
            b = world.random.nextDouble();
            c = world.random.nextDouble();

            switch (world.random.nextInt(6)) {
                case 0 -> a = 0;
                case 1 -> a = 1;
                case 2 -> b = 0;
                case 3 -> b = 1;
                case 4 -> c = 0;
                case 5 -> c = 1;
                default -> a = 0;
            }

            spawnParticle(world, ParticleTypes.FLAME, blockPos.getX() + a, blockPos.getY() + b, blockPos.getZ() + c, 0d, 0d, 0d);
        }
    }

    private static void playSound(World world, BlockPos blockPos) {
        world.playSound(null, blockPos, SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, .5f, 1f);
    }

    //spawnParticle - spawns particles across both client & server
    //Method taken from https://github.com/Sweenus/SimplySwords/blob/Architectury/common/src/main/java/net/sweenus/simplyswords/util/HelperMethods.java by Sweenus
    public static void spawnParticle(World world, ParticleEffect particle, double  xpos, double ypos, double zpos,
                                     double xvelocity, double yvelocity, double zvelocity) {

        if (world.isClient) {
            world.addParticle(particle, xpos, ypos, zpos, xvelocity, yvelocity, zvelocity);
        } else {
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(particle, xpos, ypos, zpos, 1, xvelocity, yvelocity, zvelocity, 0);
            }
        }
    }

}
