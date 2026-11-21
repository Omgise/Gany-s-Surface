package ganymedes01.ganyssurface.integration;

import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.ModBlocks;
import ganymedes01.ganyssurface.ModItems;
import ganymedes01.ganyssurface.blocks.BlockStorage;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class ThaumcraftManager extends Integration {

	@Override
	public void init() {
		if (GanysSurface.enableTea) {
			addAspectsToItem(ModItems.camelliaSeeds, new Aspect[] { Aspect.PLANT }, new int[] { 1 });
			addAspectsToItem(ModItems.teaLeaves, new Aspect[] { Aspect.CROP }, new int[] { 1 });
			addAspectsToItem(ModItems.mankyCupOfTea, new Aspect[] { Aspect.POISON, Aspect.DEATH }, new int[] { 2, 1 });
			addAspectsToItem(ModItems.teaBag, new Aspect[] { Aspect.CROP, Aspect.CLOTH }, new int[] { 1, 1 });
			addAspectsToItem(ModItems.cupOfTea, new Aspect[] { Aspect.CROP, Aspect.HUNGER, Aspect.WATER, Aspect.HEAL, Aspect.EARTH }, new int[] { 1, 2, 2, 2, 3 });
			addAspectsToItem(ModItems.emptyMug, new Aspect[] { Aspect.EARTH }, new int[] { 3 });
		}
		if (GanysSurface.enablePoop) {
			addAspectsToItem(ModItems.poop, new Aspect[] { Aspect.SLIME }, new int[] { 2 });
			addAspectsToItem(ModItems.poop, 1, new Aspect[] { Aspect.SLIME }, new int[] { 2 });
		}
		if (GanysSurface.enablePocketCritters) {
			addAspectsToItem(ModItems.pocketCritter, new Aspect[] { Aspect.TRAP, Aspect.BEAST }, new int[] { 3, 1 });
			addAspectsToItem(ModItems.pocketCritter, 1, new Aspect[] { Aspect.TRAP, Aspect.BEAST }, new int[] { 3, 1 });
			addAspectsToItem(ModItems.roastedSquid, new Aspect[] { Aspect.HUNGER, Aspect.BEAST }, new int[] { 3, 1 });
			addAspectsToItem(ModItems.batStew, new Aspect[] { Aspect.HUNGER, Aspect.BEAST, Aspect.DARKNESS }, new int[] { 2, 1, 1 });
			addAspectsToItem(ModItems.batNet, new Aspect[] { Aspect.TREE, Aspect.CLOTH }, new int[] { 2, 1 });
		}
		if (GanysSurface.enableCookedEgg)
			addAspectsToItem(ModItems.cookedEgg, new Aspect[] { Aspect.HUNGER, Aspect.LIFE, Aspect.BEAST }, new int[] { 1, 1, 1 });
		if (GanysSurface.enableChocolate) {
			addAspectsToItem(ModItems.chocolateBar, new Aspect[] { Aspect.HUNGER, Aspect.SENSES }, new int[] { 2, 1 });
			addAspectsToItem(ModBlocks.chocolateCake, new Aspect[] { Aspect.WATER, Aspect.HUNGER, Aspect.LIFE, Aspect.SENSES }, new int[] { 4, 4, 4, 2 });
		}
		if (GanysSurface.enablePineCones) {
			addAspectsToItem(ModItems.pineCone, new Aspect[] { Aspect.CROP, Aspect.PLANT }, new int[] { 2, 1 });
			addAspectsToItem(ModItems.pineNuts, new Aspect[] { Aspect.CROP, Aspect.HUNGER }, new int[] { 2, 1 });
		}
		if (GanysSurface.enableSpawnEggs) {
			addAspectsToItem(new ItemStack(ModItems.horseSpawner, 1, OreDictionary.WILDCARD_VALUE), new Aspect[] { Aspect.SOUL, Aspect.TRAP }, new int[] { 1, 1 });
			addAspectsToItem(ModItems.chargedCreeperSpawner, new Aspect[] { Aspect.SOUL, Aspect.TRAP }, new int[] { 1, 1 });
		}
		if (GanysSurface.enableSlowRail)
			addAspectsToItem(ModBlocks.slowRail, new Aspect[] { Aspect.METAL, Aspect.ENERGY, Aspect.MECHANISM }, new int[] { 2, 1, 1 });
		if (GanysSurface.enableItemDisplay)
			for (int i = 0; i < 16; i++)
				addAspectsToItem(ModBlocks.itemDisplay, i, new Aspect[] { Aspect.CRYSTAL, Aspect.CLOTH }, new int[] { 4, 1 });


		if (GanysSurface.enableLeafWalls)
			addAspectsToItem(ModBlocks.leafWall, new Aspect[] { Aspect.PLANT }, new int[] { 2 });

		if (GanysSurface.enableStorageBlocks)
			addAspectsToItem(new ItemStack(ModBlocks.storage, 1, BlockStorage.TYPES.sugar.ordinal()), new Aspect[] { Aspect.HUNGER }, new int[] { 6 });

		if (GanysSurface.enableWorkTables) {
			addAspectsToItem(ModBlocks.workTable, new Aspect[] { Aspect.CRAFT, Aspect.VOID }, new int[] { 4, 3 });
			addAspectsToItem(ModBlocks.dualWorkTable, new Aspect[] { Aspect.CRAFT, Aspect.VOID }, new int[] { 7, 5 });
			addAspectsToItem(ModItems.portalDualWorkTable, new Aspect[] { Aspect.CRAFT, Aspect.VOID }, new int[] { 7, 5 });
		}
	}

	@Override
	public void postInit() {
	}

	@Override
	public String getModID() {
		return "Thaumcraft";
	}

	private void addAspectsToItem(Block block, Aspect[] aspects, int[] amounts) {
		addAspectsToItem(new ItemStack(block), aspects, amounts);
	}

	private void addAspectsToItem(Block block, int meta, Aspect[] aspects, int[] amounts) {
		addAspectsToItem(new ItemStack(block, 1, meta), aspects, amounts);
	}

	private void addAspectsToItem(Item item, Aspect[] aspects, int[] amounts) {
		addAspectsToItem(new ItemStack(item), aspects, amounts);
	}

	private void addAspectsToItem(Item items, int meta, Aspect[] aspects, int[] amounts) {
		addAspectsToItem(new ItemStack(items, 1, meta), aspects, amounts);
	}

	private void addAspectsToItem(ItemStack stack, Aspect[] aspects, int[] amounts) {
		AspectList list = new AspectList();
		for (int i = 0; i < aspects.length; i++)
			list.add(aspects[i], amounts[i]);

		ThaumcraftApi.registerObjectTag(stack, list);
	}
}