package ganymedes01.ganyssurface.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ganymedes01.ganyssurface.GanysSurface;
import ganymedes01.ganyssurface.compat.BaublesCompat;
import ganymedes01.ganyssurface.compat.ModCompat;
import ganymedes01.ganyssurface.lib.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Gany's Surface
 *
 * Draws the quiver on the player's back while it is worn in the Baubles body slot.
 *
 * Coordinates are entity space (origin at the feet, +y up, +z behind the player), the same space
 * the vanilla cape is drawn in, so the quad sits on the back at 0.82..1.32 above the feet.
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class QuiverPlayerRenderer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/items/quiver.png");

	private static final float SIZE = 0.5F;
	private static final float OFFSET_X = 0.0F;
	private static final float OFFSET_Y = 0.82F;
	private static final float OFFSET_Z = 0.13F;

	@SubscribeEvent
	public void onRenderSpecials(RenderPlayerEvent.Specials.Post event) {
		if (!GanysSurface.enableQuiver || !ModCompat.baubles())
			return;

		EntityPlayer player = event.entityPlayer;
		if (player == null || player.isInvisible())
			return;

		if (BaublesCompat.getEquippedQuiver(player) == null)
			return;

		GL11.glPushMatrix();
		GL11.glTranslatef(OFFSET_X, OFFSET_Y, OFFSET_Z);

		// Sneaking tilts the torso forward; lean the quiver with it so it does not float.
		if (player.isSneaking())
			GL11.glRotatef(28.0F, 1.0F, 0.0F, 0.0F);

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		float half = SIZE / 2.0F;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setNormal(0.0F, 0.0F, 1.0F);
		tessellator.addVertexWithUV(-half, SIZE, 0.0D, 0.0D, 1.0D);
		tessellator.addVertexWithUV(half, SIZE, 0.0D, 1.0D, 1.0D);
		tessellator.addVertexWithUV(half, 0.0F, 0.0D, 1.0D, 0.0D);
		tessellator.addVertexWithUV(-half, 0.0F, 0.0D, 0.0D, 0.0D);
		tessellator.draw();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
