package mangoclient.module;

import mangoclient.settings.ColorSetting;

public class ESP extends Module {
	public final ColorSetting boxColor;
	public final ColorSetting outlineColor;

	public ESP() {
		super("ESP", "Render boxes around players through walls");
		boxColor = color("ESP Box", 255, 0, 0, 60);
		outlineColor = color("ESP Outline", 255, 0, 0, 200);
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}
