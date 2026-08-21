package mangoclient.keybind;

import java.util.ArrayList;
import java.util.List;

import mangoclient.MangoClientMod;
import mangoclient.gui.ClickGUI;
import mangoclient.module.Manager;
import mangoclient.module.Module;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class Keybinds {
	private static final List<Entry> entries = new ArrayList<>();
	public static KeyMapping guiKey;

	private static class Entry {
		final KeyMapping binding;
		final Runnable action;

		Entry(KeyMapping b, Runnable a) {
			binding = b;
			action = a;
		}
	}

	public static void init() {
		guiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.mango.gui", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, KeyMapping.Category.MISC));
		ClientTickEvents.END_CLIENT_TICK.register(Keybinds::tick);
	}

	public static void bindModules() {
		bind("key.mango.esp", GLFW.GLFW_KEY_K, Manager.esp);
		bind("key.mango.storageesp", GLFW.GLFW_KEY_L, Manager.storageESP);
		bind("key.mango.newchunk", GLFW.GLFW_KEY_N, Manager.newChunkFinder);
		bind("key.mango.xray", GLFW.GLFW_KEY_X, Manager.xray);
		bind("key.mango.tracers", GLFW.GLFW_KEY_T, Manager.tracers);
		bind("key.mango.notifications", GLFW.GLFW_KEY_B, Manager.notifications);
	}

	private static void bind(String id, int code, Module m) {
		KeyMapping kb = KeyBindingHelper.registerKeyBinding(
				new KeyMapping(id, InputConstants.Type.KEYSYM, code, KeyMapping.Category.MISC));
		entries.add(new Entry(kb, m::toggle));
	}

	private static void tick(Minecraft mc) {
		if (guiKey.consumeClick()) {
			ClickGUI.toggle();
		}
		for (Entry e : entries) {
			if (e.binding.consumeClick()) {
				e.action.run();
			}
		}
		if (Manager.esp != null) Manager.tick();
	}
}
