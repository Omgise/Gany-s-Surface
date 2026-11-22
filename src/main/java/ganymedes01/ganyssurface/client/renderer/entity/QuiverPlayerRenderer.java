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
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderPlayerEvent;

/**
 * Gany's Surface
 *
 * Draws the quiver on the player's back while it is worn in the Baubles body slot. Runs after
 * the vanilla specials so the cape is drawn first and the quiver sits on top of it.
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class QuiverPlayerRenderer {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MOD_ID + ":textures/items/quiver.png");
	private static final float SIZE = 6.5F;

	@SubscribeEvent
	public void onRenderSpecials(RenderPlayerEvent.Specials.Post event) {
		if (!GanysSurface.enableQuiver || !ModCompat.baubles())
			return;

		EntityPlayer player = event.entityPlayer;
		if (player == null || player.isInvisible())
			return;

		if (BaublesCompat.getEquippedQuiver(player) == null)
			return;

		ModelBiped model = event.renderer.modelBipedMain;

		GL11.glPushMatrix();
		// Follow the torso so the quiver leans with the sneaking/swimming pose.
		model.bipedBody.postRender(0.0625F);
		GL11.glTranslatef(1.0F, 2.0F, 2.4F);

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
