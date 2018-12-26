package therealfarfetchd.qcommon.architect.model;

import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.croco.Mat4;

public interface Transform {

    AffineTransform IDENTITY = sp -> Mat4.IDENTITY;

    Face transform(StateProvider sp, Face face);

}
