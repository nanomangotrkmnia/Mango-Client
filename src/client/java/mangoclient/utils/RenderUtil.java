package mangoclient.utils;

import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.VertexConsumer;

import mangoclient.settings.ColorSetting;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;

public class RenderUtil {
	private static final RenderType LINES = RenderType.create("mango_lines",
			RenderSetup.builder(RenderPipelines.LINES).createRenderSetup());
	private static final RenderType QUADS = RenderType.create("mango_quads",
			RenderSetup.builder(RenderPipelines.DEBUG_FILLED_BOX).createRenderSetup());

	public static Matrix4f matrix(WorldRenderContext ctx) {
		return ctx.matrices().last().pose();
	}

	public static MultiBufferSource buffers(WorldRenderContext ctx) {
		return ctx.consumers();
	}

	public static void box(WorldRenderContext ctx, double x0, double y0, double z0, double x1, double y1, double z1,
			ColorSetting fill, ColorSetting outline) {
		MultiBufferSource buffers = ctx.consumers();
		Matrix4f mat = ctx.matrices().last().pose();
		drawQuads(buffers, b -> face(b, mat, x0, y0, z0, x1, y1, z1, fill));
		drawLines(buffers, b -> edges(b, mat, x0, y0, z0, x1, y1, z1, outline));
	}

	public static void line(WorldRenderContext ctx, Vec3 a, Vec3 b, ColorSetting color) {
		VertexConsumer lines = ctx.consumers().getBuffer(LINES);
		Matrix4f mat = ctx.matrices().last().pose();
		seg(lines, mat, a.x, a.y, a.z, b.x, b.y, b.z, color.rf(), color.gf(), color.bf(), color.af());
	}

	private static void face(VertexConsumer b, Matrix4f m, double x0, double y0, double z0, double x1, double y1,
			double z1, ColorSetting c) {
		float r = c.rf(), g = c.gf(), bl = c.bf(), a = c.af();
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
		float r = c.rf(), g = c.gf(), bl = c.bf(), a = c.af();
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

	private static void drawQuads(MultiBufferSource buffers, LineSink sink) {
		sink.build(buffers.getBuffer(QUADS));
	}

	private static void drawLines(MultiBufferSource buffers, LineSink sink) {
		sink.build(buffers.getBuffer(LINES));
	}

	private interface LineSink {
		void build(VertexConsumer b);
	}
}
