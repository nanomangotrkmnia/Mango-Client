package mangoclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.Window;

import mangoclient.imgui.ImguiLoader;
import mangoclient.module.ModuleManager;
import mangoclient.utils.GuiBlock;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.input.KeyEvent;

@Mixin(KeyboardHandler.class)
public class KeyboardMixin {

	@Inject(method = "setup", at = @At("TAIL"))
	private void mangoInitImGui(Window window, CallbackInfo ci) {
		ImguiLoader.onGlfwInit(window.handle());
	}

	@Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
	private void mangoOnKey(long window, int action, KeyEvent keyEvent, CallbackInfo ci) {
		if (GuiBlock.shouldBlockKeyboard()) {
			ci.cancel();
			return;
		}
		ModuleManager.onKey(keyEvent.key(), action);
	}
}
