package mangoclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import mangoclient.MangoClientMod;
import mangoclient.module.Manager;
import mangoclient.module.Xray;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(Block.class)
public class XrayMixin {

	@Inject(method = "shouldRenderFace", at = @At("HEAD"), cancellable = true)
	private static void mangoXray(BlockState state, BlockState neighborState, Direction side,
			CallbackInfoReturnable<Boolean> cir) {
		Xray xray = Manager.xray;
		if (xray == null || !xray.enabled || MangoClientMod.mc.level == null) return;
		Identifier id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
		if (id != null && !xray.isXrayBlock(id.toString())) {
			cir.setReturnValue(false);
		}
	}
}
