package mangoclient.render;

import java.util.ArrayList;
import java.util.List;

import mangoclient.MangoClientMod;
import mangoclient.module.ESP;
import mangoclient.module.Manager;
import mangoclient.module.NewChunkFinder;
import mangoclient.module.StorageESP;
import mangoclient.module.Tracers;
import mangoclient.settings.ColorSetting;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class WorldRender {

	private static final RenderType LINES = RenderType.create("mango_lines", RenderSetup.builder(RenderPipelines.LINES).createRenderSetup());
	private static final RenderType QUADS = RenderType.create("mango_quads", RenderSetup.builder(RenderPipelines.DEBUG_FILLED_BOX).createRenderSetup());

	public static void onWorldRender(WorldRenderContext ctx) {
		Minecraft mc = MangoClientMod.mc;
		if (mc.player == null || mc.level == null) return;

		Matrix4f mat = ctx.matrices().last().pose();
		MultiBufferSource buffers = ctx.consumers();
		ClientLevel level = mc.level;
		Vec3 camPos = mc.gameRenderer.getMainCamera().position();

		StorageESP se = Manager.storageESP;
		ESP esp = Manager.esp;
		NewChunkFinder ncf = Manager.newChunkFinder;
		Tracers tr = Manager.tracers;

		if (se.enabled) renderStorage(buffers, mat, level, se);
		if (esp.enabled) renderPlayers(buffers, mat, level, esp);
		if (ncf.enabled) renderChunks(buffers, mat, ncf);
		if (tr.enabled) renderTracers(buffers, mat, level, camPos, tr, se);
	}

	private static void renderPlayers(MultiBufferSource buffers, Matrix4f mat, ClientLevel level, ESP esp) {
		for (Entity e : level.entitiesForRendering()) {
			if (!(e instanceof Player)) continue;
			if (e == MangoClientMod.mc.player) continue;
			AABB box = e.getBoundingBox();
			drawBox(buffers, mat, box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ,
					esp.boxColor, esp.outlineColor);
		}
	}

	private static void renderStorage(MultiBufferSource buffers, Matrix4f mat, ClientLevel level, StorageESP se) {
		for (BlockEntity be : blockEntities(level)) {
			ColorSetting fill;
			if (be instanceof ChestBlockEntity) fill = se.chest;
			else if (be instanceof EnderChestBlockEntity) fill = se.enderChest;
			else if (be instanceof ShulkerBoxBlockEntity) fill = se.shulker;
			else if (be instanceof BarrelBlockEntity) fill = se.barrel;
			else continue;
			BlockPos p = be.getBlockPos();
			drawBox(buffers, mat, p.getX(), p.getY(), p.getZ(), p.getX() + 1, p.getY() + 1, p.getZ() + 1,
					fill, se.outline);
		}
	}

	private static void renderChunks(MultiBufferSource buffers, Matrix4f mat, NewChunkFinder ncf) {
		for (long k : ncf.chunks) {
			int cx = (int) (k >> 32);
			int cz = (int) (k & 0xFFFFFFFFL);
			double x0 = cx * 16, z0 = cz * 16;
			drawBox(buffers, mat, x0, 0, z0, x0 + 16, 256, z0 + 16, ncf.color, ncf.color);
		}
	}

	private static void renderTracers(MultiBufferSource buffers, Matrix4f mat, ClientLevel level, Vec3 camPos,
			Tracers tr, StorageESP se) {
		VertexConsumer lines = buffers.getBuffer(LINES);
		for (Entity e : level.entitiesForRendering()) {
			if (!(e instanceof Player)) continue;
			if (e == MangoClientMod.mc.player) continue;
			Vec3 t = e.position();
			line(lines, mat, camPos.x, camPos.y, camPos.z, t.x, t.y + 1.0, t.z, tr.playerColor);
		}
		if (se.enabled) {
			for (BlockEntity be : blockEntities(level)) {
				if (!(be instanceof ChestBlockEntity) && !(be instanceof EnderChestBlockEntity)
						&& !(be instanceof ShulkerBoxBlockEntity) && !(be instanceof BarrelBlockEntity)) continue;
				BlockPos p = be.getBlockPos();
				line(lines, mat, camPos.x, camPos.y, camPos.z, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5,
						tr.chestColor);
			}
		}
	}

	private static List<BlockEntity> blockEntities(ClientLevel level) {
		List<BlockEntity> result = new ArrayList<>();
		if (MangoClientMod.mc.player == null) return result;
		int pcx = MangoClientMod.mc.player.chunkPosition().x;
		int pcz = MangoClientMod.mc.player.chunkPosition().z;
		int radius = 8;
		for (int cx = pcx - radius; cx <= pcx + radius; cx++) {
			for (int cz = pcz - radius; cz <= pcz + radius; cz++) {
				LevelChunk chunk = level.getChunkSource().getChunkNow(cx, cz);
				if (chunk != null) result.addAll(chunk.getBlockEntities().values());
			}
		}
		return result;
	}

	private interface LineSink {
		void build(VertexConsumer b);
	}

	private static void drawBox(MultiBufferSource buffers, Matrix4f mat, double x0, double y0, double z0,
			double x1, double y1, double z1, ColorSetting fill, ColorSetting outline) {
		drawQuads(buffers, mat, b -> face(b, mat, x0, y0, z0, x1, y1, z1, fill));
		drawLines(buffers, mat, b -> edges(b, mat, x0, y0, z0, x1, y1, z1, outline));
	}

	private static void face(VertexConsumer b, Matrix4f m, double x0, double y0, double z0, double x1, double y1,
			double z1, ColorSetting c) {
		float r = c.r / 255f, g = c.g / 255f, bl = c.b / 255f, a = c.a / 255f;
		quad(b, m, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1, r, g, bl, a);
		quad(b, m, x0, y1, z0, x0, y1, z1, x1, y1, z1, x1, y1, z0, r, g, bl, a);
		quad(b, m, x0, y0, z0, x0, y1, z0, x1, y1, z0, x1, y0, z0, r, g, bl, a);
		quad(b, m, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1, r, g, bl, a);
		quad(b, m, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0, r, g, bl, a);
		quad(b, m, x1, y0, z0, x1, y1, z0, x1, y1, z1, x1, y0, z1, r, g, bl, a);
	}

	private static void quad(VertexConsumer b, Matrix4f m, double x0, double y0, double z0, double x1, double y1,
			double z1, double x2, double y2, double z2, double x3, double y3, double z3, float r, float g, float bl,
			float a) {
		b.addVertex(m, (float) x0, (float) y0, (float) z0).setColor(r, g, bl, a);
		b.addVertex(m, (float) x1, (float) y1, (float) z1).setColor(r, g, bl, a);
		b.addVertex(m, (float) x2, (float) y2, (float) z2).setColor(r, g, bl, a);
		b.addVertex(m, (float) x3, (float) y3, (float) z3).setColor(r, g, bl, a);
	}

	private static void edges(VertexConsumer b, Matrix4f m, double x0, double y0, double z0, double x1, double y1,
			double z1, ColorSetting c) {
		float r = c.r / 255f, g = c.g / 255f, bl = c.b / 255f, a = c.a / 255f;
		seg(b, m, x0, y0, z0, x1, y0, z0, r, g, bl, a);
		seg(b, m, x1, y0, z0, x1, y1, z0, r, g, bl, a);
		seg(b, m, x1, y1, z0, x0, y1, z0, r, g, bl, a);
		seg(b, m, x0, y1, z0, x0, y0, z0, r, g, bl, a);
		seg(b, m, x0, y0, z1, x1, y0, z1, r, g, bl, a);
		seg(b, m, x1, y0, z1, x1, y1, z1, r, g, bl, a);
		seg(b, m, x1, y1, z1, x0, y1, z1, r, g, bl, a);
		seg(b, m, x0, y1, z1, x0, y0, z1, r, g, bl, a);
		seg(b, m, x0, y0, z0, x0, y0, z1, r, g, bl, a);
		seg(b, m, x1, y0, z0, x1, y0, z1, r, g, bl, a);
		seg(b, m, x1, y1, z0, x1, y1, z1, r, g, bl, a);
		seg(b, m, x0, y1, z0, x0, y1, z1, r, g, bl, a);
	}

	private static void seg(VertexConsumer b, Matrix4f m, double x0, double y0, double z0, double x1, double y1,
			double z1, float r, float g, float bl, float a) {
		b.addVertex(m, (float) x0, (float) y0, (float) z0).setColor(r, g, bl, a).setNormal(0.0F, 1.0F, 0.0F)
				.setLineWidth(1.0F);
		b.addVertex(m, (float) x1, (float) y1, (float) z1).setColor(r, g, bl, a).setNormal(0.0F, 1.0F, 0.0F)
				.setLineWidth(1.0F);
	}

	private static void line(VertexConsumer b, Matrix4f m, double x0, double y0, double z0, double x1, double y1,
			double z1, ColorSetting c) {
		float r = c.r / 255f, g = c.g / 255f, bl = c.b / 255f, a = c.a / 255f;
		b.addVertex(m, (float) x0, (float) y0, (float) z0).setColor(r, g, bl, a).setNormal(0.0F, 1.0F, 0.0F)
				.setLineWidth(1.0F);
		b.addVertex(m, (float) x1, (float) y1, (float) z1).setColor(r, g, bl, a).setNormal(0.0F, 1.0F, 0.0F)
				.setLineWidth(1.0F);
	}

	private static void drawQuads(MultiBufferSource buffers, Matrix4f mat, LineSink sink) {
		VertexConsumer b = buffers.getBuffer(QUADS);
		sink.build(b);
	}

	private static void drawLines(MultiBufferSource buffers, Matrix4f mat, LineSink sink) {
		VertexConsumer b = buffers.getBuffer(LINES);
		sink.build(b);
	}
}
