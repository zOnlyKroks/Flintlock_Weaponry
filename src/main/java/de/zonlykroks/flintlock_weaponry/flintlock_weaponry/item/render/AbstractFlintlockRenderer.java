package de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item.render;

import de.zonlykroks.flintlock_weaponry.flintlock_weaponry.item.AbstractFlintlockWeapon;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AbstractFlintlockRenderer extends GeoItemRenderer<AbstractFlintlockWeapon> {
    public AbstractFlintlockRenderer(String id) {
        super(new DefaultedItemGeoModel<>(new Identifier("flintlock_weaponry", id)));
    }
}
