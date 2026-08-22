package mangoclient.module.render;

import mangoclient.MangoClientMod;
import mangoclient.module.Module;
import mangoclient.module.ModuleCategory;
import mangoclient.module.Renderable3D;
import mangoclient.settings.ColorSetting;
import mangoclient.utils.RenderUtil;
import mangoclient.utils.WorldUtil;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.phys.Vec3;

public class Tracers extends Module implements Renderable3D {
	public final ColorSetting playerColor = color("Player", 0, 255, 0, 200);
	public final ColorSetting storageColor = color("Storage", 255, 200, 0, 200);

	public Tracers() {
		super("Tracers", "Draw lines to players and storage", ModuleCategory.RENDER);
	}

	@Override
	public void onWorldRender(WorldRenderContext ctx) {
		Vec3 cam = MangoClientMod.mc.gameRenderer.getMainCamera().position();

		for (Entity e : MangoClientMod.mc.level.entitiesForRendering()) {
			if (!(e instanceof Player) || e == MangoClientMod.mc.player) continue;
			Vec3 t = e.position();
			RenderUtil.line(ctx, cam, new Vec3(t.x, t.y + 1.0, t.z), playerColor);
		}

		for (BlockEntity be : WorldUtil.blockEntities(MangoClientMod.mc.level, 8)) {
			if (!isStorage(be)) continue;
			BlockPos p = be.getBlockPos();
			RenderUtil.line(ctx, cam, new Vec3(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5), storageColor);
		}
	}

	private static boolean isStorage(BlockEntity be) {
		return be instanceof ChestBlockEntity || be instanceof EnderChestBlockEntity
				|| be instanceof ShulkerBoxBlockEntity || be instanceof BarrelBlockEntity;
	}
}
