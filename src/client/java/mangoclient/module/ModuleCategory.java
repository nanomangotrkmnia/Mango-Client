package mangoclient.module;

public enum ModuleCategory {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	PLAYER("Player"),
	RENDER("Render"),
	MISC("Misc");

	public final String label;

	ModuleCategory(String label) {
		this.label = label;
	}
}
