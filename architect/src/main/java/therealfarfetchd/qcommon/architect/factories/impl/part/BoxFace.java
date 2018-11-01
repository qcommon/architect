package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.EnumFacing;

import java.util.Objects;
import java.util.Optional;

import therealfarfetchd.qcommon.architect.math.Vec2;
import therealfarfetchd.qcommon.architect.math.Vec3;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;

public interface BoxFace {

    Optional<Face> makeFace(EnumFacing face, Vec3 from, Vec3 to);

    class None implements BoxFace {

        public static final None INSTANCE = new None();

        private None() {}

        @Override
        public Optional<Face> makeFace(EnumFacing face, Vec3 from, Vec3 to) {
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "None";
        }
    }

    class AutoUV implements BoxFace {

        public final TextureRef texture;

        public AutoUV(TextureRef texture) {
            this.texture = texture;
        }

        @Override
        public Optional<Face> makeFace(EnumFacing face, Vec3 from, Vec3 to) {
            Vec3[] v = new Vec3[4];
            ManualUV.getVec(face, from, to, v);

            Vec2 uv1a, uv2a, uv3a, uv4a;

            switch (face) {
                case DOWN:
                case UP:
                    uv1a = new Vec2(from.x, from.z);
                    uv3a = new Vec2(to.x, to.z);
                    break;
                case NORTH:
                case SOUTH:
                    uv1a = new Vec2(from.x, from.y);
                    uv3a = new Vec2(to.x, to.y);
                    break;
                case WEST:
                case EAST:
                    uv1a = new Vec2(from.z, from.y);
                    uv3a = new Vec2(to.z, to.y);
                    break;
                default:
                    throw new IllegalStateException("face is null");
            }

            uv2a = new Vec2(uv1a.x, uv3a.y);
            uv4a = new Vec2(uv3a.x, uv1a.y);

            return Optional.of(new Quad(texture, new Vertex(v[0], uv1a), new Vertex(v[1], uv2a), new Vertex(v[2], uv3a), new Vertex(v[3], uv4a)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AutoUV autoUV = (AutoUV) o;
            return Objects.equals(texture, autoUV.texture);
        }

        @Override
        public int hashCode() {
            return Objects.hash(texture);
        }

        @Override
        public String toString() {
            return String.format("AutoUV(texture = %s)", texture);
        }
    }

    class ManualUV implements BoxFace {

        public final TextureRef texture;
        public final Vec2 uv1, uv2;

        public ManualUV(TextureRef texture, Vec2 uv1, Vec2 uv2) {
            this.texture = texture;
            this.uv1 = uv1;
            this.uv2 = uv2;
        }

        @Override
        public Optional<Face> makeFace(EnumFacing face, Vec3 from, Vec3 to) {
            Vec3[] v = new Vec3[4];
            getVec(face, from, to, v);

            Vec2 uv1a = uv1;
            Vec2 uv2a = new Vec2(uv1.x, uv2.y);
            Vec2 uv3a = uv2;
            Vec2 uv4a = new Vec2(uv2.x, uv1.y);

            return Optional.of(new Quad(texture, new Vertex(v[0], uv1a), new Vertex(v[1], uv2a), new Vertex(v[2], uv3a), new Vertex(v[3], uv4a)));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ManualUV manualUV = (ManualUV) o;
            return Objects.equals(texture, manualUV.texture) &&
                Objects.equals(uv1, manualUV.uv1) &&
                Objects.equals(uv2, manualUV.uv2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(texture, uv1, uv2);
        }

        @Override
        public String toString() {
            return String.format("ManualUV(texture = %s, uv1 = %s, uv2 = %s)", texture,uv1,uv2);
        }

        static void getVec(EnumFacing face, Vec3 from, Vec3 to, Vec3[] v) {
            switch (face) {
                case DOWN:
                    v[0] = from;
                    v[1] = new Vec3(to.x, from.y, from.z);
                    v[2] = new Vec3(to.x, from.y, to.z);
                    v[3] = new Vec3(from.x, from.y, to.z);
                    break;
                case UP:
                    v[0] = new Vec3(from.x, to.y, from.z);
                    v[1] = new Vec3(from.x, to.y, to.z);
                    v[2] = to;
                    v[3] = new Vec3(to.x, to.y, from.z);
                    break;
                case NORTH:
                    v[0] = from;
                    v[1] = new Vec3(to.x, from.y, from.z);
                    v[2] = new Vec3(to.x, to.y, from.z);
                    v[3] = new Vec3(from.x, to.y, from.z);
                    break;
                case SOUTH:
                    v[0] = new Vec3(from.x, from.y, to.z);
                    v[1] = new Vec3(from.x, to.y, to.z);
                    v[2] = to;
                    v[3] = new Vec3(to.x, from.y, to.z);
                    break;
                case WEST:
                    v[0] = from;
                    v[1] = new Vec3(from.x, to.y, from.z);
                    v[2] = new Vec3(from.x, to.y, to.z);
                    v[3] = new Vec3(from.x, from.y, to.z);
                    break;
                case EAST:
                    v[0] = new Vec3(to.x, from.y, from.z);
                    v[1] = new Vec3(to.x, from.y, to.z);
                    v[2] = to;
                    v[3] = new Vec3(to.x, to.y, from.z);
                    break;
                default:
                    throw new IllegalStateException("face is null");
            }
        }
    }

}
