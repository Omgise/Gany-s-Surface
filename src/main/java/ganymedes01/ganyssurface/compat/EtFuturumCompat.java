package ganymedes01.ganyssurface.compat;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Gany's Surface
 *
 * Et Futurum Requiem's tipped arrows carry their potion in the item stack, so the quiver stores
 * them as stacks and has to spawn EF's own arrow entity when firing one. Everything here is
 * reflective so the jar keeps working without EF installed.
 *
 * @author ganymedes01
 *
 */

public final class EtFuturumCompat {

	private static boolean initialised;
	private static Class<?> tippedArrowItem;
	private static Constructor<?> tippedArrowEntity;
	private static Method setArrow;

	private EtFuturumCompat() {
	}

	private static void init() {
		if (initialised)
			return;
		initialised = true;

		try {
			tippedArrowItem = Class.forName("ganymedes01.etfuturum.items.ItemArrowTipped");
			Class<?> entity = Class.forName("ganymedes01.etfuturum.entities.EntityTippedArrow");
			tippedArrowEntity = entity.getConstructor(World.class, EntityLivingBase.class, float.class);
			setArrow = entity.getMethod("setArrow", ItemStack.class);
		} catch (Throwable e) {
			tippedArrowItem = null;
			tippedArrowEntity = null;
			setArrow = null;
		}
	}

	public static boolean isTippedArrow(ItemStack stack) {
		init();
		return stack != null && tippedArrowItem != null && tippedArrowItem.isInstance(stack.getItem());
	}

	public static EntityArrow createTippedArrow(World world, EntityLivingBase shooter, float velocity, ItemStack arrow) {
		init();
		if (tippedArrowEntity == null)
			return null;

		try {
			EntityArrow entity = (EntityArrow) tippedArrowEntity.newInstance(world, shooter, velocity);
			setArrow.invoke(entity, arrow);
			return entity;
		} catch (Throwable e) {
			return null;
		}
	}
}
