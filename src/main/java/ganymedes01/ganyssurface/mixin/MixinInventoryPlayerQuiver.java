package ganymedes01.ganyssurface.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ganymedes01.ganyssurface.items.Quiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

/**
 * Gany's Surface
 *
 * Makes the quiver's plain arrows answer the two questions every bow asks the inventory.
 *
 * <p>
 * The quiver only ever hooked the vanilla bow, through {@code ArrowNockEvent} /
 * {@code ArrowLooseEvent}. Modded bows never post those events, or post them and then do
 * their own arrow accounting, so they used to either refuse to draw or get their shot
 * replaced by a plain vanilla arrow. But all of them - Twilight Forest's four bows, Natura,
 * Ender Zoo, Draconic Evolution, Mekanism's electric bow, RiftFlux's More Bows and Searing
 * Bow, and anything else built on {@code ItemBow} - do go through exactly these two
 * {@link InventoryPlayer} methods:
 *
 * <ul>
 * <li>{@code hasItem(Items.arrow)} - "may I draw / fire?"</li>
 * <li>{@code consumeInventoryItem(Items.arrow)} - "spend one arrow"</li>
 * </ul>
 *
 * <p>
 * So instead of emulating every bow, the quiver answers those two questions for plain arrows.
 * The bow keeps its own arrow entity (triple shot, homing, freezing, ender teleport, ...) and
 * the quiver is simply its ammo. Tipped arrows are deliberately not counted here: a modded bow
 * would fire them as a plain {@code EntityArrow} and silently lose the potion. They are handled
 * one level up instead, in the mod's {@code ArrowLooseEvent} hook, which fires Et Futurum's
 * {@code EntityTippedArrow} for every bow that posts that event (see {@code MiscEventHandler}).
 *
 * <p>
 * Both injections are no-ops unless the player is actually carrying a quiver with plain arrows,
 * and {@code consumeInventoryItem} still lets the vanilla lookup win when a real arrow is in the
 * inventory, so the priority order is unchanged: inventory first, quiver second.
 */
@Mixin(InventoryPlayer.class)
public class MixinInventoryPlayerQuiver {

	@Inject(method = "hasItem(Lnet/minecraft/item/Item;)Z", at = @At("HEAD"), cancellable = true)
	private void ganyssurface$hasQuiverArrow(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item != Items.arrow)
			return;

		EntityPlayer player = ((InventoryPlayer) (Object) this).player;
		if (player != null && Quiver.hasQuiverPlainArrow(player))
			cir.setReturnValue(true);
	}

	@Inject(method = "consumeInventoryItem(Lnet/minecraft/item/Item;)Z", at = @At("HEAD"), cancellable = true)
	private void ganyssurface$consumeQuiverArrow(Item item, CallbackInfoReturnable<Boolean> cir) {
		if (item != Items.arrow)
			return;

		EntityPlayer player = ((InventoryPlayer) (Object) this).player;
		if (player == null || Quiver.hasInventoryArrow(player))
			return; // a real arrow is there - the vanilla lookup consumes that one

		if (Quiver.consumeQuiverPlainArrow(player))
			cir.setReturnValue(true);
	}
}
