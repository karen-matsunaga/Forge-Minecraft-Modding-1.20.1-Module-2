package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModesPickaxe;
import net.karen.mccourse.util.ModTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.*;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;

// Credits by Parlack - Pickaxe modes - https://www.youtube.com/watch?v=pBo1c3hM3b0
// Using code with some modifications
public class ModesPickaxeItem extends PickaxeItem {
    private ModesPickaxe modeActual = ModesPickaxe.NORMAL; // Pickaxe DEFAULT mode actual

    // Pickaxe tier, attack damage, attack speed, properties, enchantment level and enchantments
    public ModesPickaxeItem(Tier tier, int attackDamageModifier,
                            float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    public ModesPickaxe getModeActual() { return modeActual; } // Getter Pickaxe mode actual

    // DEFAULT METHOD - Method is called when pressed right button -> Trade modes
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (!world.isClientSide()) {
            switch (modeActual) { // Trading the pickaxe mode
                case NORMAL: modeActual = ModesPickaxe.SLOW; break; // Normal -> Slow
                case SLOW: modeActual = ModesPickaxe.FAST; break; // Slow -> Fast
                case FAST: modeActual = ModesPickaxe.HAMMER; break; // Fast -> Hammer
                case HAMMER: modeActual = ModesPickaxe.AUTO_SMELT; break; // Hammer -> Auto Smelt
                case AUTO_SMELT: modeActual = ModesPickaxe.MORE_ORES; break; // Auto Smelt -> More Ores
                case MORE_ORES: modeActual = ModesPickaxe.MAGNETIC; break; // More Ores -> Magnetic
                case MAGNETIC: modeActual = ModesPickaxe.NORMAL; break; // Magnetic -> Normal
            }
            // Show message on chat with mode actual
            playerBool(player, "§6§lMode: §c§l" + modeActual.toString().replace("_", " ").toUpperCase());
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }

    // DEFAULT METHOD - Custom text tooltip
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        if (Screen.hasShiftDown()) {
            tooltipLineTranslatable(list, "tooltip.mccourse.modes_pickaxe.tooltip.shift");
            list.add(CommonComponents.NEW_LINE);
        }
        else { tooltipLineTranslatable(list, "tooltip.mccourse.modes_pickaxe.tooltip"); }
        super.appendHoverText(stack, level, list, flag);
    }

    // DEFAULT METHOD - Define mine speed of pickaxe depends on the mode
    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return switch (modeActual) {
            case SLOW -> 2.0F; // Stone pickaxe speed
            case FAST -> 25.0F; // Pickaxe ultra speed
            case HAMMER -> super.getDestroySpeed(stack, state); // Normal speed and breaks 3x3
            case AUTO_SMELT -> super.getDestroySpeed(stack, state); // Auto Smelt event
            case MORE_ORES -> super.getDestroySpeed(stack, state); // More Ores event
            case MAGNETIC -> super.getDestroySpeed(stack, state); // Magnetic event
            default -> super.getDestroySpeed(stack, state); // Normal speed
        };
    }

    // DEFAULT METHOD - When broken blocks with pickaxe
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level world, @NotNull BlockState blockstate,
                             @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (!world.isClientSide()) {
            if (modeActual == ModesPickaxe.SLOW || modeActual == ModesPickaxe.FAST) { // Slow mode and Fast mode logics
                stack.hurtAndBreak(1, entity, (e) -> e.broadcastBreakEvent(entity.getUsedItemHand()));
            }
            switch (modeActual) { // Hammer mode logic
                case HAMMER -> hammerMode(stack, world, pos, (Player) entity);
                case AUTO_SMELT -> autoSmeltMode(new BlockEvent.BreakEvent(world, pos, blockstate, (Player) entity));
                case MORE_ORES -> moreOresMode(new BlockEvent.BreakEvent(world, pos, blockstate, (Player) entity));
                case MAGNETIC -> magneticMode(new BlockEvent.BreakEvent(world, pos, blockstate, (Player) entity));
            }
        }
        return super.mineBlock(stack, world, blockstate, pos, entity);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) { return false; }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) { return false; }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) { return false; }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) { return true; }

    // CUSTOM METHOD - Hammer mode method
    private void hammerMode(ItemStack itemstack, Level world, BlockPos pos, Player player) {
        BlockState targetState = world.getBlockState(pos); // Checks if a block is an ore
        if (targetState.is(BlockTags.MINEABLE_WITH_PICKAXE) &&
            targetState.getTags().anyMatch(tag -> tag.location().getPath().contains("ores"))) {
            breakAdjacentBlocks(itemstack, world, pos, player, targetState, 0);
        }
        else { // If not is ore broken 5x5x5 area
            int radius = 1; // Break an area around of a block -> radius equals 2 that results on 5x5x5 area
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        BlockPos newPos = pos.offset(dx, dy, dz);
                        BlockState state = world.getBlockState(newPos);
                        if (!state.isAir() && !newPos.equals(pos)) {
                            state.getBlock().playerDestroy(world, player, newPos, state, world.getBlockEntity(newPos), itemstack);
                            world.removeBlock(newPos, false);
                            itemstack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
                        }
                    }
                }
            }
            itemstack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
        }
    }

    // CUSTOM METHOD - Recursive method that breaks down ores of the same type
    private void breakAdjacentBlocks(ItemStack itemstack, Level world, BlockPos pos,
                                     Player player, BlockState targetState, int depth) {
        if (depth > 50) { return; } // Prevents infinite loops
        world.removeBlock(pos, false);
        targetState.getBlock().playerDestroy(world, player, pos, targetState, world.getBlockEntity(pos), itemstack);
        itemstack.hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(player.getUsedItemHand()));
        for (BlockPos offset : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (world.getBlockState(offset).equals(targetState)) {
                breakAdjacentBlocks(itemstack, world, offset, player, targetState, depth + 1);
            }
        }
    }

    // CUSTOM METHOD - Auto Smelt mode method
    private void autoSmeltMode(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        double x = event.getPos().getX(), y = event.getPos().getY(), z = event.getPos().getZ();
        BlockPos pos = BlockPos.containing(x, y, z); // Player x, y, and z coordinates
        if (world instanceof Level level) { // Check if there is a casting recipe for the block
            ItemStack smeltResult = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING,
                      new SimpleContainer(new ItemStack(world.getBlockState(pos).getBlock())), level)
                      .map(recipe -> recipe.getResultItem(level.registryAccess()).copy()).orElse(ItemStack.EMPTY);
            if (!smeltResult.isEmpty()) {
                if (world instanceof ServerLevel serverLevel) { // Replaces the block with air and drops the molten item
                    ItemEntity entityToSpawn = new ItemEntity(serverLevel, x + 0.5, y + 0.5, z + 0.5,
                                                              smeltResult);
                    serverLevel.addFreshEntity(entityToSpawn);
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
            else { // Drop normal resources if there is no foundry revenue
                Block.dropResources(world.getBlockState(pos), world, pos, null);
                world.destroyBlock(pos, false);
            }
        }
    }

    // CUSTOM METHOD - Mores Ores mode method
    private void moreOresMode(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState blockState = world.getBlockState(pos); // Check if the block is STONE's tags and has a small chance to drop ores
        // If player breaks a STONE block and random is small of 20% receive some random stone ores
        if (blockState.is(Blocks.STONE) && Math.random() < 0.2 && world instanceof ServerLevel serverLevel) {
            for (int i = 0; i < 1; i++) { // Number of random ores are generated by block mined on any position
                ITagManager<Block> block = ForgeRegistries.BLOCKS.tags();
                if (block != null) { // Create a new ItemEntity with the randomly ORES's tags on randomOre
                    ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            new ItemStack(block.getTag(ModTags.Blocks.MORE_ORES_MODES_PICKAXE_DROPS)
                                               .getRandomElement(RandomSource.create()).orElse(Blocks.AIR)));
                    serverLevel.addFreshEntity(entityToSpawn); // All drops generated by block
                }
            }
        }
    }

    // CUSTOM METHOD - Magnetic mode method
    private void magneticMode(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && event.getState().getBlock() != Blocks.AIR) {
            Level world = player.level();
            ItemStack mainHandItem = player.getMainHandItem(); // Player has a tool on main hand
            event.setCanceled(true); // Prevents drop in the world = DEFAULT is dropped on the ground
            BlockPos pos = event.getPos(); // Block position = (X, Y, Z)
            BlockState state = event.getState(); // Block state = AIR
            // Drops are generated automatically on Player's inventory
            Block.getDrops(state, (ServerLevel) world, pos, null, player, mainHandItem)
                 .forEach(drop -> { // Blocks does added drop on Player's inventory
                        if (!player.getInventory().add(drop)) { player.drop(drop, false); } });
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()); // Prevents drop in the world
        }
    }
}