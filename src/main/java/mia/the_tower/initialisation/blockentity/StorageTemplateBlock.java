//package mia.the_tower.initialisation.blockentity;
//
//
//import com.mojang.serialization.MapCodec;
//import net.minecraft.block.Block;
//import net.minecraft.block.BlockState;
//import net.minecraft.block.BlockWithEntity;
//import net.minecraft.block.entity.BlockEntity;
//import net.minecraft.block.entity.BlockEntityTicker;
//import net.minecraft.block.entity.BlockEntityType;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemPlacementContext;
//import net.minecraft.screen.ScreenHandler;
//import net.minecraft.server.world.ServerWorld;
//import net.minecraft.state.StateManager;
//import net.minecraft.state.property.BooleanProperty;
//import net.minecraft.state.property.EnumProperty;
//import net.minecraft.state.property.Properties;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.BlockMirror;
//import net.minecraft.util.BlockRotation;
//import net.minecraft.util.ItemScatterer;
//import net.minecraft.util.hit.BlockHitResult;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.Direction;
//import net.minecraft.util.math.random.Random;
//import net.minecraft.world.World;
//import org.jetbrains.annotations.Nullable;
//
//
//public class StorageTemplateBlock extends BlockWithEntity {
//    public static final MapCodec<StorageTemplateBlock> CODEC = createCodec(StorageTemplateBlock::new);
//    public static final EnumProperty<Direction> FACING = Properties.FACING;
//    public static final BooleanProperty OPEN = Properties.OPEN;
//
//    @Override
//    public MapCodec<StorageTemplateBlock> getCodec() {
//        return CODEC;
//    }
//
//    public StorageTemplateBlock(Settings settings) {
//        super(settings);
//        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
//    }
//
//    @Override
//    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
//        if (world instanceof ServerWorld serverWorld && world.getBlockEntity(pos) instanceof StorageTemplateBlockEntity storageTemplateBlockEntity) {
//            player.openHandledScreen(storageTemplateBlockEntity);
//            //player.incrementStat(Stats.OPEN_BARREL);
//        }
//
//        return ActionResult.SUCCESS;
//    }
//
//    @Override
//    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
//        ItemScatterer.onStateReplaced(state, newState, world, pos);
//        super.onStateReplaced(state, world, pos, newState, moved);
//    }
//
//    @Override
//    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        BlockEntity blockEntity = world.getBlockEntity(pos);
//        if (blockEntity instanceof StorageTemplateBlockEntity) {
//            ((StorageTemplateBlockEntity)blockEntity).tick();
//        }
//    }
//
//    @Nullable
//    @Override
//    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
//        return new StorageTemplateBlockEntity(pos, state);
//    }
//
//    @Override
//    protected boolean hasComparatorOutput(BlockState state) {
//        return true;
//    }
//
//    @Override
//    protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
//        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
//    }
//
//    @Override
//    protected BlockState rotate(BlockState state, BlockRotation rotation) {
//        return state.with(FACING, rotation.rotate(state.get(FACING)));
//    }
//
//    @Override
//    protected BlockState mirror(BlockState state, BlockMirror mirror) {
//        return state.rotate(mirror.getRotation(state.get(FACING)));
//    }
//
//    @Override
//    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
//        builder.add(FACING, OPEN);
//    }
//
//    @Override
//    public BlockState getPlacementState(ItemPlacementContext ctx) {
//        return this.getDefaultState().with(FACING, ctx.getPlayerLookDirection().getOpposite());
//    }
//
//}
//

