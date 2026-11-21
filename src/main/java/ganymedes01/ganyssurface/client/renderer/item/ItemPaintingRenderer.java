package ganymedes01.ganyssurface.client.renderer.item;

import java.lang.reflect.Method;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.ganyssurface.client.OpenGLHelper;
import ganymedes01.ganyssurface.items.Painting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.item.EntityPainting.EnumArt;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

/**
 * Gany's Surface
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class ItemPaintingRenderer implements IItemRenderer {

	// The 1.7.10 paintings atlas. Only the 26 vanilla arts live in it.
	private static final ResourceLocation VANILLA_PAINTINGS = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
	// Fallback for modded arts we have no texture for: the plain painting item icon.
	private static final ResourceLocation CANVAS = new ResourceLocation("textures/items/painting.png");

	// RiftFlux (GTNH) appends its own arts to EnumArt and keeps one texture per art. Asking it
	// for the texture keeps this renderer correct without a build-time dependency on it.
	private static boolean riftFluxChecked;
	private static Method getCustomTexture;
	private static Method usesFullTexture;

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return type == ItemRenderType.INVENTORY;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
		EnumArt art = EnumArt.values()[Painting.getMeta(stack)];

		ResourceLocation texture = getModdedTexture(art);
		boolean fullTexture = texture != null && usesFullTexture(art);

		if (texture == null)
			if (Painting.isVanillaArt(stack)) {
				texture = VANILLA_PAINTINGS;
				fullTexture = false;
			} else {
				// A modded art whose mod cannot tell us where its texture is. Sampling the vanilla
				// atlas with its offsets would only draw garbage, so show a plain canvas instead.
				texture = CANVAS;
				fullTexture = true;
			}

		int textureWidth = fullTexture ? art.sizeX : 256;
		int textureHeight = fullTexture ? art.sizeY : 256;
		int offsetX = fullTexture ? 0 : art.offsetX;
		int offsetY = fullTexture ? 0 : art.offsetY;

		OpenGLHelper.pushMatrix();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		OpenGLHelper.scale(0.0625F, 0.0625F, 0.0625F);
		OpenGLHelper.disableLighting();
		OpenGLHelper.rotate(225, 0, 1, 0);

		if (art.sizeX > 26)
			OpenGLHelper.scale(26.0 / art.sizeX, 26.0 / art.sizeX, 1);
		else if (art.sizeY > 26)
			OpenGLHelper.scale(26.0 / art.sizeY, 26.0 / art.sizeY, 1);

		renderPainting(art.sizeX, art.sizeY, offsetX, offsetY, textureWidth, textureHeight);

		OpenGLHelper.enableLighting();
		OpenGLHelper.popMatrix();
	}

	private static ResourceLocation getModdedTexture(EnumArt art) {
		initRiftFlux();
		if (getCustomTexture == null)
			return null;

		try {
			return (ResourceLocation) getCustomTexture.invoke(null, art);
		} catch (Throwable e) {
			return null;
		}
	}

	private static boolean usesFullTexture(EnumArt art) {
		if (usesFullTexture == null)
			return false;

		try {
			return Boolean.TRUE.equals(usesFullTexture.invoke(null, art));
		} catch (Throwable e) {
			return false;
		}
	}

	private static void initRiftFlux() {
		if (riftFluxChecked)
			return;
		riftFluxChecked = true;

		try {
			Class<?> registry = Class.forName("com.voidsrift.riftflux.painting.CustomPaintingRegistry");
			getCustomTexture = registry.getMethod("getCustomTexture", EnumArt.class);
			usesFullTexture = registry.getMethod("usesFullTexture", EnumArt.class);
		} catch (Throwable e) {
			getCustomTexture = null;
			usesFullTexture = null;
		}
	}

	private void renderPainting(int width, int height, int offsetX, int offsetY, int textureWidth, int textureHeight) {
		float f = -width / 2.0F;
		float f1 = -height / 2.0F;
		Tessellator tess = Tessellator.instance;
		tess.startDrawingQuads();
		for (int w = 0; w < width / 16; w++)
			for (int h = 0; h < height / 16; h++) {
				float f2 = f + (w + 1) * 16;
				float f3 = f + w * 16;
				float f4 = f1 + (h + 1) * 16;
				float f5 = f1 + h * 16;
				float f6 = (offsetX + width - w * 16) / (float) textureWidth;
				float f7 = (offsetX + width - (w + 1) * 16) / (float) textureWidth;
				float f8 = (offsetY + height - h * 16) / (float) textureHeight;
				float f9 = (offsetY + height - (h + 1) * 16) / (float) textureHeight;

				tess.addVertexWithUV(f2, f5, 0, f7, f8);
				tess.addVertexWithUV(f3, f5, 0, f6, f8);
				tess.addVertexWithUV(f3, f4, 0, f6, f9);
				tess.addVertexWithUV(f2, f4, 0, f7, f9);
			}
		tess.draw();
	}
}
