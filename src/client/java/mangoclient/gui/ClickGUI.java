package mangoclient.gui;

import java.util.ArrayList;
import java.util.List;

import mangoclient.MangoClientMod;
import mangoclient.module.Manager;
import mangoclient.module.Module;
import mangoclient.settings.ColorSetting;
import mangoclient.settings.Setting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.lwjgl.glfw.GLFW;

public class ClickGUI {
	private static boolean open = false;
	private static int panelX = 12;
	private static int panelY = 12;
	private static final int panelW = 180;
	private static boolean dragging = false;
	private static int lastMx = 0, lastMy = 0;
	private static boolean prevLeft = false;

	private static class Region {
		final int x, y, w, h;
		final Runnable action;

		Region(int x, int y, int w, int h, Runnable action) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.action = action;
		}

		boolean hit(int mx, int my) {
			return mx >= x && mx <= x + w && my >= y && my <= y + h;
		}
	}

	public static void init() {
	}

	public static void toggle() {
		open = !open;
	}

	private static boolean leftDown() {
		Minecraft mc = MangoClientMod.mc;
		long handle = mc.getWindow().handle();
		return GLFW.glfwGetMouseButton(handle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
	}

	public static void onHudRender(GuiGraphics ctx, DeltaTracker tickDelta) {
		if (!open) return;
		Minecraft mc = MangoClientMod.mc;
		if (mc.player == null) return;

		int mx = (int) mc.mouseHandler.xpos();
		int my = (int) mc.mouseHandler.ypos();
		boolean down = leftDown();
		boolean edge = down && !prevLeft;
		prevLeft = down;

		List<Region> regions = new ArrayList<>();
		int y = panelY + 22;
		for (Module m : Manager.modules) {
			final Module mod = m;
			regions.add(new Region(panelX + 4, y, panelW - 8, 16, mod::toggle));
			y += 18;
			for (Setting s : m.settings) {
				if (s instanceof ColorSetting cs) {
					final ColorSetting col = cs;
					regions.add(new Region(panelX + 14, y, panelW - 18, 14, col::cycle));
					y += 15;
				}
			}
		}

		if (edge) {
			if (mx >= panelX && mx <= panelX + panelW && my >= panelY && my <= panelY + 20) {
				dragging = true;
				lastMx = mx;
				lastMy = my;
			} else {
				for (Region r : regions) {
					if (r.hit(mx, my)) {
						r.action.run();
						break;
					}
				}
			}
		}

		if (dragging && down) {
			panelX += mx - lastMx;
			panelY += my - lastMy;
			lastMx = mx;
			lastMy = my;
		} else {
			dragging = false;
		}

		render(ctx, regions);
	}

	private static void render(GuiGraphics ctx, List<Region> regions) {
		ctx.fill(panelX, panelY, panelX + panelW, panelY + 20, 0xFF113322);
		ctx.drawString(MangoClientMod.mc.font, "Mango Client", panelX + 6, panelY + 6, 0xFF00CC66);

		int y = panelY + 24;
		for (Module m : Manager.modules) {
			int bg = m.enabled ? 0x9944AA66 : 0x88000000;
			ctx.fill(panelX + 2, y - 2, panelX + panelW - 2, y + 14, bg);
			ctx.drawString(MangoClientMod.mc.font, m.name, panelX + 6, y + 2,
					m.enabled ? 0xFFFFFFFF : 0xFF888888);
			ctx.drawString(MangoClientMod.mc.font, m.enabled ? "[ON]" : "[OFF]",
					panelX + panelW - 38, y + 2, m.enabled ? 0xFF00FF66 : 0xFF666666);
			y += 18;
			for (Setting s : m.settings) {
				if (s instanceof ColorSetting cs) {
					ctx.fill(panelX + 12, y, panelX + 24, y + 10, cs.packed());
					ctx.drawString(MangoClientMod.mc.font, cs.getDisplay(), panelX + 28, y + 1,
							0xFFCCCCCC);
					y += 15;
				}
			}
		}
	}
}
