package therealfarfetchd.qcommon.architect.model;

import therealfarfetchd.qcommon.architect.math.Mat4;
import therealfarfetchd.qcommon.architect.math.Vec2;
import therealfarfetchd.qcommon.architect.math.Vec3;

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

}
