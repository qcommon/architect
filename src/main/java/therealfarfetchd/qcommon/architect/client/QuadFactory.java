package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormatElement;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.croco.Vec3;

import static java.lang.Math.min;

public class QuadFactory {

    public static final QuadFactory INSTANCE = new QuadFactory();

    public List<BakedQuad> bake(VertexFormat vf, Function<TextureRef, Sprite> mapper, Face face) {
        return face.toQuads().parallelStream().map(q -> bake(vf, mapper, q)).collect(Collectors.toList());
    }

    private BakedQuad bake(VertexFormat vf, Function<TextureRef, Sprite> mapper, Quad quad) {
        ByteBuffer buf = ByteBuffer.allocate(4 * vf.getVertexSize());

        Sprite tex = mapper.apply(quad.texture);

        pushVertex(buf, vf, quad.v0, quad.getNormal(), quad.color, tex);
        pushVertex(buf, vf, quad.v1, quad.getNormal(), quad.color, tex);
        pushVertex(buf, vf, quad.v2, quad.getNormal(), quad.color, tex);
        pushVertex(buf, vf, quad.v3, quad.getNormal(), quad.color, tex);

        int[] d = bytesToInts(buf.array(), buf.position());

        return new BakedQuad(d, 0, quad.getFacing(), mapper.apply(quad.texture));
    }

    private void pushVertex(ByteBuffer buf, VertexFormat vf, Vertex v, Vec3 normal, Color color, Sprite tex) {
        List<VertexFormatElement> elements = vf.getElements();
        for (VertexFormatElement el : elements) {
            switch (el.getType()) {
                case POSITION:
                    putData(buf, el, v.xyz.x, v.xyz.y, v.xyz.z);
                    break;
                case NORMAL:
                    putData(buf, el, normal.x, normal.y, normal.z);
                    break;
                case COLOR:
                    putData(buf, el, color.getRed() / 255f, color.getBlue() / 255f, color.getGreen() / 255f, color.getAlpha() / 255f);
                    break;
                case UV:
                    switch (el.getIndex()) {
                        case 0: // texture
                            putData(buf, el, tex.getU(v.uv.x * 16), tex.getV(v.uv.y * 16));
                            break;
                        case 1: // lightmap
                            putData(buf, el, 0f, 0f);
                            break;
                    }
                    break;
                default:
                    putData(buf, el);
            }
        }
    }

    private void putData(ByteBuffer buf, VertexFormatElement el, float... data) {
        for (int i = 0; i < el.getCount(); i++) {
            putData0(buf, el, data.length > i ? data[i] : 0f);
        }
    }

    private void putData0(ByteBuffer buf, VertexFormatElement el, float f) {
        switch (el.getFormat()) {
            case FLOAT:
                buf.putFloat(f);
                break;
            case UNSIGNED_BYTE:
                buf.put((byte) (f * 255));
                break;
            case BYTE:
                buf.put((byte) (f * 127));
                break;
            case UNSIGNED_SHORT:
                buf.putShort((short) (f * 65535));
                break;
            case SHORT:
                buf.putShort((short) (f * 32767));
                break;
            case UNSIGNED_INT:
                buf.putInt((int) (f * 2147483647));
                break;
            case INT:
                buf.putInt((int) (f * 4294967295L));
                break;
        }
    }

    // big endian
    private int[] bytesToInts(byte[] arr, int count) {
        int[] result = new int[(int) Math.ceil(count / 4f)];

        for (int i = 0; i < min(count, arr.length); i++) {
            int c = i % 4;
            int ri = i / 4;
            int sh = 24 - c * 8;
            result[ri] = result[ri] | (arr[i] & 0xFF) << sh;
        }

        return result;
    }

}
