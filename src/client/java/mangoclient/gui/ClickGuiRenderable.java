package mangoclient.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.type.ImInt;
import imgui.type.ImString;
import mangoclient.imgui.Renderable;
import mangoclient.module.Module;
import mangoclient.module.ModuleManager;
import mangoclient.settings.BlockListSetting;
import mangoclient.settings.BoolSetting;
import mangoclient.settings.ColorSetting;
import mangoclient.settings.FloatSetting;
import mangoclient.settings.IntSetting;
import mangoclient.settings.ListSetting;
import mangoclient.settings.Setting;
import mangoclient.utils.KeyUtil;

public class ClickGuiRenderable implements Renderable {
	private final Map<String, ImString> inputBuffers = new HashMap<>();
	private final ImInt selectedModule = new ImInt(0);

	@Override
	public String getName() {
		return "Mango ClickGui";
	}

	@Override
	public void render() {
		ImGui.setNextWindowSize(380, 520, ImGuiCond.Once);
		ImGui.setNextWindowPos(60, 60, ImGuiCond.Once);

		if (!ImGui.begin("Mango Client")) {
			ImGui.end();
			return;
		}

		renderModuleDropdown();

		ImGui.end();
	}

	private void renderModuleDropdown() {
		List<Module> modules = ModuleManager.getModules();
		String[] names = new String[modules.size()];
		for (int i = 0; i < modules.size(); i++) {
			names[i] = modules.get(i).name;
		}

		ImGui.setNextItemWidth(-1);
		ImGui.combo("##moduleDropdown", selectedModule, names);
		ImGui.separator();

		if (modules.isEmpty()) {
			ImGui.textColored(148, 158, 174, 255, "No modules registered");
			return;
		}

		int index = Math.max(0, Math.min(selectedModule.get(), modules.size() - 1));
		renderModuleSettings(modules.get(index));
	}

	private void renderModuleSettings(Module module) {
		ImGui.pushID("panel" + module.name);

		ImGui.textColored(0, 204, 102, 255, module.name);
		if (module.description != null && !module.description.isEmpty()) {
			ImGui.textColored(255, 205, 92, 255, module.description);
		}
		ImGui.separator();

		if (ImGui.checkbox("Enabled##" + module.name, module.enabled)) {
			module.toggle();
		}

		ImString keyBuffer = inputBuffer("key:" + module.name, KeyUtil.keyName(module.key), 64);
		ImGui.inputText("Bind##" + module.name, keyBuffer);
		if (ImGui.isItemDeactivatedAfterEdit()) {
			module.key = KeyUtil.parseKey(keyBuffer.get());
			keyBuffer.set(KeyUtil.keyName(module.key));
		}

		if (!module.settings.isEmpty()) {
			ImGui.separator();
			ImGui.indent();
		}
		for (Setting setting : module.settings) {
			if (setting.isVisible()) renderSetting(setting, module.name);
		}
		if (!module.settings.isEmpty()) {
			ImGui.unindent();
		}

		ImGui.popID();
	}

	private void renderSetting(Setting setting, String moduleName) {
		if (setting instanceof BoolSetting b) {
			if (ImGui.checkbox(b.name + "##" + moduleName, b.value)) b.toggle();
		} else if (setting instanceof ColorSetting c) {
			float[] color = {c.rf(), c.gf(), c.bf(), c.af()};
			ImGui.setNextItemWidth(160);
			if (ImGui.colorEdit4(c.name + "##" + moduleName, color)) {
				c.set((int) (color[0] * 255), (int) (color[1] * 255), (int) (color[2] * 255), (int) (color[3] * 255));
			}
		} else if (setting instanceof FloatSetting f) {
			float[] v = {f.value};
			ImGui.setNextItemWidth(160);
			if (ImGui.sliderFloat(f.name + "##" + moduleName, v, f.min, f.max, "%.2f")) {
				f.value = v[0];
			}
		} else if (setting instanceof IntSetting i) {
			int[] v = {i.value};
			ImGui.setNextItemWidth(160);
			if (ImGui.sliderInt(i.name + "##" + moduleName, v, i.min, i.max)) {
				i.value = v[0];
			}
		} else if (setting instanceof ListSetting l) {
			ImInt idx = new ImInt(l.index);
			ImGui.setNextItemWidth(160);
			if (ImGui.combo(l.name + "##" + moduleName, idx, l.values)) {
				l.index = idx.get();
			}
		} else if (setting instanceof BlockListSetting bl) {
			ImGui.textColored(148, 158, 174, 255, bl.name + ": " + bl.size() + " blocks");
		}
	}

	private ImString inputBuffer(String id, String value, int capacity) {
		return inputBuffers.computeIfAbsent(id, k -> new ImString(value, capacity));
	}
}
