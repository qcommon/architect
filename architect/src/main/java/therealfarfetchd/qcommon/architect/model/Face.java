package therealfarfetchd.qcommon.architect.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.math.Mat4;
import therealfarfetchd.qcommon.architect.math.Vec3;

public interface Face {

    List<Quad> toQuads();

    List<Tri> toTris();

    default Face translate(Vec3 offset) {
        return transform(Mat4.IDENTITY.translate(offset));
    }

    default Face rotate(Vec3 axis, float angle) {
        return transform(Mat4.IDENTITY.rotate(axis.x, axis.y, axis.z, angle));
    }

    default Face scale(Vec3 scale) {
        return transform(Mat4.IDENTITY.scale(scale.x, scale.y, scale.z));
    }

    Face transform(Mat4 mat);

}
