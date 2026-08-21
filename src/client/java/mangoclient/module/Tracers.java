package mangoclient.module;

import mangoclient.settings.ColorSetting;

public class Tracers extends Module {
	public final ColorSetting playerColor;
	public final ColorSetting chestColor;

	public Tracers() {
		super("Tracers", "Draw lines to players and storage");
		playerColor = color("Player", 0, 255, 0, 200);
		chestColor = color("Chest", 255, 200, 0, 200);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}
