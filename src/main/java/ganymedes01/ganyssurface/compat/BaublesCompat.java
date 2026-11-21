package ganymedes01.ganyssurface.compat;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.FMLLog;
import ganymedes01.ganyssurface.items.Quiver;
import ganymedes01.ganyssurface.items.QuiverBauble;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Gany's Surface
 *
 * Baubles bridge. Never touch this class unless {@code Loader.isModLoaded("Baubles")} is true,
 * otherwise the JVM tries to load the Baubles API classes that are not there.
 *
 * @author ganymedes01
 *
 */

public final class BaublesCompat {

	private BaublesCompat() {
	}

	public static Item createQuiver() {
		FMLLog.fine("[ganyssurface] Baubles detected - the quiver will accept the quiver/cape/body slots");
		return new QuiverBauble();
	}

	public static ItemStack getEquippedQuiver(EntityPlayer player) {
		IInventory baubles = BaublesApi.getBaubles(player);
		if (baubles == null)
			return null;

		for (int i = 0; i < baubles.getSizeInventory(); i++) {
			ItemStack stack = baubles.getStackInSlot(i);
			if (stack != null && stack.getItem() instanceof Quiver)
				return stack;
		}

		return null;
	}
}
