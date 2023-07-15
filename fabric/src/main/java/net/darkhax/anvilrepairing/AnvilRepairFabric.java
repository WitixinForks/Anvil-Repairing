package net.darkhax.anvilrepairing;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;

public class AnvilRepairFabric implements ModInitializer {

    @Override
    public void onInitialize() {

        UseBlockCallback.EVENT.register(AnvilRepairCommon::attemptAnvilRepair);
    }
}