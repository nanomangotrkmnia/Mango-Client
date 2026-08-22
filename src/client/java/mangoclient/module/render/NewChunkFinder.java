package mangoclient.module.render;

import java.util.HashSet;
import java.util.Set;

import mangoclient.module.Module;
import mangoclient.module.ModuleCategory;
import mangoclient.module.Renderable3D;
import mangoclient.settings.ColorSetting;
import mangoclient.utils.RenderUtil;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;

public class NewChunkFinder extends Module implements Renderable3D {
	public final Set<Long> chunks = new HashSet<>();
	public final ColorSetting color = color("Chunk", 0, 255, 255, 50);

	public NewChunkFinder() {
		super("New Chunks", "Highlight chunks generated in this session", ModuleCategory.RENDER);
	}

	public static long key(int x, int z) {
		return ((long) x << 32) | (z & 0xFFFFFFFFL);
	}

	public void onChunkLoad(int x, int z) {
		chunks.add(key(x, z));
	}

	public void clear() {
		chunks.clear();
	}

	@Override
	public void onWorldRender(WorldRenderContext ctx) {
		for (long k : chunks) {
			int cx = (int) (k >> 32);
			int cz = (int) (k & 0xFFFFFFFFL);
			double x0 = cx * 16.0;
			double z0 = cz * 16.0;
			RenderUtil.box(ctx, x0, 0, z0, x0 + 16, 256, z0 + 16, color, color);
		}
	}
}
