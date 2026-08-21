package mangoclient.module;

import java.util.ArrayList;
import java.util.List;

import mangoclient.MangoClientMod;
import mangoclient.keybind.Keybinds;
import net.minecraft.client.Minecraft;

public class Manager {
	public static final List<Module> modules = new ArrayList<>();

	public static ESP esp;
	public static StorageESP storageESP;
	public static NewChunkFinder newChunkFinder;
	public static Xray xray;
	public static Tracers tracers;
	public static Notifications notifications;

	public static void init() {
		esp = new ESP();
		storageESP = new StorageESP();
		newChunkFinder = new NewChunkFinder();
		xray = new Xray();
		tracers = new Tracers();
		notifications = new Notifications();

		modules.add(esp);
		modules.add(storageESP);
		modules.add(newChunkFinder);
		modules.add(xray);
		modules.add(tracers);
		modules.add(notifications);

		Keybinds.bindModules();
	}

	public static Module get(String name) {
		for (Module m : modules) {
			if (m.name.equalsIgnoreCase(name)) return m;
		}
		return null;
	}

	public static void tick() {
		Minecraft mc = MangoClientMod.mc;
		if (mc.level == null || mc.player == null) return;
		for (Module m : modules) {
			if (m.enabled) {
				if (m instanceof Tickable t) t.onTick();
			}
		}
	}
}
