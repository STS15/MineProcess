package com.sts15.mineprocess.block.custom;

import com.sts15.mineprocess.block.entity.ModBlockEntities;
import com.sts15.mineprocess.block.entity.ArcFurnaceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class ArcFurnace extends BaseEntityBlock {
	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final BooleanProperty LIT = BooleanProperty.create("lit");

	public static final VoxelShape NORTH = Shapes.box(0, 0, 0, 1, 1, 1);
	public static final VoxelShape EAST = Shapes.box(0, 0, 0, 1, 1, 1);
	public static final VoxelShape SOUTH = Shapes.box(0, 0, 0, 1, 1, 1);
	public static final VoxelShape WEST = Shapes.box(0, 0, 0, 1, 1, 1);

	public ArcFurnace(Properties properties) {
		super(properties.lightLevel(state -> {
			return state.getValue(LIT) ? 4 : 0;
		}));

		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
			case EAST:
				return EAST;
			case SOUTH:
				return SOUTH;
			case WEST:
				return WEST;
			default:
				return NORTH;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, LIT);
		super.createBlockStateDefinition(builder);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	@Override
	public BlockState rotate(BlockState state, LevelAccessor world, BlockPos pos, Rotation direction) {
		return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
	}


	// BLOCK ENTITY BELOW V


	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pState.getBlock() != pNewState.getBlock()) {
			BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
			if (blockEntity instanceof ArcFurnaceBlockEntity) {
				((ArcFurnaceBlockEntity) blockEntity).drops();
			}
		}
		super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
	}

	@Override
	public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
								 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
		if (!pLevel.isClientSide()) {
			BlockEntity entity = pLevel.getBlockEntity(pPos);
			if(entity instanceof ArcFurnaceBlockEntity) {
				NetworkHooks.openScreen(((ServerPlayer)pPlayer), (ArcFurnaceBlockEntity)entity, pPos);
			} else {
				throw new IllegalStateException("Our Container provider is missing!");
			}
		}

		return InteractionResult.sidedSuccess(pLevel.isClientSide());
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ArcFurnaceBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
																  BlockEntityType<T> type) {
		return createTickerHelper(type, ModBlockEntities.ARC_FURNACE.get(),
				ArcFurnaceBlockEntity::tick);
	}


}
