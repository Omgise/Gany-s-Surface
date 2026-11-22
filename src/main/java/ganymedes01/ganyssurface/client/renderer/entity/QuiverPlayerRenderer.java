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
 * Draws the quiver on the player's back while it is worn in the Baubles body slot. The transform
 * follows the approach Inmis uses for its backpacks on 1.7.10 (the specials event still has the
 * player's body transform active): flip 180 degrees around X so the item quad faces backwards,
 * move it behind the torso, then draw it with ItemRenderer.renderItemIn2D so it gets a little
 * depth instead of being a paper-thin billboard.
 *
 * It renders the item's own icon, so the back always matches what the item shows in the inventory
 * (the empty quiver art, the with-arrows art, and whatever a resource pack overrides on top).
 *
 * @author ganymedes01
 *
 */

@SideOnly(Side.CLIENT)
public class QuiverPlayerRenderer {

	private static final float SCALE = 0.55F;
	private static final float OFFSET_Y = -0.6F;
	private static final float OFFSET_Z = -0.25F;

	@SubscribeEvent
	public void onRenderSpecials(RenderPlayerEvent.Specials.Post event) {
		if (!GanysSurface.enableQuiver || !ModCompat.baubles())
			return;

		EntityPlayer player = event.entityPlayer;
		if (player == null || player.isInvisible())
			return;

		ItemStack quiver = BaublesCompat.getEquippedQuiver(player);
		if (quiver == null)
			return;

		IIcon icon = quiver.getItem().getIcon(quiver, 0);
		if (icon == null)
			return;

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_CURRENT_BIT);
		GL11.glPushMatrix();

		try {
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
			GL11.glTranslatef(0.0F, OFFSET_Y, OFFSET_Z);

			if (player.isSneaking()) {
				GL11.glRotatef(25.0F, 1.0F, 0.0F, 0.0F);
				GL11.glTranslatef(0.0F, -0.2F, 0.0F);
			}

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
}
