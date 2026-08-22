package mangoclient.imgui;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryStack;

import com.mojang.logging.LogUtils;

import imgui.ImFont;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.ImGuiStyle;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;

import org.slf4j.Logger;

public class ImguiLoader {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final List<Renderable> renderables = new CopyOnWriteArrayList<>();

	private static ImGuiImplGlfw imGuiGlfw;
	private static ImGuiImplGl3 imGuiGl3;
	private static long windowHandle;
	private static boolean initialized = false;
	private static boolean glInitialized = false;
	private static boolean contextCreated = false;
	private static boolean renderedLastFrame = false;

	public static void onGlfwInit(long handle) {
		if (initialized) return;
		initialize(handle);
	}

	private static void initialize(long handle) {
		if (initialized) return;

		try {
			FontExtractor.extractFont();
		} catch (Exception e) {
			LOGGER.warn("Could not extract the custom ImGui font; using the default font", e);
		}

		windowHandle = handle;
		try {
			initializeImGui();
			imGuiGlfw = new ImGuiImplGlfw();
			imGuiGlfw.init(handle, true);
			initialized = true;
		} catch (RuntimeException | Error error) {
			shutdown();
			throw error;
		}
	}

	private static void initializeGl() {
		if (glInitialized) return;
		try {
			imGuiGl3 = new ImGuiImplGl3();
			imGuiGl3.init();
			rebuildFont();
			glInitialized = true;
		} catch (RuntimeException | Error error) {
			shutdown();
			throw error;
		}
	}

	private static void initializeImGui() {
		ImGui.createContext();
		contextCreated = true;

		ImGuiIO io = ImGui.getIO();
		io.setIniFilename(null);
		io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);

		applyStyle();
	}

	private static void rebuildFont() {
		ImGuiIO io = ImGui.getIO();
		ImFontAtlas atlas = io.getFonts();
		atlas.clear();

		float scale = contentScale();
		float fontSize = Math.max(9.0f, Math.round(16.0f * scale));

		ImFontConfig cfg = new ImFontConfig();
		try {
			cfg.setPixelSnapH(true);
			cfg.setOversampleH(3);
			cfg.setOversampleV(2);
			ImFont font = atlas.addFontFromFileTTF(FontExtractor.getFontPath("arial.ttf"), fontSize, cfg);
			if (font != null && font.isValidPtr()) {
				io.setFontDefault(font);
			} else {
				atlas.addFontDefault();
			}
		} finally {
			cfg.destroy();
		}
		atlas.build();
		imGuiGl3.updateFontsTexture();
	}

	private static void applyStyle() {
		ImGuiStyle style = ImGui.getStyle();

		style.setAlpha(1.0f);
		style.setWindowPadding(12f, 10f);
		style.setFramePadding(9f, 6f);
		style.setItemSpacing(9f, 7f);
		style.setItemInnerSpacing(7f, 5f);
		style.setIndentSpacing(18f);
		style.setScrollbarSize(14f);
		style.setGrabMinSize(10f);
		style.setWindowRounding(8f);
		style.setChildRounding(6f);
		style.setFrameRounding(5f);
		style.setPopupRounding(6f);
		style.setScrollbarRounding(7f);
		style.setGrabRounding(5f);
		style.setTabRounding(6f);
		style.setWindowBorderSize(1f);
		style.setChildBorderSize(1f);
		style.setPopupBorderSize(1f);
		style.setFrameBorderSize(1f);
		style.setWindowTitleAlign(0.5f, 0.5f);
		style.setAntiAliasedLines(true);
		style.setAntiAliasedFill(true);

		int accentR = 0, accentG = 204, accentB = 102;
		int glowR = 34, glowG = 255, glowB = 160;

		style.setColor(ImGuiCol.Text, 244, 247, 252, 255);
		style.setColor(ImGuiCol.TextDisabled, 148, 158, 174, 215);
		style.setColor(ImGuiCol.WindowBg, 17, 20, 25, 242);
		style.setColor(ImGuiCol.ChildBg, 23, 27, 34, 228);
		style.setColor(ImGuiCol.PopupBg, 20, 24, 31, 248);
		style.setColor(ImGuiCol.Border, 255, 255, 255, 68);
		style.setColor(ImGuiCol.BorderShadow, 0, 0, 0, 0);
		style.setColor(ImGuiCol.FrameBg, 38, 43, 52, 235);
		style.setColor(ImGuiCol.FrameBgHovered, accentR, accentG, accentB, 76);
		style.setColor(ImGuiCol.FrameBgActive, glowR, glowG, glowB, 104);
		style.setColor(ImGuiCol.TitleBg, 13, 15, 19, 255);
		style.setColor(ImGuiCol.TitleBgActive, 25, 29, 36, 255);
		style.setColor(ImGuiCol.TitleBgCollapsed, 13, 15, 19, 235);
		style.setColor(ImGuiCol.MenuBarBg, 25, 29, 36, 245);
		style.setColor(ImGuiCol.ScrollbarBg, 0, 0, 0, 38);
		style.setColor(ImGuiCol.ScrollbarGrab, 255, 255, 255, 90);
		style.setColor(ImGuiCol.ScrollbarGrabHovered, accentR, accentG, accentB, 125);
		style.setColor(ImGuiCol.ScrollbarGrabActive, glowR, glowG, glowB, 165);
		style.setColor(ImGuiCol.CheckMark, accentR, accentG, accentB, 255);
		style.setColor(ImGuiCol.SliderGrab, accentR, accentG, accentB, 225);
		style.setColor(ImGuiCol.SliderGrabActive, glowR, glowG, glowB, 255);
		style.setColor(ImGuiCol.Button, accentR, accentG, accentB, 78);
		style.setColor(ImGuiCol.ButtonHovered, accentR, accentG, accentB, 128);
		style.setColor(ImGuiCol.ButtonActive, glowR, glowG, glowB, 158);
		style.setColor(ImGuiCol.Header, accentR, accentG, accentB, 42);
		style.setColor(ImGuiCol.HeaderHovered, accentR, accentG, accentB, 84);
		style.setColor(ImGuiCol.HeaderActive, glowR, glowG, glowB, 118);
		style.setColor(ImGuiCol.Separator, 255, 255, 255, 68);
		style.setColor(ImGuiCol.SeparatorHovered, accentR, accentG, accentB, 130);
		style.setColor(ImGuiCol.SeparatorActive, glowR, glowG, glowB, 170);
		style.setColor(ImGuiCol.ResizeGrip, accentR, accentG, accentB, 60);
		style.setColor(ImGuiCol.ResizeGripHovered, accentR, accentG, accentB, 118);
		style.setColor(ImGuiCol.ResizeGripActive, glowR, glowG, glowB, 160);
		style.setColor(ImGuiCol.Tab, 30, 35, 43, 245);
		style.setColor(ImGuiCol.TabHovered, accentR, accentG, accentB, 130);
		style.setColor(ImGuiCol.TabActive, glowR, glowG, glowB, 150);
		style.setColor(ImGuiCol.TabUnfocused, 22, 26, 33, 220);
		style.setColor(ImGuiCol.TabUnfocusedActive, glowR, glowG, glowB, 105);
		style.setColor(ImGuiCol.PlotLines, accentR, accentG, accentB, 225);
		style.setColor(ImGuiCol.PlotLinesHovered, glowR, glowG, glowB, 255);
		style.setColor(ImGuiCol.PlotHistogram, accentR, accentG, accentB, 210);
		style.setColor(ImGuiCol.PlotHistogramHovered, glowR, glowG, glowB, 245);
		style.setColor(ImGuiCol.TableHeaderBg, 34, 39, 48, 245);
		style.setColor(ImGuiCol.TableBorderStrong, 255, 255, 255, 72);
		style.setColor(ImGuiCol.TableBorderLight, 255, 255, 255, 44);
		style.setColor(ImGuiCol.TableRowBg, 255, 255, 255, 0);
		style.setColor(ImGuiCol.TableRowBgAlt, 255, 255, 255, 24);
		style.setColor(ImGuiCol.TextSelectedBg, accentR, accentG, accentB, 76);
		style.setColor(ImGuiCol.DragDropTarget, glowR, glowG, glowB, 210);
		style.setColor(ImGuiCol.NavHighlight, accentR, accentG, accentB, 165);
		style.setColor(ImGuiCol.NavWindowingHighlight, 255, 255, 255, 180);
		style.setColor(ImGuiCol.NavWindowingDimBg, 0, 0, 0, 80);
		style.setColor(ImGuiCol.ModalWindowDimBg, 0, 0, 0, 110);
	}

	private static float contentScale() {
		float scale = 1.0f;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer x = stack.mallocFloat(1);
			FloatBuffer y = stack.mallocFloat(1);
			GLFW.glfwGetWindowContentScale(windowHandle, x, y);
			scale = Math.max(x.get(0), y.get(0));
		} catch (Throwable ignored) {
			scale = 1.0f;
		}
		if (!Float.isFinite(scale) || scale <= 0.0f) scale = 1.0f;
		return Math.max(1.0f, scale);
	}

	public static void onFrameRender() {
		if (!initialized) return;
		if (!glInitialized) initializeGl();

		boolean hasComponents = !renderables.isEmpty();
		if (!hasComponents && !renderedLastFrame) return;
		renderedLastFrame = hasComponents;

		boolean frameStarted = false;
		try {
			imGuiGlfw.newFrame();
			ImGui.newFrame();
			frameStarted = true;

			for (Renderable renderable : renderables) {
				if (renderable == null) continue;
				try {
					renderable.render();
				} catch (RuntimeException exception) {
					LOGGER.error("Failed to render ImGui component {}", renderable.getName(), exception);
				}
			}

			ImGui.render();
			frameStarted = false;
			endFrame(windowHandle);
		} catch (RuntimeException exception) {
			if (frameStarted) {
				try {
					ImGui.endFrame();
				} catch (RuntimeException endFrameException) {
					exception.addSuppressed(endFrameException);
				}
			}
			LOGGER.error("Failed to render the ImGui frame", exception);
		}
	}

	private static void endFrame(long windowPtr) {
		int activeTexture = GL11.glGetInteger(GL13.GL_ACTIVE_TEXTURE);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		int textureBinding = GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
		int sampler = GL11.glGetInteger(GL33.GL_SAMPLER_BINDING);
		boolean scissorTest = GL11.glIsEnabled(GL11.GL_SCISSOR_TEST);
		GL13.glActiveTexture(activeTexture);

		try {
			prepareGlState();
			imGuiGl3.renderDrawData(ImGui.getDrawData());
		} finally {
			restoreCapability(GL11.GL_SCISSOR_TEST, scissorTest);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL33.glBindSampler(0, sampler);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureBinding);
			GL13.glActiveTexture(activeTexture);
		}
	}

	private static void prepareGlState() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL33.glBindSampler(0, 0);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	private static void restoreCapability(int capability, boolean enabled) {
		if (enabled) {
			GL11.glEnable(capability);
		} else {
			GL11.glDisable(capability);
		}
	}

	public static boolean wantsCaptureKeyboard() {
		return initialized && ImGui.getIO().getWantCaptureKeyboard();
	}

	public static boolean wantsTextInput() {
		return initialized && ImGui.getIO().getWantTextInput();
	}

	public static boolean wantsCaptureMouse() {
		return initialized && ImGui.getIO().getWantCaptureMouse();
	}

	public static void shutdown() {
		initialized = false;
		glInitialized = false;
		renderedLastFrame = false;
		windowHandle = 0L;
		renderables.clear();

		if (imGuiGl3 != null) {
			try {
				imGuiGl3.dispose();
			} catch (Throwable ignored) {
			} finally {
				imGuiGl3 = null;
			}
		}

		if (imGuiGlfw != null) {
			try {
				imGuiGlfw.dispose();
			} catch (Throwable ignored) {
			} finally {
				imGuiGlfw = null;
			}
		}

		if (contextCreated) {
			try {
				ImGui.destroyContext();
			} catch (Throwable ignored) {
			} finally {
				contextCreated = false;
			}
		}
	}
}
