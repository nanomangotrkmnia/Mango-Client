package mangoclient.config;

import java.util.HashMap;
import java.util.Map;

import mangoclient.settings.BoolSetting;
import mangoclient.settings.ColorSetting;
import mangoclient.settings.Setting;

public class Config {
	private static final Map<String, Setting> settings = new HashMap<>();

	public static void register(Setting s) {
		settings.put(s.name, s);
	}

	public static Setting get(String name) {
		return settings.get(name);
	}

	public static BoolSetting getBool(String name) {
		Setting s = settings.get(name);
		return s instanceof BoolSetting ? (BoolSetting) s : null;
	}

	public static ColorSetting getColor(String name) {
		Setting s = settings.get(name);
		return s instanceof ColorSetting ? (ColorSetting) s : null;
	}

	public static void init() {
		// settings are registered by modules on construction
	}
}
