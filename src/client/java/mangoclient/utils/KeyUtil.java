package mangoclient.utils;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;

public class KeyUtil {
	public static String keyName(int key) {
		if (key == GLFW.GLFW_KEY_UNKNOWN) return "NONE";

		String name = GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key));
		if (name != null) return name.toUpperCase();

		return switch (key) {
			case GLFW.GLFW_KEY_SPACE -> "Space";
			case GLFW.GLFW_KEY_ESCAPE -> "Escape";
			case GLFW.GLFW_KEY_ENTER -> "Enter";
			case GLFW.GLFW_KEY_TAB -> "Tab";
			case GLFW.GLFW_KEY_BACKSPACE -> "Backspace";
			case GLFW.GLFW_KEY_INSERT -> "Insert";
			case GLFW.GLFW_KEY_DELETE -> "Delete";
			case GLFW.GLFW_KEY_RIGHT -> "Right";
			case GLFW.GLFW_KEY_LEFT -> "Left";
			case GLFW.GLFW_KEY_DOWN -> "Down";
			case GLFW.GLFW_KEY_UP -> "Up";
			case GLFW.GLFW_KEY_PAGE_UP -> "Page Up";
			case GLFW.GLFW_KEY_PAGE_DOWN -> "Page Down";
			case GLFW.GLFW_KEY_HOME -> "Home";
			case GLFW.GLFW_KEY_END -> "End";
			case GLFW.GLFW_KEY_CAPS_LOCK -> "Caps Lock";
			case GLFW.GLFW_KEY_LEFT_SHIFT -> "Left Shift";
			case GLFW.GLFW_KEY_LEFT_CONTROL -> "Left Control";
			case GLFW.GLFW_KEY_LEFT_ALT -> "Left Alt";
			case GLFW.GLFW_KEY_RIGHT_SHIFT -> "Right Shift";
			case GLFW.GLFW_KEY_RIGHT_CONTROL -> "Right Control";
			case GLFW.GLFW_KEY_RIGHT_ALT -> "Right Alt";
			case GLFW.GLFW_KEY_F1 -> "F1";
			case GLFW.GLFW_KEY_F2 -> "F2";
			case GLFW.GLFW_KEY_F3 -> "F3";
			case GLFW.GLFW_KEY_F4 -> "F4";
			case GLFW.GLFW_KEY_F5 -> "F5";
			case GLFW.GLFW_KEY_F6 -> "F6";
			case GLFW.GLFW_KEY_F7 -> "F7";
			case GLFW.GLFW_KEY_F8 -> "F8";
			case GLFW.GLFW_KEY_F9 -> "F9";
			case GLFW.GLFW_KEY_F10 -> "F10";
			case GLFW.GLFW_KEY_F11 -> "F11";
			case GLFW.GLFW_KEY_F12 -> "F12";
			default -> String.valueOf(key);
		};
	}

	public static int parseKey(String value) {
		String key = value == null ? "" : value.trim();
		if (key.isEmpty() || key.equalsIgnoreCase("none")) return GLFW.GLFW_KEY_UNKNOWN;

		try {
			return Integer.parseInt(key);
		} catch (NumberFormatException ignored) {
		}

		try {
			return InputConstants.getKey("key.keyboard." + key.toLowerCase().replace(" ", ".")).getValue();
		} catch (RuntimeException ignored) {
			return GLFW.GLFW_KEY_UNKNOWN;
		}
	}
}
