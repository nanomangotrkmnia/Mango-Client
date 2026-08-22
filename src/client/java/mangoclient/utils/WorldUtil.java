package mangoclient.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

public class WorldUtil implements IMinecraft {
	public static List<BlockEntity> blockEntities(ClientLevel level, int radius) {
		List<BlockEntity> result = new ArrayList<>();
		if (mc.player == null) return result;
		int pcx = mc.player.chunkPosition().x;
		int pcz = mc.player.chunkPosition().z;
		for (int cx = pcx - radius; cx <= pcx + radius; cx++) {
			for (int cz = pcz - radius; cz <= pcz + radius; cz++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
				if (chunk != null) result.addAll(chunk.getBlockEntities().values());
			}
		}
		return result;
	}
}
