package ganymedes01.ganyssurface;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;

/**
 * Gany's Surface
 *
 * Late-mixin entry point. Everything that targets another mod has to go through here: the
 * {@code MixinConfigs} entry in the jar manifest is parsed and locked on the first class
 * transformation of {@code Launch.main}, before FML has even put the other mods' jars in the
 * classloader - a config registered there for another mod is skipped for good, with nothing but a
 * WARN in the log. GTNHMixins finds this class through {@code @LateMixin} (implementing the
 * interface alone is not enough) and calls {@link #getMixins} once the mod jars are visible.
 *
 * <p>
 * Only the Weapon Mod bridge lives here; the {@code InventoryPlayer} mixin targets a vanilla class
 * and stays in the main config.
 *
 * @author ganymedes01
 *
 */

@LateMixin
public class LateMixins implements ILateMixinLoader {

	private static final String CONFIG = "mixins.ganyssurface.late.json";

	@Override
	public String getMixinConfig() {
		return CONFIG;
	}

	/**
	 * Simple class names relative to the config's package. Returning an empty list (rather than the
	 * mixin name) keeps Mixin from looking for a class that is not installed - and
	 * {@code loadedMods} is the reliable gate, since {@code Loader.isModLoaded} can lie this early.
	 */
	@Override
	public List<String> getMixins(Set<String> loadedMods) {
		if (loadedMods.contains("weaponmod"))
			return Collections.singletonList("MixinWeaponModRangedComponent");

		return Collections.emptyList();
	}
}
