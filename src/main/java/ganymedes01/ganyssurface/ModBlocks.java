package ganymedes01.ganyssurface;

import java.lang.reflect.Field;

import cpw.mods.fml.common.registry.GameRegistry;
import ganymedes01.ganyssurface.blocks.AutoEncaser;
import ganymedes01.ganyssurface.blocks.BlockDetector;
import ganymedes01.ganyssurface.blocks.BlockDye;
import ganymedes01.ganyssurface.blocks.BlockOfPoop;
import ganymedes01.ganyssurface.blocks.BlockSlowRail;
import ganymedes01.ganyssurface.blocks.BlockStorage;
import ganymedes01.ganyssurface.blocks.CamelliaCrop;
import ganymedes01.ganyssurface.blocks.CharcoalBlock;
import ganymedes01.ganyssurface.blocks.ChestPropellant;
import ganymedes01.ganyssurface.blocks.ChocolateCakeBlock;
import ganymedes01.ganyssurface.blocks.ColouredRedstone;
import ganymedes01.ganyssurface.blocks.ColouredRedstoneBlock;
import ganymedes01.ganyssurface.blocks.CubicSensoringDislocator;
import ganymedes01.ganyssurface.blocks.Cushion;
import ganymedes01.ganyssurface.blocks.Dislocator;
import ganymedes01.ganyssurface.blocks.DualWorkTable;
import ganymedes01.ganyssurface.blocks.EncasingBench;
import ganymedes01.ganyssurface.blocks.FarmManager;
import ganymedes01.ganyssurface.blocks.FertilizedSoil;
import ganymedes01.ganyssurface.blocks.InkHarvester;
import ganymedes01.ganyssurface.blocks.InvertedRedstoneLamp;
import ganymedes01.ganyssurface.blocks.ItemDisplay;
import ganymedes01.ganyssurface.blocks.Market;
import ganymedes01.ganyssurface.blocks.OrganicMatterCompressor;
import ganymedes01.ganyssurface.blocks.Planter;
import ganymedes01.ganyssurface.blocks.PoopBlock;
import ganymedes01.ganyssurface.blocks.RainDetector;
import ganymedes01.ganyssurface.blocks.SensoringDislocator;
import ganymedes01.ganyssurface.blocks.WorkTable;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class ModBlocks {

	public static final Block camelliaCrop = new CamelliaCrop();
	public static final Block rainDetector = new RainDetector();
	public static final Block blockDetector = new BlockDetector();
	public static final Block dislocator = new Dislocator();
	public static final Block sensitiveDislocator = new SensoringDislocator();
	public static final Block cubicSensitiveDislocator = new CubicSensoringDislocator();
	public static final Block workTable = new WorkTable();
	public static final Block organicMatterCompressor = new OrganicMatterCompressor();
	public static final Block cushion = new Cushion();
	public static final Block chocolateCake = new ChocolateCakeBlock();
	public static final Block itemDisplay = new ItemDisplay();
	public static final Block chestPropellant = new ChestPropellant();
	public static final Block fertilizedSoil = new FertilizedSoil();
	public static final Block planter = new Planter();
	public static final Block inkHarvester = new InkHarvester();
	public static final Block[] colouredRedstone = new Block[16];
	public static final Block colouredRedstoneBlock = new ColouredRedstoneBlock();
	public static final Block dualWorkTable = new DualWorkTable();
	public static final Block poop = new PoopBlock();
	public static final Block farmManager = new FarmManager();
	public static final Block encasingBench = new EncasingBench();
	public static final Block autoEncaser = new AutoEncaser();
	public static final Block charcoalBlock = new CharcoalBlock();
	public static final Block slowRail = new BlockSlowRail();
	public static final Block blockOfPoop = new BlockOfPoop();
	public static final BlockStorage storage = new BlockStorage();
	public static final BlockStorage dye = new BlockDye();
	public static final Block market = new Market();
	public static final Block invertedRedstoneLampOff = new InvertedRedstoneLamp(true);
	public static final Block invertedRedstoneLampOn = new InvertedRedstoneLamp(false);

	// The 1.8 wood blocks (doors, fences, fence gates, buttons, pressure plates,
	// trapdoors, signs) and the other 1.8/1.9 blocks (granite/diorite/andesite,
	// iron trapdoor, prismarine, sea lantern, inverted daylight detector, red
	// sandstone, coarse dirt, banner, sponge, slime block, beetroots, lantern and
	// basalt) used to live here. Et Futurum Requiem registers all of them, so a
	// GTNH instance would end up with two of every block; they were removed.

	static {
		for (int i = 0; i < 16; i++)
			if (i != 1) // Skip Red
				colouredRedstone[i] = new ColouredRedstone(i);
	}

	public static void init() {
		try {
			for (Field f : ModBlocks.class.getDeclaredFields()) {
				Object obj = f.get(null);
				if (obj instanceof Block)
					registerBlock((Block) obj);
				else if (obj instanceof Block[])
					for (Block block : (Block[]) obj)
						if (block != null)
							registerBlock(block);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	private static void registerBlock(Block block) {
		if (!(block instanceof IConfigurable) || ((IConfigurable) block).isEnabled()) {
			String name = block.getUnlocalizedName();
			String[] strings = name.split("\\.");

			if (block instanceof ISubBlocksBlock)
				GameRegistry.registerBlock(block, ((ISubBlocksBlock) block).getItemBlockClass(), strings[strings.length - 1]);
			else
				GameRegistry.registerBlock(block, strings[strings.length - 1]);

			if (block instanceof IBurnableBlock)
				Blocks.fire.setFireInfo(block, 5, 20);
		}
	}

	public static interface ISubBlocksBlock {

		Class<? extends ItemBlock> getItemBlockClass();
	}

	public static interface IBurnableBlock {
	}
}
