package mangoclient.settings;

public abstract class Setting {
	public final String name;
	private BoolSetting parent;

	public Setting(String name) {
		this.name = name;
	}

	public boolean isVisible() {
		return parent == null || parent.value;
	}

	public void setParent(BoolSetting parent) {
		this.parent = parent;
	}
}
