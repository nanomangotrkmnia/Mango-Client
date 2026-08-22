package mangoclient.module;

import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;

public interface Renderable3D {
	void onWorldRender(WorldRenderContext ctx);
}
