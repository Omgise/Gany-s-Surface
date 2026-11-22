package ganymedes01.ganyssurface.client.renderer.entity;

import java.io.File;
import java.io.PrintWriter;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

/**
 * Gany's Surface
 *
 * Where the quiver sprite sits on the player's back. The defaults are the values that looked right
 * in game; /quiveroffsets edits them at runtime and saves them to config/ganyssurface-quiver.txt so
 * a pack or a different player model can be tuned without rebuilding.
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public final class QuiverRenderConfig {

	public static final float DEFAULT_SCALE = 0.55F;
	public static final float DEFAULT_X = 0.0F;
	public static final float DEFAULT_Y = -0.25F;
	public static final float DEFAULT_Z = -0.12F;

	public static float scale = DEFAULT_SCALE;
	public static float x = DEFAULT_X;
	public static float y = DEFAULT_Y;
	public static float z = DEFAULT_Z;

	private QuiverRenderConfig() {
	}

	public static void reset() {
		scale = DEFAULT_SCALE;
		x = DEFAULT_X;
		y = DEFAULT_Y;
		z = DEFAULT_Z;
	}

	private static File file() {
		return new File(Minecraft.getMinecraft().mcDataDir, "config/ganyssurface-quiver.txt");
	}

	public static void load() {
		File file = file();
		if (!file.isFile())
			return;

		try {
			String text = new String(java.nio.file.Files.readAllBytes(file.toPath()), "UTF-8").trim();
			String[] parts = text.split("[\\s,]+");
			if (parts.length >= 4) {
				scale = Float.parseFloat(parts[0]);
				x = Float.parseFloat(parts[1]);
				y = Float.parseFloat(parts[2]);
				z = Float.parseFloat(parts[3]);
			}
		} catch (Exception e) {
			reset();
		}
	}

	public static void save() {
		File file = file();
		try {
			File parent = file.getParentFile();
			if (parent != null && !parent.isDirectory())
				parent.mkdirs();

			PrintWriter writer = new PrintWriter(file, "UTF-8");
			try {
				writer.println(scale + " " + x + " " + y + " " + z);
			} finally {
				writer.close();
			}
		} catch (Exception e) {
			// Not worth breaking the game over a cosmetic offset.
		}
	}

	public static String describe() {
		return "scale=" + scale + " x=" + x + " y=" + y + " z=" + z;
	}
}
