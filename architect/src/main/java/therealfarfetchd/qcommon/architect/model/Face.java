package therealfarfetchd.qcommon.architect.model;

import net.minecraft.util.EnumFacing;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public interface Face {

    TextureRef getTexture();

    Vec3 getNormal();

    EnumFacing getFacing();

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
