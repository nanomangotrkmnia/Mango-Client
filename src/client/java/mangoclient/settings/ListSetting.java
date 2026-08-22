package mangoclient.settings;

public class ListSetting extends Setting {
	public final String[] values;
	public int index;

	public ListSetting(String name, String[] values, String def) {
		super(name);
		this.values = values;
		this.index = indexOf(values, def);
	}

	public String get() {
		return values[index];
	}

	public void set(String value) {
		int i = indexOf(values, value);
		if (i >= 0) index = i;
	}

	private static int indexOf(String[] values, String def) {
		for (int i = 0; i < values.length; i++) {
			if (values[i].equalsIgnoreCase(def)) return i;
		}
		return 0;
	}
}
