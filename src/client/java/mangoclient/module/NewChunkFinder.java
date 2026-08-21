package mangoclient.module;

import java.util.HashSet;
import java.util.Set;

import mangoclient.MangoClientMod;
import mangoclient.settings.ColorSetting;

public class NewChunkFinder extends Module {
	public final Set<Long> chunks = new HashSet<>();
	public final ColorSetting color;

	public NewChunkFinder() {
		super("NewChunkFinder", "Highlight chunks generated in this session");
		color = color("Chunk", 0, 255, 255, 50);
	}

	public long key(int x, int z) {
		return ((long) x << 32) | (z & 0xFFFFFFFFL);
	}

	public void onChunkLoad(int x, int z) {
		if (MangoClientMod.mc.level == null) return;
		chunks.add(key(x, z));
	}

	@Override
	public void onEnable() {
	}

	@Override
	public void onDisable() {
	}
}
