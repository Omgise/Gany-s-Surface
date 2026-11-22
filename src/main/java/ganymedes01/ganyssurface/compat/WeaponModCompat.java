package ganymedes01.ganyssurface.compat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ganymedes01.ganyssurface.items.Quiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Gany's Surface
 *
 * Balkon's Weapon Mod bridge. The quiver accepts that mod's ranged ammo (the heavy crossbow's
 * bolts, and the bullets / shot / shells / darts of its other guns) and
 * {@code MixinWeaponModRangedComponent} lets those weapons spend it straight out of the quiver.
 *
 * Everything here is reflective, so the mod loads without the Weapon Mod installed. The ammo
 * lists come from the weapons themselves ({@code RangedComponent.getAmmoItems()}), which resolves
 * its registry names lazily - that is why the set is collected on first use, not at class load.
 *
 * @author ganymedes01
 *
 */

public final class WeaponModCompat {

	private static boolean initialised;
	private static Class<?> shooterClass;
	private static Field rangedComponentField;
	private static Method getAmmoItems;
	private static Set<Item> ammoTypes;

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

	/** True for items any installed Weapon Mod ranged weapon accepts as ammo. */
	public static boolean isAmmoItem(ItemStack stack) {
		if (stack == null)
			return false;

		init();
		if (getAmmoItems == null)
			return false;
		if (ammoTypes == null)
			ammoTypes = collectAmmoTypes();
		return ammoTypes.contains(stack.getItem());
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

	private static Set<Item> collectAmmoTypes() {
		Set<Item> types = new HashSet<Item>();

		for (Object entry : Item.itemRegistry)
			if (shooterClass.isInstance(entry))
				try {
					types.addAll(asItems(getAmmoItems.invoke(rangedComponentField.get(entry))));
				} catch (Throwable e) {
					// one broken weapon must not take the whole list down
				}

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
