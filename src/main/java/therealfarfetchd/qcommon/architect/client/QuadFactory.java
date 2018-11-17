package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.croco.Vec3;

public class QuadFactory {

    public static final QuadFactory INSTANCE = new QuadFactory();

    public List<BakedQuad> bake(VertexFormat vf, Function<TextureRef, TextureAtlasSprite> mapper, Face face) {
        return face.toQuads().parallelStream().map(q -> bake(vf, mapper, q)).collect(Collectors.toList());
    }

    private BakedQuad bake(VertexFormat vf, Function<TextureRef, TextureAtlasSprite> mapper, Quad quad) {
        UnpackedBakedQuad.Builder b = new UnpackedBakedQuad.Builder(vf);
        pipe(b, mapper, quad);
        return b.build();
    }

    private void pipe(IVertexConsumer c, Function<TextureRef, TextureAtlasSprite> mapper, Quad quad) {
        TextureAtlasSprite tex = mapper.apply(quad.texture);

        c.setApplyDiffuseLighting(true);
        c.setQuadOrientation(quad.getFacing());
        c.setQuadTint(-1);
        c.setTexture(tex);

        Vec3 normal = quad.getNormal();

        List<VertexFormatElement> elements = c.getVertexFormat().getElements();
        for (Vertex v : Arrays.asList(quad.v0, quad.v1, quad.v2, quad.v3)) {
            for (int i = 0; i < elements.size(); i++) {
                VertexFormatElement el = elements.get(i);
                switch (el.getUsage()) {
                    case POSITION:
                        c.put(i, v.xyz.x, v.xyz.y, v.xyz.z, 1f);
                        break;
                    case NORMAL:
                        c.put(i, normal.x, normal.y, normal.z, 0f);
                        break;
                    case COLOR:
                        final Color color = quad.getColor();
                        c.put(i, color.getRed() * 255f, color.getGreen() * 255f, color.getBlue() * 255f, color.getAlpha() * 255f);
                        break;
                    case UV:
                        switch (el.getIndex()) {
                            case 0: // texture
                                c.put(i, tex.getInterpolatedU(v.uv.x * 16), tex.getInterpolatedV(v.uv.y * 16));
                                break;
                            case 1: // lightmap
                                c.put(i, 0f, 0f, 0f, 1f);
                                break;
                        }
                        break;
                    default:
                        c.put(i);
                }
            }
        }
    }

}
