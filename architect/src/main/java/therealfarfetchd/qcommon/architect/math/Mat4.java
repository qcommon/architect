package therealfarfetchd.qcommon.architect.math;

import java.util.Objects;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

public final class Mat4 {

    public static final Mat4 IDENTITY = new Mat4(
        1, 0, 0, 0,
        0, 1, 0, 0,
        0, 0, 1, 0,
        0, 0, 0, 1
    );

    public final float c00, c01, c02, c03;
    public final float c10, c11, c12, c13;
    public final float c20, c21, c22, c23;
    public final float c30, c31, c32, c33;

    private Vec4 r0, r1, r2, r3;
    private Vec4 c0, c1, c2, c3;

    public Mat4(
        float c00, float c01, float c02, float c03,
        float c10, float c11, float c12, float c13,
        float c20, float c21, float c22, float c23,
        float c30, float c31, float c32, float c33
    ) {
        this.c00 = c00;
        this.c01 = c01;
        this.c02 = c02;
        this.c03 = c03;
        this.c10 = c10;
        this.c11 = c11;
        this.c12 = c12;
        this.c13 = c13;
        this.c20 = c20;
        this.c21 = c21;
        this.c22 = c22;
        this.c23 = c23;
        this.c30 = c30;
        this.c31 = c31;
        this.c32 = c32;
        this.c33 = c33;
    }

    public Mat4 translate(float x, float y, float z) {
        return mul(new Mat4(
            1, 0, 0, x,
            0, 1, 0, y,
            0, 0, 1, z,
            0, 0, 0, 1
        ));
    }

    public Mat4 scale(float x, float y, float z) {
        return mul(new Mat4(
            x, 0, 0, 0,
            0, y, 0, 0,
            0, 0, z, 0,
            0, 0, 0, 1
        ));
    }

    public Mat4 rotate(float x, float y, float z, float angle) {
        float c = cosd(-angle);
        float s = sind(-angle);
        float t = 1 - c;

        return mul(new Mat4(
            t * x * x + c, t * x * y - s * z, t * x * z + s * y, 0f,
            t * x * y + s * z, t * y * y + c, t * y * z - s * x, 0f,
            t * x * z - s * y, t * y * z + s * x, t * z * z + c, 0f,
            0f, 0f, 0f, 1f
        ));
    }

    public Mat4 translate(Vec3 xyz) {
        return translate(xyz.x, xyz.y, xyz.z);
    }

    public Mat4 mul(Mat4 other) {
        return new Mat4(
            getR0().dot(other.getC0()), getR0().dot(other.getC1()), getR0().dot(other.getC2()), getR0().dot(other.getC3()),
            getR1().dot(other.getC0()), getR1().dot(other.getC1()), getR1().dot(other.getC2()), getR1().dot(other.getC3()),
            getR2().dot(other.getC0()), getR2().dot(other.getC1()), getR2().dot(other.getC2()), getR2().dot(other.getC3()),
            getR3().dot(other.getC0()), getR3().dot(other.getC1()), getR3().dot(other.getC2()), getR3().dot(other.getC3())
        );
    }

    public Vec4 mul(Vec4 other) {
        return new Vec4(getR0().dot(other), getR1().dot(other), getR2().dot(other), getR3().dot(other));
    }

    public Vec3 mul(Vec3 other) {
        return mul(other.toVec4()).toVec3();
    }

    // @formatter:off
    public Vec4 getR0() { if (r0 == null) r0 = new Vec4(c00, c01, c02, c03); return r0; }
    public Vec4 getR1() { if (r1 == null) r1 = new Vec4(c10, c11, c12, c13); return r1; }
    public Vec4 getR2() { if (r2 == null) r2 = new Vec4(c20, c21, c22, c23); return r2; }
    public Vec4 getR3() { if (r3 == null) r3 = new Vec4(c30, c31, c32, c33); return r3; }
    public Vec4 getC0() { if (c0 == null) c0 = new Vec4(c00, c10, c20, c30); return c0; }
    public Vec4 getC1() { if (c1 == null) c1 = new Vec4(c01, c11, c21, c31); return c1; }
    public Vec4 getC2() { if (c2 == null) c2 = new Vec4(c02, c12, c22, c32); return c2; }
    public Vec4 getC3() { if (c3 == null) c3 = new Vec4(c03, c13, c23, c33); return c3; }
    // @formatter:on

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mat4 mat4 = (Mat4) o;
        return Float.compare(mat4.c00, c00) == 0 &&
            Float.compare(mat4.c01, c01) == 0 &&
            Float.compare(mat4.c02, c02) == 0 &&
            Float.compare(mat4.c03, c03) == 0 &&
            Float.compare(mat4.c10, c10) == 0 &&
            Float.compare(mat4.c11, c11) == 0 &&
            Float.compare(mat4.c12, c12) == 0 &&
            Float.compare(mat4.c13, c13) == 0 &&
            Float.compare(mat4.c20, c20) == 0 &&
            Float.compare(mat4.c21, c21) == 0 &&
            Float.compare(mat4.c22, c22) == 0 &&
            Float.compare(mat4.c23, c23) == 0 &&
            Float.compare(mat4.c30, c30) == 0 &&
            Float.compare(mat4.c31, c31) == 0 &&
            Float.compare(mat4.c32, c32) == 0 &&
            Float.compare(mat4.c33, c33) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(c00, c01, c02, c03, c10, c11, c12, c13, c20, c21, c22, c23, c30, c31, c32, c33);
    }

    @Override
    public String toString() {
        return String.format("mat4(%f, %f, %f, %f; %f, %f, %f, %f; %f, %f, %f, %f; %f, %f, %f, %f)", c00, c01, c02, c03, c10, c11, c12, c13, c20, c21, c22, c23, c30, c31, c32, c33);
    }

    private static float cosd(float angle) {
        return (float) cos(angle * (2 * PI) / 360.0);
    }

    private static float sind(float angle) {
        return (float) sin(angle * (2 * PI) / 360.0);
    }

}
