package de.zonlykroks.flintlock_weaponry.flintlock_weaponry.entity;

import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.Flintlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class BulletEntity extends PersistentProjectileEntity {

    private final float damage;

    public BulletEntity(EntityType<? extends PersistentProjectileEntity> bulletEntityEntityType, World world) {
        super(bulletEntityEntityType,world);
        damage = 0;
    }

    public BulletEntity(World world, LivingEntity owner,float damage) {
        super(Flintlock.BULLET_ENTITY_TYPE, owner, world);
        this.damage = damage;
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult)
    {
        if(entityHitResult.getEntity() instanceof LivingEntity entity)
        {
            entity.damage(this.getDamageSources().arrow(this, this.getOwner() != null?this.getOwner():this), damage);
            entity.timeUntilRegen = 0;
        }
        this.discard();
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        this.discard();
    }

    @Override
    public void tick() {
        super.tick();
        this.getWorld().addParticle(ParticleTypes.SMOKE,this.getX(),this.getY(),this.getZ(),0,0.125F,0);
    }

    @Override
    protected ItemStack asItemStack() {
        return null;
    }
}
