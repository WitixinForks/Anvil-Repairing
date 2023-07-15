package net.darkhax.anvilrepairing;

import net.minecraft.world.InteractionResult;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(AnvilRepairCommon.MODID)
public class AnvilRepairForge {

    public AnvilRepairForge() {

        MinecraftForge.EVENT_BUS.addListener(this::handleInteractEvent);
    }

    private void handleInteractEvent(final PlayerInteractEvent.RightClickBlock event) {

        if (AnvilRepairCommon.attemptAnvilRepair(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec()) == InteractionResult.SUCCESS) {

            event.setCanceled(true);
        }
    }
}