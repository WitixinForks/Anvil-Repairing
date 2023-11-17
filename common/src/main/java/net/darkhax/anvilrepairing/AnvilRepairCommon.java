package net.darkhax.anvilrepairing;

import net.minecraft.ResourceLocationException;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class AnvilRepairCommon {

    public static final String MODID = "anvilrepairing";
    private static final ResourceLocation ADVANCEMENT_ID = new ResourceLocation(MODID, "story/repair");
    public static final TagKey<Item> ANVIL_REPAIR_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MODID, "anvil_repair_items"));

    public static InteractionResult attemptAnvilRepair(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {

        if (!level.isClientSide && !player.isSpectator()) {

            final BlockPos pos = hitResult.getBlockPos();
            final BlockState blockState = level.getBlockState(pos);

            if (isRepairable(blockState) && player.getItemInHand(hand).is(ANVIL_REPAIR_ITEMS)) {

                if (!player.getAbilities().instabuild) {

                    player.getItemInHand(hand).shrink(1);
                }

                if (player instanceof ServerPlayer serverPlayer) {

                    awardAdvancement(serverPlayer, ADVANCEMENT_ID);
                }

                repairAnvil(level, pos, blockState.getBlock(), blockState.getValue(AnvilBlock.FACING));
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    private static boolean isRepairable(BlockState state) {

        return state.is(Blocks.CHIPPED_ANVIL) || state.is(Blocks.DAMAGED_ANVIL);
    }

    private static void repairAnvil(Level level, BlockPos pos, Block block, Direction direction) {

        if (block == Blocks.CHIPPED_ANVIL) {

            level.setBlock(pos, Blocks.ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, direction), Block.UPDATE_CLIENTS);
            level.levelEvent(LevelEvent.SOUND_ANVIL_USED, pos, 0);
        }

        else if (block == Blocks.DAMAGED_ANVIL) {

            level.setBlock(pos, Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(AnvilBlock.FACING, direction), Block.UPDATE_CLIENTS);
            level.levelEvent(LevelEvent.SOUND_ANVIL_USED, pos, 0);
        }
    }

    private static void awardAdvancement(ServerPlayer player, ResourceLocation advancementId) {

        final AdvancementHolder toGrant = player.getServer().getAdvancements().get(advancementId);

        if (toGrant != null) {

            final AdvancementProgress progress = player.getAdvancements().getOrStartProgress(toGrant);

            if (!progress.isDone()) {

                for (String remainingCriteria : progress.getRemainingCriteria()) {

                    player.getAdvancements().award(toGrant, remainingCriteria);
                }
            }
        }

        else {

            throw new ResourceLocationException("No advancement found for ID: " + advancementId);
        }
    }
}
