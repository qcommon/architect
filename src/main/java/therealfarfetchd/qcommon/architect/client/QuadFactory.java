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
        ByteBuffer buf = ByteBuffer.allocate(128);

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
        for (int i = 0; i < elements.size(); i++) {
            VertexFormatElement el = elements.get(i);
            switch (el.getType()) {
                case POSITION:
                    putData(buf, el, v.xyz.x, v.xyz.y, v.xyz.z, 1f);
                    break;
                case NORMAL:
                    putData(buf, el, normal.x, normal.y, normal.z, 0f);
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
                            putData(buf, el, 0f, 0f, 0f, 1f);
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
            case BYTE:
                buf.put((byte) f);
                break;
            case UNSIGNED_SHORT:
            case SHORT:
                buf.putShort((short) f);
                break;
            case UNSIGNED_INT:
            case INT:
                buf.putInt((int) f);
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

//    private void pipe(IVertexConsumer c, Function<TextureRef, TextureAtlasSprite> mapper, Quad quad) {
//        TextureAtlasSprite tex = mapper.apply(quad.texture);
//
//        c.setApplyDiffuseLighting(true);
//        c.setQuadOrientation(quad.getFacing());
//        c.setQuadTint(-1);
//        c.setTexture(tex);
//
//        Vec3 normal = quad.getNormal();
//
//        List<VertexFormatElement> elements = c.getVertexFormat().getElements();
//        for (Vertex v : Arrays.asList(quad.v0, quad.v1, quad.v2, quad.v3)) {
//            for (int i = 0; i < elements.size(); i++) {
//                VertexFormatElement el = elements.get(i);
//                switch (el.getUsage()) {
//                    case POSITION:
//                        c.put(i, v.xyz.x, v.xyz.y, v.xyz.z, 1f);
//                        break;
//                    case NORMAL:
//                        c.put(i, normal.x, normal.y, normal.z, 0f);
//                        break;
//                    case COLOR:
//                        final Color color = quad.getColor();
//                        c.put(i, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
//                        break;
//                    case UV:
//                        switch (el.getIndex()) {
//                            case 0: // texture
//                                c.put(i, tex.getInterpolatedU(v.uv.x * 16), tex.getInterpolatedV(v.uv.y * 16));
//                                break;
//                            case 1: // lightmap
//                                c.put(i, 0f, 0f, 0f, 1f);
//                                break;
//                        }
//                        break;
//                    default:
//                        c.put(i);
//                }
//            }
//        }
//    }

}
