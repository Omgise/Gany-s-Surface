package ganymedes01.ganyssurface.client.renderer.entity;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.compat.BaublesCompat;
import ganymedes01.ganyssurface.compat.ModCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Gany's Surface
 *
 * Draws the quiver on the player's back while it is worn in the Baubles body slot, and hides the
 * cape while it is there.
 *
 * The quad is drawn from the item's own icon (so it always matches the inventory icon, including
 * resource pack overrides) with the transform Inmis uses for its 1.7.10 backpacks. Because the
 * body may be bent - vanilla's sneaking pose, and more so on EvenMoBends' animated model - the
 * quad is rotated around the waist by the current bipedBody angles, which is where both of those
 * models keep the torso rotation.
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class QuiverPlayerRenderer {

	/** Roughly the waist: the point the torso bends around. */
	private static final float PIVOT_Y = 0.75F;
	private static final float RAD_TO_DEG = 180.0F / (float) Math.PI;

	private static final float SCALE = 0.45F;
	private static final float OFFSET_X = -0.02F;
	private static final float OFFSET_Y = -0.25F;
	private static final float OFFSET_Z = -0.12F;

	@SubscribeEvent
	public void onRenderSpecialsPre(RenderPlayerEvent.Specials.Pre event) {
		EntityPlayer player = event.entityPlayer;
		if (player == null || !hasQuiver(player))
			return;

		// The quiver sits where the cape would be.
		event.renderCape = false;
	}

	@SubscribeEvent
	public void onRenderSpecialsPost(RenderPlayerEvent.Specials.Post event) {
		if (!GanysSurface.enableQuiver || !ModCompat.baubles())
			return;

		EntityPlayer player = event.entityPlayer;
		if (player == null || player.isInvisible() || !hasQuiver(player))
			return;

		ItemStack quiver = BaublesCompat.getEquippedQuiver(player);
		IIcon icon = quiver.getItem().getIcon(quiver, 0);
		if (icon == null)
			return;

		ModelBiped model = event.renderer.modelBipedMain;

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_CURRENT_BIT);
		GL11.glPushMatrix();

		try {
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			// Follow the torso: vanilla's sneak is 0.5 rad here, EvenMoBends animates the same bone.
			GL11.glTranslatef(0.0F, PIVOT_Y, 0.0F);
			GL11.glRotatef(model.bipedBody.rotateAngleX * RAD_TO_DEG, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(model.bipedBody.rotateAngleY * RAD_TO_DEG, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(model.bipedBody.rotateAngleZ * RAD_TO_DEG, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0.0F, -PIVOT_Y, 0.0F);

			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(OFFSET_X, OFFSET_Y, OFFSET_Z);
			GL11.glScalef(SCALE, SCALE, SCALE);
			GL11.glTranslatef(-0.5F, -0.5F, 0.0F);

			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationItemsTexture);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(),
					icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
		} finally {
			GL11.glPopMatrix();
			GL11.glPopAttrib();
		}
	}

	private static boolean hasQuiver(EntityPlayer player) {
		return GanysSurface.enableQuiver && ModCompat.baubles() && BaublesCompat.getEquippedQuiver(player) != null;
	}
}
