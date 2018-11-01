package therealfarfetchd.qcommon.architect.math;

import java.util.Objects;

public final class Vec4 {

    public static final Vec4 ORIGIN = new Vec4(0, 0, 0, 0);

    public final float x;
    public final float y;
    public final float z;
    public final float w;

    public Vec4(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4 add(Vec4 other) {
        return new Vec4(this.x + other.x, this.y + other.y, this.z + other.z, this.w + other.w);
    }

    public Vec4 sub(Vec4 other) {
        return new Vec4(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
    }

    public Vec4 mul(Vec4 other) {
        return new Vec4(this.x * other.x, this.y * other.y, this.z * other.z, this.w * other.w);
    }

    public Vec4 div(Vec4 other) {
        return new Vec4(x / other.x, this.y / other.y, this.z / other.z, this.w / other.w);
    }

    public float dot(Vec4 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z + this.w * other.w;
    }

    public Vec3 toVec3() {
        return new Vec3(x / w, y / w, z / w);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec4 vec4 = (Vec4) o;
        return Float.compare(vec4.x, x) == 0 &&
            Float.compare(vec4.y, y) == 0 &&
            Float.compare(vec4.z, z) == 0 &&
            Float.compare(vec4.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f, %f)", x, y, z, w);
    }

}
