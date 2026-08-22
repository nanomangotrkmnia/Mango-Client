package mangoclient.settings;

public class BoolSetting extends Setting {
	public boolean value;

	public BoolSetting(String name, boolean def) {
		super(name);
		this.value = def;
	}

	public void toggle() {
		value = !value;
	}
}
