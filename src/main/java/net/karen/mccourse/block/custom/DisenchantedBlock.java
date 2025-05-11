package net.karen.mccourse.block.custom;

import io.netty.buffer.Unpooled;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.entity.DisenchantedBlockEntity;
import net.karen.mccourse.screen.DisenchantedMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

public class DisenchantedBlock extends Block implements EntityBlock {
	public DisenchantedBlock(Properties pProperties) { super(pProperties); }

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target,
									   BlockGetter world, BlockPos pos, Player player) {
		return new ItemStack(this);
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState blockstate, @NotNull Level world,
										  @NotNull BlockPos pos, @NotNull Player entity,
										  @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		if (entity instanceof ServerPlayer player) {
			NetworkHooks.openScreen(player, new MenuProvider() {
				@Override
				public @NotNull Component getDisplayName() { return Component.literal("Disenchant"); }

				@Override
				public AbstractContainerMenu createMenu(int id, @NotNull Inventory inventory,
														@NotNull Player player) {
					return new DisenchantedMenu(id, inventory,
							new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
				}
			}, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public MenuProvider getMenuProvider(@NotNull BlockState state, Level worldIn, @NotNull BlockPos pos) {
		BlockEntity tileEntity = worldIn.getBlockEntity(pos);
		return tileEntity instanceof MenuProvider menuProvider ? menuProvider : null;
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new DisenchantedBlockEntity(pos, state);
	}

	@Override
	public boolean triggerEvent(@NotNull BlockState state, @NotNull Level world,
								@NotNull BlockPos pos, int eventID, int eventParam) {
		super.triggerEvent(state, world, pos, eventID, eventParam);
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity != null && blockEntity.triggerEvent(eventID, eventParam);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
									   boolean willHarvest, FluidState fluid) {
		if (state.getBlock().defaultBlockState().is(ModBlocks.DISENCHANTED_BLOCK.get())) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity instanceof DisenchantedBlockEntity be) {
				Containers.dropContents(level, pos, be);
				level.updateNeighbourForOutputSignal(pos, this);
			}
		}
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
}