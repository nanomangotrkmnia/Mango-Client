package mangoclient.module.render;

import org.lwjgl.glfw.GLFW;

import mangoclient.MangoClientMod;
import mangoclient.gui.ClickGuiRenderable;
import mangoclient.imgui.EmptyScreen;
import mangoclient.imgui.ImguiLoader;
import mangoclient.module.Module;
import mangoclient.module.ModuleCategory;
import mangoclient.utils.InputUtil;
import net.minecraft.client.Minecraft;

public class ClickGui extends Module {
	private static final ClickGuiRenderable RENDERABLE = new ClickGuiRenderable();
	private static boolean open = false;

	public ClickGui() {
		super("Click Gui", "Opens the ImGui module menu", ModuleCategory.RENDER, GLFW.GLFW_KEY_RIGHT_SHIFT);
	}

	public static boolean isOpen() {
		return open;
	}

	@Override
	public void onEnable() {
		open();
	}

	@Override
	public void onDisable() {
		close();
	}

	public static void open() {
		if (open) return;
		open = true;
		ImguiLoader.renderables.add(RENDERABLE);

		Minecraft mc = MangoClientMod.mc;
		InputUtil.releaseMovementKeys();
		mc.mouseHandler.releaseMouse();
		if (mc.screen == null) {
			mc.setScreen(new EmptyScreen());
		}
	}

	public static void close() {
		if (!open) return;
		open = false;
		ImguiLoader.renderables.remove(RENDERABLE);

		Minecraft mc = MangoClientMod.mc;
		if (mc.screen instanceof EmptyScreen) {
			mc.setScreen(null);
		}
		mc.mouseHandler.grabMouse();
	}
}
