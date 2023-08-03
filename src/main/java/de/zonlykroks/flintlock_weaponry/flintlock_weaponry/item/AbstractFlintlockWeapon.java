package de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item;

import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.Flintlock;
import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.entity.BulletEntity;
import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item.render.AbstractFlintlockRenderer;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rarity;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.RenderProvider;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class AbstractFlintlockWeapon extends Item implements GeoItem {

    public boolean loaded;

    private final String geoItemId;
    private final int reloadCoolDown,fireCooldown;
    private final float damage;

    private final AnimatableInstanceCache animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);
    private final Supplier<Object> renderProvider = GeoItem.makeRenderer(this);

    public AbstractFlintlockWeapon(String geoItemId, int reloadCoolDown, int fireCooldown,float damage) {
        super(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        this.geoItemId = geoItemId;
        this.reloadCoolDown = reloadCoolDown;
        this.fireCooldown = fireCooldown;
        this.damage = damage;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0,playstate -> PlayState.STOP)
                .triggerableAnim("fire", RawAnimation.begin().thenPlay("weapon.fire"))
                .triggerableAnim("reload", RawAnimation.begin().thenPlay("weapon.reload")));
    }

    @Override
    public TypedActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if(player.getItemCooldownManager().isCoolingDown(this)) {
            return TypedActionResult.fail(stack);
        }

        loaded = stack.getOrCreateNbt().getBoolean("loaded");

        if (!level.isClient && loaded) {
            BulletEntity bulletEntity = new BulletEntity(level, player,damage);

            bulletEntity.setVelocity(player, player.getRotationClient().x, player.getRotationClient().y, 0, 5.0F, 1.5F);
            bulletEntity.setNoGravity(true);

            level.spawnEntity(bulletEntity);

            level.playSound(null,player.getX(),player.getY(),player.getZ(),Flintlock.PISTOL_FIRE,SoundCategory.MASTER,10.0F,0.0F);
            triggerAnim(player, GeoItem.getOrAssignId(stack, (ServerWorld) level), "controller", "fire");

            Random rand = new SecureRandom();

            player.addVelocity(rand.nextFloat(0.15F) + 0.5F,rand.nextFloat(0.15F) + 0.5F,rand.nextFloat(0.15F) + 0.5F);

            stack.getNbt().putBoolean("loaded",false);
            player.getItemCooldownManager().set(this,fireCooldown);
            return TypedActionResult.pass(stack);
        }else if(!level.isClient){
            reload(player,level,stack);
        }

        return TypedActionResult.fail(stack);
    }

    public void reload(PlayerEntity playerEntity, World level,ItemStack stack) {
        boolean hasAmmo = playerEntity.getInventory().contains(Flintlock.ammo.getDefaultStack());

        if(!hasAmmo) return;

        ItemStack bulletStack = playerEntity.getInventory().getStack(playerEntity.getInventory().getSlotWithStack(Flintlock.ammo.getDefaultStack()));

        bulletStack.decrement(1);

        playerEntity.getItemCooldownManager().set(stack.getItem(),reloadCoolDown);
        loaded = true;
        stack.getNbt().putBoolean("loaded",true);
        triggerAnim(playerEntity, GeoItem.getOrAssignId(stack, (ServerWorld) level), "controller", "reload");
        level.playSound(null,playerEntity.getX(),playerEntity.getY(),playerEntity.getZ(),Flintlock.PISTOL_RELOAD,SoundCategory.MASTER,10.0F,0.0F);
    }

    @Override
    public void createRenderer(Consumer<Object> consumer) {
        consumer.accept(new RenderProvider() {

            @Override
            public BuiltinModelItemRenderer getCustomRenderer() {
                return new AbstractFlintlockRenderer(geoItemId);
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animatableInstanceCache;
    }

    @Override
    public Supplier<Object> getRenderProvider() {
        return this.renderProvider;
    }
}
