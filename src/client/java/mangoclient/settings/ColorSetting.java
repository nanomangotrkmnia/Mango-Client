package mangoclient.settings;

public class ColorSetting extends Setting {
	public int r, g, b, a;

	public ColorSetting(String name, int r, int g, int b, int a) {
		super(name);
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public int packed() {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public void cycle() {
		a = a >= 255 ? 80 : a + 50;
	}

	@Override
	public String getDisplay() {
		return name + " [#" + String.format("%02X%02X%02X]", r, g, b) + "]";
	}
}
