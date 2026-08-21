package mangoclient.settings;

import java.util.ArrayList;
import java.util.List;

import mangoclient.config.Config;

public abstract class Setting {
	public final String name;
	protected final List<String> parents = new ArrayList<>();

	public Setting(String name) {
		this.name = name;
	}

	public abstract String getDisplay();

	public boolean isVisible() {
		for (String p : parents) {
			BoolSetting bs = Config.getBool(p);
			if (bs != null && !bs.value) return false;
		}
		return true;
	}
}
