package mangoclient.utils;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;

public class InputUtil implements IMinecraft {
	public static void releaseMovementKeys() {
		Options options = mc.options;
		if (options == null) return;
		release(options.keyUp);
		release(options.keyDown);
		release(options.keyLeft);
		release(options.keyRight);
		release(options.keyJump);
		release(options.keyShift);
		release(options.keySprint);
	}

	private static void release(KeyMapping keyMapping) {
		if (keyMapping != null) keyMapping.setDown(false);
	}
}
