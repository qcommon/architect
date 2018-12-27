package therealfarfetchd.qcommon.architect.model.part;

import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Direction.AxisDirection;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.tuple.Pair;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;
import therealfarfetchd.qcommon.croco.Vec3i;

import static java.lang.Math.abs;

public class PartInflatedTexture implements Part {

    private final Identifier texture;

    public PartInflatedTexture(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public List<Face> getFaces(StateProvider sp) {
        return Cache.INSTANCE.getFaces(texture);
    }

    private static class Cache implements ResourceReloadListener {

        public static final Cache INSTANCE = new Cache();

        private Map<Identifier, List<Face>> cache = new HashMap<>();

        private Cache() {
            Architect.proxy.registerReloadListener(this);
        }

        public List<Face> getFaces(Identifier texture) {
            return cache.computeIfAbsent(texture, this::createModel);
        }

        private List<Face> createModel(Identifier texture) {
            TextureRef t = TextureRef.fromIdentifier(texture);

            List<Face> faces = new ArrayList<>();

            // sides
            faces.add(new Quad(t,
                new Vertex(new Vec3(1, 0, 7.5f / 16), new Vec2(1, 1)),
                new Vertex(new Vec3(0, 0, 7.5f / 16), new Vec2(0, 1)),
                new Vertex(new Vec3(0, 1, 7.5f / 16), new Vec2(0, 0)),
                new Vertex(new Vec3(1, 1, 7.5f / 16), new Vec2(1, 0)),
                Color.WHITE));

            faces.add(new Quad(t,
                new Vertex(new Vec3(0, 0, 8.5f / 16), new Vec2(0, 1)),
                new Vertex(new Vec3(1, 0, 8.5f / 16), new Vec2(1, 1)),
                new Vertex(new Vec3(1, 1, 8.5f / 16), new Vec2(1, 0)),
                new Vertex(new Vec3(0, 1, 8.5f / 16), new Vec2(0, 0)),
                Color.WHITE));

            try (InputStream is = Architect.proxy.openResource(new Identifier(texture.getNamespace(), String.format("textures/%s.png", texture.getPath())), true)) {
                if (is != null) {
                    BufferedImage img = ImageIO.read(is);
                    for (QuadFacing f : QuadFacing.values()) {
                        descend(t, f, img, faces);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return faces;
        }

        private void descend(TextureRef tex, QuadFacing f, BufferedImage img, List<Face> target) {
            int right = abs(img.getWidth() * f.right.x + img.getHeight() * f.right.y);
            int down = abs(img.getWidth() * f.down.x + img.getHeight() * f.down.y);

            int[] prevColors = new int[right];

            int maxX = img.getWidth() - 1;
            int maxY = img.getHeight() - 1;
            int offsetX = f.offset.x * maxX;
            int offsetY = f.offset.y * maxY;

            for (int i = 0; i < down; i++) {
                int[] colors = new int[right];
                boolean[] c = new boolean[right];

                for (int j = 0; j < colors.length; j++) {
                    int x = (offsetX + f.right.x * j + f.down.x * i);
                    int y = maxY - (offsetY + f.right.y * j + f.down.y * i);
                    int color = img.getRGB(x, y);
                    colors[j] = color;
                    c[j] = compareColors(prevColors[j], color);
                }

                createQuads(f, i, tex, img, c, target);

                prevColors = colors;
            }
        }

        private boolean compareColors(int prevColor, int color) {
            int prevAlpha = prevColor >>> 24;
            int newAlpha = color >>> 24;

            if (prevAlpha < 255 && newAlpha == 255) return true;
            if (prevAlpha == 0 && newAlpha > 0) return true;

            return false;
        }

        private void createQuads(QuadFacing f, int depth, TextureRef tex, BufferedImage img, boolean[] parts, List<Face> target) {
            List<Pair<Integer, Integer>> chunks = new ArrayList<>();

            int start = 0;
            boolean last = false;
            for (int i = 0; i < parts.length; i++) {
                boolean b = parts[i];
                if (b && !last) {
                    start = i;
                } else if (!b && last) {
                    chunks.add(Pair.of(start, i));
                }

                last = b;
            }

            if (last) chunks.add(Pair.of(start, parts.length));

            int dPart = abs(img.getWidth() * f.down.x + img.getHeight() * f.down.y);
            int rPart = abs(img.getWidth() * f.right.x + img.getHeight() * f.right.y);

            for (Pair<Integer, Integer> chunk : chunks) {
                float minX = ((float) f.down.x * depth / dPart) + ((float) f.right.x * chunk.getLeft() / rPart) + f.offset.x;
                float minY = ((float) f.down.y * depth / dPart) + ((float) f.right.y * chunk.getLeft() / rPart) + f.offset.y;
                float maxX = ((float) f.down.x * depth / dPart) + ((float) f.right.x * chunk.getRight() / rPart) + f.offset.x;
                float maxY = ((float) f.down.y * depth / dPart) + ((float) f.right.y * chunk.getRight() / rPart) + f.offset.y;
                float minUvX = minX;
                float minUvY = 1 - minY;
                float maxUvX = ((float) f.down.x * (depth + 1) / dPart) + ((float) f.right.x * chunk.getRight() / rPart) + f.offset.x;
                float maxUvY = 1 - (((float) f.down.y * (depth + 1) / dPart) + ((float) f.right.y * chunk.getRight() / rPart) + f.offset.y);

                Vec3 min = new Vec3(minX, minY, 7.5f / 16);
                Vec3 max = new Vec3(maxX, maxY, 8.5f / 16);
                Vec2 minuv = new Vec2(minUvX, minUvY);
                Vec2 maxuv = new Vec2(maxUvX, maxUvY);

                Vec3 pos2, pos4;
                Vec2 uv2, uv4;

                if (f.facing.getAxis() == Axis.X) {
                    pos2 = new Vec3(min.x, max.y, min.z);
                    uv2 = new Vec2(minuv.x, maxuv.y);
                    pos4 = new Vec3(max.x, min.y, max.z);
                    uv4 = new Vec2(maxuv.x, minuv.y);
                } else {
                    pos2 = new Vec3(max.x, min.y, min.z);
                    uv2 = new Vec2(maxuv.x, minuv.y);
                    pos4 = new Vec3(min.x, max.y, max.z);
                    uv4 = new Vec2(minuv.x, maxuv.y);
                }

                Quad q = new Quad(tex,
                    new Vertex(min, minuv),
                    new Vertex(pos2, uv2),
                    new Vertex(max, maxuv),
                    new Vertex(pos4, uv4),
                    Color.WHITE);

                if (f.flip) q = q.flip();

                target.add(q);
            }
        }

        @Override
        public void onResourceReload(ResourceManager var1) {
            cache.clear();
        }

    }

    private enum QuadFacing {
        XN(Direction.WEST, new Vec3i(1, 0, 0), new Vec3i(0, -1, 0), new Vec3i(0, 1, 0)),
        XP(Direction.EAST, new Vec3i(-1, 0, 0), new Vec3i(0, -1, 0), new Vec3i(1, 1, 0)),
        YN(Direction.DOWN, new Vec3i(0, 1, 0), new Vec3i(1, 0, 0), new Vec3i(0, 0, 0)),
        YP(Direction.UP, new Vec3i(0, -1, 0), new Vec3i(1, 0, 0), new Vec3i(0, 1, 0));

        public final Direction facing;
        public final Vec3i down, right, offset;
        public final boolean flip;

        QuadFacing(Direction facing, Vec3i down, Vec3i right, Vec3i offset) {
            this.facing = facing;
            this.down = down;
            this.right = right;
            this.offset = offset;
            this.flip = ((facing.getDirection() == AxisDirection.NEGATIVE) ^ (facing.getAxis() == Axis.X)) ^ (facing.getAxis() == Axis.Y);
        }
    }

}
