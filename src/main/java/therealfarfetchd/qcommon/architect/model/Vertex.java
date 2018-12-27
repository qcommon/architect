package therealfarfetchd.qcommon.architect.model;

import java.util.Objects;

import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class Vertex {

    public final Vec3 xyz;
    public final Vec2 uv;

    public Vertex(Vec3 xyz, Vec2 uv) {
        this.xyz = xyz;
        this.uv = uv;
    }

    public Vertex withXYZ(Vec3 xyz) {
        return new Vertex(xyz, uv);
    }

    public Vertex withUV(Vec2 uv) {
        return new Vertex(xyz, uv);
    }

    public Vertex transform(Mat4 mat) {
        return new Vertex(mat.mul(xyz), uv);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return xyz.equals(vertex.xyz) &&
            uv.equals(vertex.uv);
    }

    @Override
    public int hashCode() {
        return Objects.hash(xyz, uv);
    }

    @Override
    public String toString() {
        return "Vertex{" +
            "xyz=" + xyz +
            ", uv=" + uv +
            '}';
    }

}
