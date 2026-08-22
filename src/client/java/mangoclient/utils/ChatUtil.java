package mangoclient.utils;

import net.minecraft.network.chat.Component;

public class ChatUtil implements IMinecraft {
	public static void send(String message) {
		if (mc.player == null) return;
		mc.player.displayClientMessage(Component.literal("\u00a78[\u00a7aMango\u00a78] \u00a77" + message), false);
	}

	public static void info(String message) {
		send(message);
	}
}
