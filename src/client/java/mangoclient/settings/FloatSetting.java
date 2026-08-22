package mangoclient.settings;

public class FloatSetting extends Setting {
	public float value;
	public final float min;
	public final float max;

	public FloatSetting(String name, float def, float min, float max) {
		super(name);
		this.value = def;
		this.min = min;
		this.max = max;
	}
}
