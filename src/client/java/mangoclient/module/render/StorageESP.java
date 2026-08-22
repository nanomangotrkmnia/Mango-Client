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
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;

public class StorageESP extends Module implements Renderable3D {
	public final ColorSetting chest = color("Chest", 210, 180, 140, 90);
	public final ColorSetting enderChest = color("Ender Chest", 138, 43, 226, 90);
	public final ColorSetting shulker = color("Shulker", 255, 0, 255, 90);
	public final ColorSetting barrel = color("Barrel", 139, 69, 19, 90);
	public final ColorSetting outline = color("Outline", 255, 255, 255, 220);

	public StorageESP() {
		super("StorageESP", "Highlight storage blocks by type", ModuleCategory.RENDER);
	}

	@Override
	public void onWorldRender(WorldRenderContext ctx) {
		for (BlockEntity be : WorldUtil.blockEntities(MangoClientMod.mc.level, 8)) {
			ColorSetting fill = fillFor(be);
			if (fill == null) continue;
			BlockPos p = be.getBlockPos();
			RenderUtil.box(ctx, p.getX(), p.getY(), p.getZ(), p.getX() + 1, p.getY() + 1, p.getZ() + 1, fill, outline);
		}
	}

	private ColorSetting fillFor(BlockEntity be) {
		if (be instanceof ChestBlockEntity) return chest;
		if (be instanceof EnderChestBlockEntity) return enderChest;
		if (be instanceof ShulkerBoxBlockEntity) return shulker;
		if (be instanceof BarrelBlockEntity) return barrel;
		return null;
	}
}
