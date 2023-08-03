package de.zonlykroks.flintlock_weaponry.flintlock_weaponry;

import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.entity.BulletEntity;
import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item.AbstractFlintlockWeapon;
import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item.impl.FlintlockPistol;
import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item.impl.FlintlockRifle;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.lwjgl.glfw.GLFW;

public class Flintlock implements ModInitializer {

    public static final Identifier PISTOL_RELOAD_ID = new Identifier("flintlock_weaponry:pistol_reload");
    public static SoundEvent PISTOL_RELOAD = SoundEvent.of(PISTOL_RELOAD_ID);

    public static final Identifier PISTOL_FIRE_ID = new Identifier("flintlock_weaponry:pistol_fire");
    public static SoundEvent PISTOL_FIRE = SoundEvent.of(PISTOL_FIRE_ID);

    public static final EntityType<BulletEntity> BULLET_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            new Identifier("flintlock_weaponry", "bullet"),
            FabricEntityTypeBuilder.<BulletEntity>create(SpawnGroup.MISC, BulletEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25F, 0.25F))
                    .trackRangeBlocks(4).trackedUpdateRate(10)
                    .build()
    );

    public static final Item ammo = Registry.register(Registries.ITEM,new Identifier("flintlock_weaponry","ammo"), new Item(new Item.Settings().rarity(Rarity.COMMON).maxCount(16)));

    public static final Item flintlockPistol = new FlintlockPistol();
    public static final Item flintlockRifle = new FlintlockRifle();

    private static final ItemGroup ITEM_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(flintlockPistol))
            .displayName(Text.translatable("itemGroup.flintlock_weaponry.creative_group"))
            .build();

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM, new Identifier("flintlock_weaponry", "flintlock_pistol"), flintlockPistol);
        Registry.register(Registries.ITEM, new Identifier("flintlock_weaponry", "flintlock_rifle"), flintlockRifle);
        Registry.register(Registries.ITEM_GROUP, new Identifier("flintlock_weaponry", "creative_group"), ITEM_GROUP);

        Registry.register(Registries.SOUND_EVENT, PISTOL_FIRE_ID, PISTOL_FIRE);
        Registry.register(Registries.SOUND_EVENT, PISTOL_RELOAD_ID, PISTOL_RELOAD);

        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            if(group.equals(ITEM_GROUP)) {
                entries.add(flintlockPistol);
                entries.add(ammo);
                entries.add(flintlockRifle);
            }
        });
    }
}
