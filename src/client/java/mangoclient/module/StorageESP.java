package mangoclient.module;

import mangoclient.settings.ColorSetting;

public class StorageESP extends Module {
	public final ColorSetting chest;
	public final ColorSetting enderChest;
	public final ColorSetting shulker;
	public final ColorSetting barrel;
	public final ColorSetting outline;

	public StorageESP() {
		super("StorageESP", "Highlight storage blocks by type");
		chest = color("Chest", 210, 180, 140, 90);
		enderChest = color("EnderChest", 138, 43, 226, 90);
		shulker = color("Shulker", 255, 0, 255, 90);
		barrel = color("Barrel", 139, 69, 19, 90);
		outline = color("Outline", 255, 255, 255, 220);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}
