package therealfarfetchd.qcommon.architect.math;

import java.util.Objects;

public final class Vec3 {

    public static final Vec3 ORIGIN = new Vec3(0, 0, 0);

    public final float x;
    public final float y;
    public final float z;

    private float length = Float.NaN;
    private Vec3 normalized;

    public Vec3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public Vec3 sub(Vec3 other) {
        return new Vec3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public Vec3 mul(Vec3 other) {
        return new Vec3(this.x * other.x, this.y * other.y, this.z * other.z);
    }

    public Vec3 div(Vec3 other) {
        return new Vec3(x / other.x, this.y / other.y, this.z / other.z);
    }

    public float dot(Vec3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vec3 cross(Vec3 other) {
        float cx = this.y * other.z - this.z * other.y;
        float cy = this.z * other.x - this.x * other.z;
        float cz = this.x * other.y - this.y * other.x;
        return new Vec3(cx, cy, cz);
    }

    public float getLength() {
        if (Float.isNaN(length)) {
            length = (float) Math.sqrt(x * x + y * y + z * z);
        }

        return length;
    }

    public Vec3 getNormalized() {
        if (normalized == null) {
            normalized = new Vec3(x / getLength(), y / getLength(), z / getLength());
            normalized.length = 1;
        }

        return normalized;
    }

    public Vec4 toVec4() {
        return new Vec4(x, y, z, 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3 vec3 = (Vec3) o;
        return Float.compare(vec3.x, x) == 0 &&
            Float.compare(vec3.y, y) == 0 &&
            Float.compare(vec3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("(%f, %f, %f)", x, y, z);
    }

}
