package mangoclient.module;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFW;

import mangoclient.MangoClientMod;
import mangoclient.imgui.EmptyScreen;
import mangoclient.imgui.ImguiLoader;
import mangoclient.module.misc.Notifications;
import mangoclient.module.render.ClickGui;
import mangoclient.module.render.ESP;
import mangoclient.module.render.NewChunkFinder;
import mangoclient.module.render.StorageESP;
import mangoclient.module.render.Tracers;
import mangoclient.module.render.Xray;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;

public class ModuleManager {
	private static final List<Module> modules = new ArrayList<>();
	private static final Map<Class<?>, Module> byClass = new HashMap<>();
	private static final Map<String, Module> byName = new HashMap<>();

	public static void init() {
		register(
				new ESP(),
				new StorageESP(),
				new Xray(),
				new Tracers(),
				new NewChunkFinder(),
				new ClickGui(),
				new Notifications());
	}

	private static void register(Module... mods) {
		for (Module m : mods) {
			modules.add(m);
			byClass.put(m.getClass(), m);
			byName.put(m.name.toLowerCase(), m);
		}
		modules.sort(Comparator.comparing(m -> m.name));
	}

	public static List<Module> getModules() {
		return modules;
	}

	public static List<Module> getModules(ModuleCategory category) {
		List<Module> result = new ArrayList<>();
		for (Module m : modules) {
			if (m.category == category) result.add(m);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Module> T getModule(Class<T> type) {
		Module m = byClass.get(type);
		return m == null ? null : (T) m;
	}

	public static Module getModule(String name) {
		return byName.get(name.toLowerCase());
	}

	public static void onKey(int key, int action) {
		if (action != GLFW.GLFW_PRESS) return;
		Minecraft mc = MangoClientMod.mc;

		if (key == GLFW.GLFW_KEY_ESCAPE) {
			if (ClickGui.isOpen() && !ImguiLoader.wantsCaptureKeyboard()) {
				ClickGui.close();
			}
			return;
		}

		if (mc.screen == null || mc.screen instanceof EmptyScreen) {
			for (Module m : modules) {
				if (m.key != GLFW.GLFW_KEY_UNKNOWN && m.key == key) {
					m.toggle();
					return;
				}
			}
		}
	}

	public static void tick() {
		Minecraft mc = MangoClientMod.mc;
		if (mc.level == null || mc.player == null) return;
		for (Module m : modules) {
			if (m.enabled && m instanceof Tickable t) t.onTick();
		}
	}

	public static void onWorldRender(WorldRenderContext ctx) {
		Minecraft mc = MangoClientMod.mc;
		if (mc.player == null || mc.level == null) return;
		for (Module m : modules) {
			if (m.enabled && m instanceof Renderable3D r) r.onWorldRender(ctx);
		}
	}
}
