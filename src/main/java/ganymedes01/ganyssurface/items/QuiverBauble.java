package ganymedes01.ganyssurface.items;

import baubles.api.BaubleType;
import baubles.api.expanded.BaubleExpandedSlots;
import baubles.api.expanded.IBaubleExpanded;
import ganymedes01.ganyssurface.items.Quiver;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

/**
 * Gany's Surface
 *
 * The quiver as a Baubles-Expanded bauble. Baubles-Expanded has a dedicated quiver slot, and the
 * quiver is also allowed in the back/cape slot so it can be worn there when no quiver slot is
 * assigned. Only loaded when the mod is present.
 *
 * @author ganymedes01
 *
 */

public class QuiverBauble extends Quiver implements IBaubleExpanded {

	@Override
	public String[] getBaubleTypes(ItemStack stack) {
		return new String[] { BaubleExpandedSlots.quiverType, BaubleExpandedSlots.capeType };
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.UNIVERSAL;
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase player) {
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
	}

	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		return true;
	}
}
