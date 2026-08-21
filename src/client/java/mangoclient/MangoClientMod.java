package mangoclient;

import mangoclient.config.Config;
import mangoclient.gui.ClickGUI;
import mangoclient.keybind.Keybinds;
import mangoclient.module.Manager;
import mangoclient.render.WorldRender;
import mangoclient.toast.MangoToast;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.client.Minecraft;

public class MangoClientMod implements ClientModInitializer {
	public static final String NAME = "Mango Client";
	public static Minecraft mc;

	@Override
	public void onInitializeClient() {
		mc = Minecraft.getInstance();

		Config.init();
		Manager.init();
		Keybinds.init();
		MangoToast.init();
		ClickGUI.init();

		WorldRenderEvents.AFTER_ENTITIES.register(WorldRender::onWorldRender);
		HudRenderCallback.EVENT.register(ClickGUI::onHudRender);
		ClientChunkEvents.CHUNK_LOAD.register((world, chunk) -> Manager.newChunkFinder.onChunkLoad(chunk.getPos().x, chunk.getPos().z));
	}
}
