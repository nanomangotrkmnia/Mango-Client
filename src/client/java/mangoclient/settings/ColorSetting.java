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

	public void set(int r, int g, int b, int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	public int packed() {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}

	public float rf() {
		return r / 255f;
	}

	public float gf() {
		return g / 255f;
	}

	public float bf() {
		return b / 255f;
	}

	public float af() {
		return a / 255f;
	}
}
