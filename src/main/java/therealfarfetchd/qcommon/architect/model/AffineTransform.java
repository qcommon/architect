package therealfarfetchd.qcommon.architect.model;

import therealfarfetchd.qcommon.croco.Mat4;

public interface AffineTransform extends Transform {

    @Override
    default Face transform(Face face) {
        return face.transform(getMatrix());
    }

    Mat4 getMatrix();

    static AffineTransform of(Mat4 mat) {
        return () -> mat;
    }

}
