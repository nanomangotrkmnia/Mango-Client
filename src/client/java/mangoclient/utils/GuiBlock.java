package mangoclient.utils;

import mangoclient.imgui.ImguiLoader;
import mangoclient.module.render.ClickGui;

public class GuiBlock {
	public static boolean shouldBlockKeyboard() {
		return ClickGui.isOpen() && ImguiLoader.wantsTextInput();
	}

	public static boolean shouldBlockMouse() {
		return ClickGui.isOpen() && ImguiLoader.wantsCaptureMouse();
	}
}
