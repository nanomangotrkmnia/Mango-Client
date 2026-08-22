package mangoclient.settings;

public class IntSetting extends Setting {
	public int value;
	public final int min;
	public final int max;

	public IntSetting(String name, int def, int min, int max) {
		super(name);
		this.value = def;
		this.min = min;
		this.max = max;
	}
}
