package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.List;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefAbsolute;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class QuadUnpacker {

    public static final QuadUnpacker INSTANCE = new QuadUnpacker();

    public Quad unpack(VertexFormat vf, BakedQuad q) {
        Architect.INSTANCE.logger.info("=== Quad =============");
        TextureRef tr = new TextureRefAbsolute(q.getSprite().getId());

        ByteBuffer buf = ByteBuffer.wrap(intsToBytes(q.getVertexData()));

        Color[] c = {Color.WHITE};
        Vertex v0 = readVertex(vf, buf, q.getSprite(), c);
        Vertex v1 = readVertex(vf, buf, q.getSprite(), null);
        Vertex v2 = readVertex(vf, buf, q.getSprite(), null);
        Vertex v3 = readVertex(vf, buf, q.getSprite(), null);

        Architect.INSTANCE.logger.info("======================");
        return new Quad(tr, v0, v1, v2, v3, c[0]);
    }

    private Vertex readVertex(VertexFormat vf, ByteBuffer buf, Sprite s, @Nullable Color[] c) {
        Architect.INSTANCE.logger.info("Vertex:");

        List<VertexFormatElement> elements = vf.getElements();
        Vec3 pos = Vec3.ORIGIN;
        Vec2 uv = Vec2.ORIGIN;
        for (VertexFormatElement el : elements) {
            String type = "[unknown]";
            float[] data = readData(el, buf);
            switch (el.getType()) {
                case POSITION:
                    type = "position";
                    pos = new Vec3(data[0], data[1], data[2]);
                    break;
                case UV:
                    if (el.getIndex() == 0) {
                        type = "uv";
                        uv = new Vec2(s.getXFromU(data[0]) / 16f, s.getYFromV(data[1]) / 16f);
                    } else if (el.getIndex() == 1) {
                        type = "lightmap uv";
                    }
                    break;
                case COLOR:
                    type = "color";
                    if (c != null) {
                        c[0] = new Color(data[0] / 255f, data[1] / 255f, data[2] / 255f, data[3] / 255f);
                    }
                    break;
            }

            Architect.INSTANCE.logger.info(" - {}: {}", type, data);
        }
        return new Vertex(pos, uv);
    }

    private float[] readData(VertexFormatElement el, ByteBuffer buf) {
        float[] data = new float[el.getCount()];
        for (int i = 0; i < data.length; i++) {
            switch (el.getFormat()) {
                case FLOAT:
                    data[i] = buf.getFloat();
                    break;
                case UNSIGNED_BYTE:
                    data[i] = buf.get() & 0xFF;
                    break;
                case BYTE:
                    data[i] = buf.get();
                    break;
                case UNSIGNED_SHORT:
                    data[i] = buf.getShort() & 0xFFFF;
                    break;
                case SHORT:
                    data[i] = buf.getShort();
                    break;
                case UNSIGNED_INT:
                    data[i] = buf.getInt() & 0xFFFFFFFFL;
                    break;
                case INT:
                    data[i] = buf.getInt();
                    break;
            }
        }
        return data;
    }

    // big endian
    private byte[] intsToBytes(int[] arr) {
        byte[] result = new byte[arr.length * 4];

        for (int i = 0; i < arr.length; i++) {
            result[4 * i] = (byte) (arr[i] >> 24);
            result[4 * i + 1] = (byte) (arr[i] >> 16);
            result[4 * i + 2] = (byte) (arr[i] >> 8);
            result[4 * i + 3] = (byte) (arr[i]);
        }

        return result;
    }

}
