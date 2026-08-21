package mangoclient.module;

import java.util.ArrayList;
import java.util.List;

import mangoclient.config.Config;
import mangoclient.settings.BoolSetting;
import mangoclient.settings.ColorSetting;
import mangoclient.settings.Setting;
import mangoclient.toast.MangoToast;

public abstract class Module {
	public final String name;
	public final String description;
	public boolean enabled;
	public final List<Setting> settings = new ArrayList<>();

	protected Module(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public void addSetting(Setting s) {
		settings.add(s);
		Config.register(s);
	}

	public void toggle() {
		enabled = !enabled;
		onToggle(enabled);
		MangoToast.module(this);
	}

	public void setEnabled(boolean b) {
		if (enabled != b) {
			enabled = b;
			onToggle(b);
			MangoToast.module(this);
		}
	}

	protected void onToggle(boolean state) {
	}

	public BoolSetting bool(String name, boolean def) {
		BoolSetting s = new BoolSetting(name, def);
		addSetting(s);
		return s;
	}

	public ColorSetting color(String name, int r, int g, int b, int a) {
		ColorSetting s = new ColorSetting(name, r, g, b, a);
		addSetting(s);
		return s;
	}

	public abstract void onEnable();

	public abstract void onDisable();
}
