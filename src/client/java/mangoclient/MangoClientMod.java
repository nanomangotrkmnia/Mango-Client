package mangoclient;

import mangoclient.module.ModuleManager;
import mangoclient.module.render.NewChunkFinder;
import mangoclient.toast.MangoToast;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;

public class MangoClientMod implements ClientModInitializer {
	public static final String NAME = "Mango Client";
	public static Minecraft mc;

	@Override
	public void onInitializeClient() {
		mc = Minecraft.getInstance();

		MangoToast.init();
		ModuleManager.init();

		WorldRenderEvents.AFTER_ENTITIES.register(ModuleManager::onWorldRender);
		ClientTickEvents.END_CLIENT_TICK.register(client -> ModuleManager.tick());
		ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> {
			NewChunkFinder ncf = ModuleManager.getModule(NewChunkFinder.class);
			if (ncf != null) ncf.onChunkLoad(chunk.getPos().x, chunk.getPos().z);
		});
	}
}
