package de.zonlykroks.flintlock_weaponry.flintlock_weaponry.client;

import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.Flintlock;
import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.client.render.BulletRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class FlintlockClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Flintlock.BULLET_ENTITY_TYPE, BulletRenderer::new);
    }
}
