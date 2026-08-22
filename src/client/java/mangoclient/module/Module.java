package mangoclient.module;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import mangoclient.settings.BlockListSetting;
import mangoclient.settings.BoolSetting;
import mangoclient.settings.ColorSetting;
import mangoclient.settings.FloatSetting;
import mangoclient.settings.IntSetting;
import mangoclient.settings.ListSetting;
import mangoclient.settings.Setting;
import mangoclient.toast.MangoToast;

public abstract class Module {
	public final String name;
	public final String description;
	public final ModuleCategory category;
	public int key;
	public boolean enabled;
	public final List<Setting> settings = new ArrayList<>();

	protected Module(String name, String description, ModuleCategory category, int key) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.key = key;
	}

	protected Module(String name, String description, ModuleCategory category) {
		this(name, description, category, GLFW.GLFW_KEY_UNKNOWN);
	}

	public void toggle() {
		setEnabled(!enabled);
	}

	public void setEnabled(boolean b) {
		if (enabled == b) return;
		enabled = b;
		if (b) {
			onEnable();
		} else {
			onDisable();
		}
		MangoToast.module(this);
	}

	public void addSetting(Setting s) {
		settings.add(s);
	}

	protected BoolSetting bool(String name, boolean def) {
		BoolSetting s = new BoolSetting(name, def);
		addSetting(s);
		return s;
	}

	protected ColorSetting color(String name, int r, int g, int b, int a) {
		ColorSetting s = new ColorSetting(name, r, g, b, a);
		addSetting(s);
		return s;
	}

	protected FloatSetting floatSetting(String name, float def, float min, float max) {
		FloatSetting s = new FloatSetting(name, def, min, max);
		addSetting(s);
		return s;
	}

	protected IntSetting intSetting(String name, int def, int min, int max) {
		IntSetting s = new IntSetting(name, def, min, max);
		addSetting(s);
		return s;
	}

	protected ListSetting list(String name, String[] values, String def) {
		ListSetting s = new ListSetting(name, values, def);
		addSetting(s);
		return s;
	}

	protected BlockListSetting blockList(String name) {
		BlockListSetting s = new BlockListSetting(name);
		addSetting(s);
		return s;
	}

	public void onEnable() {
	}

	public void onDisable() {
	}
}
