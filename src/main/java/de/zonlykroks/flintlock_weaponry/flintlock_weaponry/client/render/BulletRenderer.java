package de.zonlykroks.flintlock_weaponry.flintlock_weaponry.client.render;

import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.entity.BulletEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class BulletRenderer extends EntityRenderer<BulletEntity> {

    public BulletRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public Identifier getTexture(BulletEntity entity) {
        return null;
    }

    @Override
    public void render(BulletEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
    {
    }
}
