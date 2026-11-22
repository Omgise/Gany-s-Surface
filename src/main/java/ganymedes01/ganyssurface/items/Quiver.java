package ganymedes01.ganyssurface.items;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.IConfigurable;
import ganymedes01.ganyssurface.api.IQuiver;
import ganymedes01.ganyssurface.compat.BaublesCompat;
import ganymedes01.ganyssurface.compat.ModCompat;
import ganymedes01.ganyssurface.compat.EtFuturumCompat;
import ganymedes01.ganyssurface.core.utils.InventoryUtils;
import ganymedes01.ganyssurface.core.utils.Utils;
import ganymedes01.ganyssurface.lib.Strings;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class Quiver extends Item implements IQuiver, IConfigurable {

	// Plain arrows only need a counter (stored in the item damage), but arrows that carry data
	// (Et Futurum's tipped arrows keep their potion in the stack) are stored as item stacks.
	private static final String TAG_TIPPED = "TippedArrows";

	@SideOnly(Side.CLIENT)
	private IIcon empty;

	@SideOnly(Side.CLIENT)
	private IIcon full;

	public static int ARROW_STACK_SIZE = 64;

	public Quiver() {
		setFull3D();
		setMaxStackSize(1);
		setUnlocalizedName(Utils.getUnlocalisedName(Strings.QUIVER_NAME));
		setCreativeTab(GanysSurface.enableQuiver ? GanysSurface.surfaceTab : null);
	}

	public static int getCapacity() {
		return ARROW_STACK_SIZE * 4;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (player.isSneaking())
			dumpArrows(stack, player);
		else
			absorbArrows(stack, player);
		return stack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean flag) {
		list.add(StatCollector.translateToLocal(Utils.getString("quiver")));

		List<ItemStack> tipped = readTipped(stack);
		int plain = getPlainArrowCount(stack);

		// An empty quiver still shows "0 x Arrow" so the tooltip never looks broken.
		if (plain > 0 || tipped.isEmpty())
			list.add(StatCollector.translateToLocalFormatted(Utils.getString("quiver.line"), plain, StatCollector.translateToLocal("item.arrow.name")));

		for (ItemStack arrow : tipped)
			list.add(StatCollector.translateToLocalFormatted(Utils.getString("quiver.line"), arrow.stackSize, arrow.getDisplayName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister reg) {
		// Two states, two textures: the mod's own empty quiver, and the vanilla-domain "quiver"
		// that texture packs (Modernity in GTNH) draw with arrows sticking out. Without such a
		// pack "minecraft:quiver" resolves to missingno, in which case both states use ours.
		empty = reg.registerIcon(Utils.getItemTexture(Strings.QUIVER_NAME));

		IIcon packed = reg.registerIcon("minecraft:" + Strings.QUIVER_NAME);
		full = packed == null || "missingno".equals(packed.getIconName()) ? empty : packed;

		itemIcon = empty;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		return meta <= 0 ? empty : full;
	}

	@Override
	public void setArrowAmount(ItemStack stack, int amount) {
		stack.setItemDamage(Math.max(0, Math.min(amount, getMaxArrowCount(stack))));
	}

	@Override
	public int getArrowAmount(ItemStack stack) {
		return Math.max(0, stack.getItemDamage());
	}

	@Override
	public int getMaxArrowCount(ItemStack stack) {
		return getCapacity();
	}

	public static int getArrowCount(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof IQuiver)
			return ((IQuiver) stack.getItem()).getArrowAmount(stack);
		return 0;
	}

	public static void setArrowCount(ItemStack stack, int count) {
		if (stack != null && stack.getItem() instanceof IQuiver)
			((IQuiver) stack.getItem()).setArrowAmount(stack, count);
	}

	// ---------------------------------------------------------------- arrows

	public static boolean isArrow(ItemStack stack) {
		if (stack == null)
			return false;
		return stack.getItem() == Items.arrow || EtFuturumCompat.isTippedArrow(stack);
	}

	public static boolean isPlainArrow(ItemStack stack) {
		return stack != null && stack.getItem() == Items.arrow;
	}

	/** Plain arrows are whatever the total does not account for. */
	public static int getPlainArrowCount(ItemStack stack) {
		return Math.max(0, getArrowCount(stack) - getTippedCount(stack));
	}

	public static int getTippedCount(ItemStack stack) {
		NBTTagList list = getTippedList(stack);
		if (list == null)
			return 0;

		int total = 0;
		for (int i = 0; i < list.tagCount(); i++) {
			ItemStack arrow = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
			if (arrow != null)
				total += arrow.stackSize;
		}
		return total;
	}

	private static NBTTagList getTippedList(ItemStack stack) {
		if (stack == null || stack.getTagCompound() == null)
			return null;
		NBTTagList list = stack.getTagCompound().getTagList(TAG_TIPPED, 10);
		return list == null || list.tagCount() == 0 ? null : list;
	}

	private static void setTippedList(ItemStack stack, NBTTagList list) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag == null) {
			tag = new NBTTagCompound();
			stack.setTagCompound(tag);
		}

		if (list == null || list.tagCount() == 0) {
			tag.removeTag(TAG_TIPPED);
			if (tag.hasNoTags())
				stack.setTagCompound(null);
		} else
			tag.setTag(TAG_TIPPED, list);
	}

	private static List<ItemStack> readTipped(ItemStack stack) {
		List<ItemStack> arrows = new ArrayList<ItemStack>();
		NBTTagList list = getTippedList(stack);
		if (list != null)
			for (int i = 0; i < list.tagCount(); i++) {
				ItemStack arrow = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
				if (arrow != null && arrow.stackSize > 0)
					arrows.add(arrow);
			}
		return arrows;
	}

	private static void writeTipped(ItemStack stack, List<ItemStack> arrows) {
		NBTTagList list = new NBTTagList();
		if (arrows != null)
			for (ItemStack arrow : arrows)
				if (arrow != null && arrow.stackSize > 0) {
					NBTTagCompound entry = new NBTTagCompound();
					arrow.writeToNBT(entry);
					list.appendTag(entry);
				}
		setTippedList(stack, list);
	}

	private static void addTipped(ItemStack quiver, ItemStack arrow, int amount) {
		List<ItemStack> arrows = readTipped(quiver);
		for (ItemStack existing : arrows)
			if (existing.isItemEqual(arrow) && ItemStack.areItemStackTagsEqual(existing, arrow)) {
				existing.stackSize += amount;
				writeTipped(quiver, arrows);
				return;
			}

		ItemStack copy = arrow.copy();
		copy.stackSize = amount;
		arrows.add(copy);
		writeTipped(quiver, arrows);
	}

	private static ItemStack takeTipped(ItemStack quiver) {
		List<ItemStack> arrows = readTipped(quiver);
		if (arrows.isEmpty())
			return null;

		ItemStack first = arrows.get(0);
		ItemStack taken = first.copy();
		taken.stackSize = 1;

		if (--first.stackSize <= 0)
			arrows.remove(0);
		writeTipped(quiver, arrows);
		return taken;
	}

	/** Returns what the next shot would consume, without removing it. */
	public static ItemStack peekArrow(ItemStack quiver) {
		if (getArrowCount(quiver) <= 0)
			return null;

		if (getTippedCount(quiver) > 0) {
			List<ItemStack> arrows = readTipped(quiver);
			if (!arrows.isEmpty()) {
				ItemStack arrow = arrows.get(0).copy();
				arrow.stackSize = 1;
				return arrow;
			}
		}

		return new ItemStack(Items.arrow);
	}

	/** Removes one arrow from the quiver and returns what should be shot. */
	public static ItemStack consumeArrow(ItemStack quiver) {
		int total = getArrowCount(quiver);
		if (total <= 0)
			return null;

		if (getTippedCount(quiver) > 0) {
			ItemStack arrow = takeTipped(quiver);
			if (arrow != null) {
				setArrowCount(quiver, total - 1);
				return arrow;
			}
		}

		setArrowCount(quiver, total - 1);
		return new ItemStack(Items.arrow);
	}

	public static ItemStack findQuiverWithArrows(EntityPlayer player) {
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (getArrowCount(stack) > 0)
				return stack;
		}

		if (ModCompat.baubles()) {
			ItemStack stack = BaublesCompat.getEquippedQuiver(player);
			if (getArrowCount(stack) > 0)
				return stack;
		}

		return null;
	}

	public static int countArrows(EntityPlayer player) {
		int total = 0;
		for (int i = 0; i < player.inventory.getSizeInventory(); i++)
			total += getArrowCount(player.inventory.getStackInSlot(i));

		if (ModCompat.baubles())
			total += getArrowCount(BaublesCompat.getEquippedQuiver(player));

		return total;
	}

	// ---------------------------------------------------------------- filling

	private void absorbArrows(ItemStack quiver, EntityPlayer player) {
		int free = getCapacity() - getArrowCount(quiver);

		for (int i = 0; i < player.inventory.getSizeInventory() && free > 0; i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if (!isArrow(stack) || stack.stackSize <= 0)
				continue;

			int moved = Math.min(free, stack.stackSize);
			if (isPlainArrow(stack))
				setArrowCount(quiver, getArrowCount(quiver) + moved);
			else {
				addTipped(quiver, stack, moved);
				setArrowCount(quiver, getArrowCount(quiver) + moved);
			}

			stack.stackSize -= moved;
			if (stack.stackSize <= 0)
				player.inventory.setInventorySlotContents(i, null);

			player.playSound("random.pop", 0.2F, ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
			return;
		}
	}

	private void dumpArrows(ItemStack quiver, EntityPlayer player) {
		int total = getArrowCount(quiver);
		if (total <= 0)
			return;

		int plain = total - getTippedCount(quiver);
		for (ItemStack arrow : readTipped(quiver)) {
			int size = arrow.stackSize;
			while (size > 0) {
				int amount = Math.min(ARROW_STACK_SIZE, size);
				ItemStack out = arrow.copy();
				out.stackSize = amount;
				InventoryUtils.addToPlayerInventory(player, out, player.posX, player.posY, player.posZ);
				size -= amount;
			}
		}
		writeTipped(quiver, null);

		while (plain > 0) {
			int amount = Math.min(ARROW_STACK_SIZE, plain);
			InventoryUtils.addToPlayerInventory(player, new ItemStack(Items.arrow, amount), player.posX, player.posY, player.posZ);
			plain -= amount;
		}

		setArrowCount(quiver, 0);
	}

	@Override
	public boolean isEnabled() {
		return GanysSurface.enableQuiver;
	}
}
