package net.darkhax.anvilrepairing;

import net.minecraft.world.InteractionResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@Mod(AnvilRepairCommon.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AnvilRepairNeoForge {

    @SubscribeEvent
    private static void handleInteractEvent(PlayerInteractEvent.RightClickBlock event) {

        if (AnvilRepairCommon.attemptAnvilRepair(event.getEntity(), event.getLevel(), event.getHand(), event.getHitVec()) == InteractionResult.SUCCESS) {

            event.setCanceled(true);
        }
    }
}