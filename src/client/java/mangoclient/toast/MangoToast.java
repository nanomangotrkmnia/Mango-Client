package mangoclient.toast;

import mangoclient.MangoClientMod;
import mangoclient.module.Manager;
import mangoclient.module.Module;
import mangoclient.module.Notifications;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.network.chat.Component;

public class MangoToast {
	public static void init() {
	}

	public static void module(Module m) {
		if (MangoClientMod.mc == null) return;
		Notifications n = Manager.notifications;
		if (n != null && !n.enabled) return;
		ToastManager tm = MangoClientMod.mc.getToastManager();
		tm.addToast(new ModuleToast(m.name, m.enabled));
	}

	private static class ModuleToast implements Toast {
		private final Component title;
		private final Component body;
		private final int width;
		private long startTime = -1L;

		ModuleToast(String name, boolean enabled) {
			this.title = Component.literal("Mango Client");
			this.body = Component.literal(name + " " + (enabled ? "ON" : "OFF"));
			Font font = MangoClientMod.mc.font;
			this.width = Math.max(160, font.width(body) + 32);
		}

		@Override
		public Visibility getWantedVisibility() {
			long now = System.currentTimeMillis();
			if (startTime < 0) startTime = now;
			return now - startTime < 3000L ? Visibility.SHOW : Visibility.HIDE;
		}

		@Override
		public void update(ToastManager manager, long time) {
		}

		@Override
		public int width() {
			return width;
		}

		@Override
		public int height() {
			return 32;
		}

		@Override
		public void render(GuiGraphics context, Font textRenderer, long startTime) {
			int w = width();
			int h = height();
			context.fill(0, 0, w, h, 0xE5000000);
			context.fill(0, 0, 3, h, 0xFF00CC66);
			context.drawString(textRenderer, title, 8, 5, 0xFF00CC66);
			context.drawString(textRenderer, body, 8, 18, 0xFFFFFFFF);
		}
	}
}
