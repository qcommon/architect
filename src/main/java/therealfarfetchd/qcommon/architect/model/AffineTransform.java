package therealfarfetchd.qcommon.architect.model;

import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.croco.Mat4;

public interface AffineTransform extends Transform {

    @Override
    default Face transform(StateProvider sp, Face face) {
        return face.transform(getMatrix(sp));
    }

    Mat4 getMatrix(StateProvider sp);

    static AffineTransform of(Mat4 mat) {
        return sp -> mat;
    }

    static AffineTransform of(AffineTransform op) {
        return op;
    }

}
