package ganymedes01.ganyssurface.configuration;

import java.awt.Color;
import java.io.File;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.blocks.BlockStorage;
import ganymedes01.ganyssurface.integration.Integration;
import ganymedes01.ganyssurface.integration.ModIntegrator;
import ganymedes01.ganyssurface.lib.EnumColour;
import ganymedes01.ganyssurface.lib.Reference;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

public class ConfigurationHandler {

	public static ConfigurationHandler INSTANCE = new ConfigurationHandler();
	public Configuration configFile;
	public String[] usedCategories = { Configuration.CATEGORY_GENERAL, "mod integration", "wool colours" };

	// Options that were dropped together with the blocks and items Et Futurum
	// Requiem already provides. Forge keeps every key it finds in an existing
	// config file, so without this list an upgraded instance would keep showing
	// options that no longer do anything. Keep this in sync when a feature is
	// removed.
	private static final String[] OBSOLETE_OPTIONS = {
			"Enable 1.8 Stones",
			"Max number of 1.8 stones in a cluster",
			"Enable Iron Trapdoor",
			"Enable Mutton",
			"Enable Prismarine stuff",
			"Enable 1.8 style doors",
			"Enable inverted daylight sensor",
			"Enable coarse dirt",
			"Enable red sandstone",
			"Enable 1.8 wood fences",
			"Enable 1.8 banners",
			"Enable 1.8 sponge (absorbs water)",
			"Enable slime block",
			"Enable MC:PE beetroots",
			"Enable Silk Touching of mushroom blocks",
			"Enable Lantern",
			"Enable Basalt",
			"Max number of Basalt per cluster",
			"Enable wooden buttons",
			"Enable wooden pressure plates",
			"Enable wooden trapdoors",
			"Enable wooden signs",
			"Make fences, gates and deadbushes burnable",
			"Enable 3D rendering for vanilla items",
			"Enable disguised trapdoors",
			"Enable wood specific chests",
			"Enable dynamic texture for wood chests",
			"Enable wooden ladders",
			"Enable wooden bookshelves",
			"Enable 1.8 Enchanting Table",
			"Enable 1.8 Stone Bricks recipes" };

	private int configInteger(String name, boolean requireRestart, int def) {
		return configInteger(name, null, requireRestart, def);
	}

	private int configInteger(String name, String tooltip, boolean requireRestart, int def) {
		int config = configFile.get(Configuration.CATEGORY_GENERAL, name, def, tooltip).getInt(def);
		return config >= 0 ? config : def;
	}

	private boolean configBoolean(String name, String tooltip, boolean requireRestart, boolean def) {
		return configFile.get(Configuration.CATEGORY_GENERAL, name, def, tooltip).getBoolean(def);
	}

	private boolean configBoolean(String name, boolean requireRestart, boolean def) {
		return configBoolean(name, null, requireRestart, def);
	}

	private boolean configIntegrationBoolean(String modID) {
		return configFile.get("Mod Integration", "Integrate " + modID, true).setRequiresMcRestart(true).getBoolean(true);
	}

	public void init(File file) {
		configFile = new Configuration(file);

		syncConfigs();
		removeObsoleteOptions();
	}

	private void removeObsoleteOptions() {
		ConfigCategory general = configFile.getCategory(Configuration.CATEGORY_GENERAL);

		boolean changed = false;
		for (String option : OBSOLETE_OPTIONS)
			if (general.containsKey(option)) {
				general.remove(option);
				changed = true;
			}

		if (changed)
			configFile.save();
	}

	private void syncConfigs() {
		// Mod Integration
		for (Integration integration : ModIntegrator.modIntegrations)
			integration.setShouldIntegrate(configIntegrationBoolean(integration.getModID()));

		// Blocks/Items
		GanysSurface.enableChocolate = configBoolean("activateChocolate", true, GanysSurface.enableChocolate);
		GanysSurface.enablePoop = configBoolean("enablePoop", true, GanysSurface.enablePoop);
		GanysSurface.enableWoodenArmour = configBoolean("enableWoodenArmour", true, GanysSurface.enableWoodenArmour);
		GanysSurface.enableTea = configBoolean("enableTea", true, GanysSurface.enableTea);
		GanysSurface.enableQuiver = configBoolean("enableQuiver", true, GanysSurface.enableQuiver);
		GanysSurface.enablePaintings = configBoolean("Enable paintings", true, GanysSurface.enablePaintings);
		GanysSurface.enableDispenserShears = configBoolean("Enable dispenser action for shears", true, GanysSurface.enableDispenserShears);
		GanysSurface.enablePlanter = configBoolean("Enable planter", true, GanysSurface.enablePlanter);
		GanysSurface.enableColouredRedstone = configBoolean("Enable coloured redsotne", true, GanysSurface.enableColouredRedstone);
		GanysSurface.enableDislocators = configBoolean("Enable dislocators and block detector", true, GanysSurface.enableDislocators);
		GanysSurface.enableItemDisplay = configBoolean("Enable item display", true, GanysSurface.enableItemDisplay);
		GanysSurface.enablePineCones = configBoolean("Enable pine cones", true, GanysSurface.enablePineCones);
		GanysSurface.enableCookedEgg = configBoolean("Enable cooked egg", true, GanysSurface.enableCookedEgg);
		GanysSurface.enablePocketCritters = configBoolean("Enable pocket critters", true, GanysSurface.enablePocketCritters);
		GanysSurface.enableWorkTables = configBoolean("Enable work tables", true, GanysSurface.enableWorkTables);
		GanysSurface.enableLeafWalls = configBoolean("Enable leaf walls", true, GanysSurface.enableLeafWalls);
		GanysSurface.enableEncasers = configBoolean("Enable encasers", true, GanysSurface.enableEncasers);
		GanysSurface.enableOMC = configBoolean("Enable organic matter compressor", true, GanysSurface.enableOMC);
		GanysSurface.enableSpawnEggs = configBoolean("Enable spawn eggs", true, GanysSurface.enableSpawnEggs);
		GanysSurface.enableAnalisers = configBoolean("Enable analisers", true, GanysSurface.enableAnalisers);
		GanysSurface.enableIcyPick = configBoolean("Enable Icy Pickaxe", true, GanysSurface.enableIcyPick);
		GanysSurface.enableFertilisedSoil = configBoolean("Enable Fertilised Soil", true, GanysSurface.enableFertilisedSoil);
		GanysSurface.enableChestPropellant = configBoolean("Enable Chest Propellant", true, GanysSurface.enableChestPropellant);
		GanysSurface.enableInkHarvester = configBoolean("Enable Ink Harvester", true, GanysSurface.enableInkHarvester);
		GanysSurface.enableRainDetector = configBoolean("Enable Rain Detector", true, GanysSurface.enableRainDetector);
		GanysSurface.enableCushion = configBoolean("Enable Cushion", true, GanysSurface.enableCushion);
		GanysSurface.enableBlockOfCharcoal = configBoolean("Enable Block of Charcoal", true, GanysSurface.enableBlockOfCharcoal);
		GanysSurface.enableRot = configBoolean("Enable Rot", true, GanysSurface.enableRot);
		GanysSurface.enableWoodenWrench = configBoolean("Enable Wooden Wrench", true, GanysSurface.enableWoodenWrench);
		GanysSurface.enableVillageFinder = configBoolean("Enable Village Finder", true, GanysSurface.enableVillageFinder);
		GanysSurface.enableDyedArmour = configBoolean("Enable Iron/Chainmail armour dyeing", true, GanysSurface.enableDyedArmour);
		GanysSurface.enableRedyeingBlocks = configBoolean("Enable re-dyeing carpets, clay, glass and panes", true, GanysSurface.enableRedyeingBlocks);
		GanysSurface.enableExtraVanillaRecipes = configBoolean("Enable extra vanilla recipes (for name tags, cobwebs and etc)", true, GanysSurface.enableExtraVanillaRecipes);
		GanysSurface.enableEndermanDropsBlocks = configBoolean("Enable enderman to drop the blocks they are carrying", true, GanysSurface.enableEndermanDropsBlocks);
		GanysSurface.enableSlowRail = configBoolean("Enable Slow Rail", true, GanysSurface.enableSlowRail);
		GanysSurface.enableFlingablePoop = configBoolean("Enable Poop Throwing", false, GanysSurface.enableFlingablePoop);
		GanysSurface.enableStorageBlocks = configBoolean("Enable storage blocks", "More specifically: " + BlockStorage.getTypesString(), true, GanysSurface.enableStorageBlocks);
		GanysSurface.enableDyeBlocks = configBoolean("Enable dye blocks", true, GanysSurface.enableDyeBlocks);
		GanysSurface.enableNoRecipeConflict = configBoolean("Enable No Recipe Conflict for Crafting Tables", true, GanysSurface.enableNoRecipeConflict);
		GanysSurface.enableNoRecipeConflictForCrafTable = configBoolean("Enable No Recipe Conflict for only the vanilla Crafting Tables", "You'll need to turn this off to craft Extra Utilities's Unstable Ingots", true, GanysSurface.enableNoRecipeConflictForCrafTable);
		GanysSurface.enableMarket = configBoolean("Enable Market", true, GanysSurface.enableMarket);
		GanysSurface.enableEatenCake = configBoolean("Cakes drop themselves", true, GanysSurface.enableEatenCake);

		// Others
		GanysSurface.shouldDoVersionCheck = configBoolean("shouldDoVersionCheck", true, GanysSurface.shouldDoVersionCheck);
		GanysSurface.poopRandomBonemeals = configBoolean("poopRandomBonemeals", false, GanysSurface.poopRandomBonemeals);
		GanysSurface.enableDynamicSnow = configBoolean("enableDynamicSnow", "Snow layers will get taller when it snows and shorter when it stops snowing", false, GanysSurface.enableDynamicSnow);

		GanysSurface.maxLevelOMCWorks = configInteger("maxLevelOMCWorks", false, GanysSurface.maxLevelOMCWorks);
		GanysSurface.inkHarvesterMaxStrike = configInteger("inkHarvesterMaxStrike", false, GanysSurface.inkHarvesterMaxStrike);
		GanysSurface.poopingChance = configInteger("poopingChance", "Larger number means poop is LESS likely to happen", false, GanysSurface.poopingChance);

		// Sheep wool colours
		for (int i = 0; i < 16; i++) {
			String def = "";
			for (int j = 0; j < 3; j++)
				def += Integer.toHexString((int) (EntitySheep.fleeceColorTable[i][j] * 255));
			def = "0x" + def.toUpperCase();
			String colour = configFile.get("wool colours", EnumColour.values()[BlockColored.func_150031_c(i)].toString(), def, "Default: " + def).getString();
			float red = Color.decode(colour).getRed() / 255F;
			float green = Color.decode(colour).getGreen() / 255F;
			float blue = Color.decode(colour).getBlue() / 255F;
			EntitySheep.fleeceColorTable[i] = new float[] { red, green, blue };
		}

		if (configFile.hasChanged())
			configFile.save();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		if (Reference.MOD_ID.equals(eventArgs.modID))
			syncConfigs();
	}
}
