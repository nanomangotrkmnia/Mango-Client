package mangoclient.module.render;

import mangoclient.MangoClientMod;
import mangoclient.module.Module;
import mangoclient.module.ModuleCategory;
import mangoclient.module.Renderable3D;
import mangoclient.settings.ColorSetting;
import mangoclient.utils.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class ESP extends Module implements Renderable3D {
	public final ColorSetting boxColor = color("Box", 255, 0, 0, 60);
	public final ColorSetting outlineColor = color("Outline", 255, 0, 0, 200);

	public ESP() {
		super("ESP", "Render boxes around players through walls", ModuleCategory.RENDER);
	}

	@Override
	public void onWorldRender(WorldRenderContext ctx) {
		for (Entity e : MangoClientMod.mc.level.entitiesForRendering()) {
			if (!(e instanceof Player) || e == MangoClientMod.mc.player) continue;
			AABB box = e.getBoundingBox();
			RenderUtil.box(ctx, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, boxColor, outlineColor);
		}
	}
}
