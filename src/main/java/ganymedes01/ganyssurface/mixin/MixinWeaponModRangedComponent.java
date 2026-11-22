package ganymedes01.ganyssurface.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ckathode.weaponmod.item.RangedComponent;
import ganymedes01.ganyssurface.compat.WeaponModCompat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Gany's Surface
 *
 * Lets the quiver feed Balkon's Weapon Mod - the heavy crossbow's bolts in particular.
 *
 * <p>
 * That mod never asks {@code InventoryPlayer}: its ranged weapons scan the inventory through
 * {@code WMItem.findAnyItemSlot} and spend ammo from {@code mainInventory} directly, so the
 * {@code InventoryPlayer} mixin that serves vanilla-style bows cannot reach them. What every one of
 * its ranged weapons does share, though, is {@code RangedComponent}:
 *
 * <ul>
 * <li>{@code hasAmmo} decides whether right-clicking starts a reload at all;</li>
 * <li>{@code hasAmmoAndConsume} spends one round when the shot is released.</li>
 * </ul>
 *
 * <p>
 * Both already consult the weapon's own ammo list ({@code getAmmoItems()} - crossbow bolts,
 * musket bullets, shotgun shot, mortar shells, blowgun darts), so the quiver only has to answer
 * when the inventory has none. The mixin is registered from {@code LateMixins} and only when
 * {@code weaponmod} is installed; all the reflection lives in {@link WeaponModCompat}.
 */
@Mixin(RangedComponent.class)
public class MixinWeaponModRangedComponent {

	@Inject(method = "hasAmmo", at = @At("RETURN"), cancellable = true, remap = false)
	private void ganyssurface$hasAmmoInQuiver(ItemStack stack, World world, EntityPlayer player, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() || player == null)
			return;

		if (WeaponModCompat.hasAmmoInQuiver(player, this))
			cir.setReturnValue(true);
	}

	@Inject(method = "hasAmmoAndConsume", at = @At("RETURN"), cancellable = true, remap = false)
	private void ganyssurface$consumeQuiverAmmo(ItemStack stack, World world, EntityLivingBase entity, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ() || !(entity instanceof EntityPlayer))
			return;

		if (WeaponModCompat.consumeAmmoFromQuiver((EntityPlayer) entity, this))
			cir.setReturnValue(true);
	}
}
