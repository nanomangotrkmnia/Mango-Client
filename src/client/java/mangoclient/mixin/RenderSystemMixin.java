package mangoclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.TracyFrameCapture;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;

import mangoclient.imgui.ImguiLoader;

@Mixin(RenderSystem.class)
public class RenderSystemMixin {

	@Inject(method = "flipFrame", at = @At("HEAD"))
	private static void mangoRenderImGui(Window window, TracyFrameCapture capture, CallbackInfo ci) {
		ImguiLoader.onFrameRender();
	}
}
