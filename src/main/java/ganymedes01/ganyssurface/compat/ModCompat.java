package ganymedes01.ganyssurface.compat;

/**
 * Gany's Surface
 *
 * Presence checks for the optional mods. Detecting the API class is more reliable than
 * {@code Loader.isModLoaded}: forks rename the mod id (Baubles-Expanded ships as
 * {@code Baubles|Expanded}, not {@code Baubles}), while the API package stays stable.
 *
 * This class deliberately references no optional-mod type, so it is safe to load when the
 * other mod is missing. The answers are cached because these checks sit on hot paths
 * (every bow draw and every shot).
 *
 * @author ganymedes01
 *
 */

public final class ModCompat {

	private static Boolean baubles;
	private static Boolean etFuturum;

	private ModCompat() {
	}

	private static boolean isClassPresent(String name) {
		try {
			Class.forName(name, false, ModCompat.class.getClassLoader());
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	public static boolean baubles() {
		if (baubles == null)
			baubles = isClassPresent("baubles.api.BaublesApi");
		return baubles;
	}

	public static boolean etFuturum() {
		if (etFuturum == null)
			etFuturum = isClassPresent("ganymedes01.etfuturum.items.ItemArrowTipped");
		return etFuturum;
	}
}
