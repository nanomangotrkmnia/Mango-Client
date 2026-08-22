package mangoclient.imgui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import mangoclient.module.render.ClickGui;

public class EmptyScreen extends Screen {

	public EmptyScreen() {
		super(Component.literal("MangoClickGui"));
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
	}

	@Override
	public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public void onClose() {
		ClickGui.close();
	}
}
