package ganymedes01.ganyssurface.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.ganyssurface.items.Quiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Gany's Surface
 *
 * Balkon's Weapon Mod bridge, for the heavy crossbow only: the quiver accepts crossbow bolts and
 * {@code MixinWeaponModRangedComponent} lets the crossbow spend them straight out of the quiver.
 *
 * <p>
 * Only the crossbow's own ammo list is exposed, not the union of every ranged weapon's: the other
 * guns have their own cartridges (musket bullets, shotgun shot, mortar shells, blowgun darts) and
 * those are deliberately kept out of the quiver. The list is read from the crossbow item itself
 * ({@code RangedComponent.getAmmoItems()}) so a config that renames or disables it still works.
 *
 * <p>
 * Everything here is reflective, so the mod loads without the Weapon Mod installed. The ammo list
 * resolves its registry names lazily - that is why it is collected on first use, not at class load.
 *
 * @author ganymedes01
 *
 */

public final class WeaponModCompat {

	private static boolean initialised;
	private static Class<?> shooterClass;
	private static Field rangedComponentField;
	private static Method getAmmoItems;
	private static Set<Item> boltTypes;

	private WeaponModCompat() {
	}

	private static void init() {
		if (initialised)
			return;
		initialised = true;

		try {
			shooterClass = Class.forName("ckathode.weaponmod.item.ItemShooter");
			// The field, not getRangedComponent(): Class#getMethod resolves every public signature of
			// ItemShooter, and that class has client-only ones (IIconRegister), which a dedicated
			// server cannot load. Field lookup only touches the field's own type.
			rangedComponentField = shooterClass.getField("rangedComponent");
			Class<?> component = Class.forName("ckathode.weaponmod.item.RangedComponent");
			getAmmoItems = component.getMethod("getAmmoItems");
		} catch (Throwable e) {
			shooterClass = null;
			rangedComponentField = null;
			getAmmoItems = null;
		}
	}

	public static boolean isPresent() {
		init();
		return getAmmoItems != null;
	}

	/** True for the items the heavy crossbow accepts as ammo - i.e. crossbow bolts. */
	public static boolean isAmmoItem(ItemStack stack) {
		if (stack == null)
			return false;

		init();
		if (getAmmoItems == null)
			return false;

		return crossbowAmmo().contains(stack.getItem());
	}

	/** Ammo a specific ranged component accepts (used by the quiver behind the mixin). */
	public static List<Item> ammoItems(Object component) {
		init();
		if (getAmmoItems == null || component == null)
			return Collections.emptyList();

		try {
			return asItems(getAmmoItems.invoke(component));
		} catch (Throwable e) {
			return Collections.emptyList();
		}
	}

	/** Does the quiver hold ammo this ranged weapon accepts? */
	public static boolean hasAmmoInQuiver(EntityPlayer player, Object component) {
		return Quiver.hasQuiverAmmo(player, ammoItems(component));
	}

	/** Spend one round of this ranged weapon's ammo from the quiver. */
	public static boolean consumeAmmoFromQuiver(EntityPlayer player, Object component) {
		return Quiver.consumeQuiverAmmo(player, ammoItems(component));
	}

	private static Set<Item> crossbowAmmo() {
		if (boltTypes != null)
			return boltTypes;

		Set<Item> types = new HashSet<Item>();
		try {
			Object crossbow = GameRegistry.findItem("weaponmod", "crossbow");
			if (crossbow != null && shooterClass.isInstance(crossbow))
				types.addAll(asItems(getAmmoItems.invoke(rangedComponentField.get(crossbow))));
		} catch (Throwable e) {
			// fall through: an empty set is never cached, so a later call can still succeed
		}

		if (!types.isEmpty())
			boltTypes = types;
		return types;
	}

	private static List<Item> asItems(Object ammo) {
		if (!(ammo instanceof List))
			return Collections.emptyList();

		List<Item> items = new ArrayList<Item>();
		for (Object entry : (List<?>) ammo)
			if (entry instanceof Item)
				items.add((Item) entry);
		return items;
	}
}
