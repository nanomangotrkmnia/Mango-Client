package mangoclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import mangoclient.utils.GuiBlock;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

	@Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
	private void mangoOnScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
		if (GuiBlock.shouldBlockMouse()) {
			ci.cancel();
		}
	}

	@Inject(method = "onButton", at = @At("HEAD"), cancellable = true)
	private void mangoOnButton(long window, MouseButtonInfo button, int action, CallbackInfo ci) {
		if (GuiBlock.shouldBlockMouse()) {
			ci.cancel();
		}
	}
}
