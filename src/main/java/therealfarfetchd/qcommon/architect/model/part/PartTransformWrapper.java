package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.croco.Vec3;

public class PartTransformWrapper implements Part {

    private final Part wrapped;
    private final List<Transform> transforms;

    public PartTransformWrapper(Part wrapped, List<Transform> transforms) {
        this.wrapped = wrapped;
        this.transforms = transforms;
    }

    @Override
    public List<Face> getFaces(StateProvider sp) {
        return wrapped.getFaces(sp).parallelStream().map(f -> transform(sp, f)).collect(Collectors.toList());
    }

    private Face transform(StateProvider sp, Face f) {
        f = f.translate(new Vec3(-0.5f, -0.5f, -0.5f));
        for (Transform t : transforms) {
            f = t.transform(sp, f);
        }
        f = f.translate(new Vec3(0.5f, 0.5f, 0.5f));
        return f;
    }

}
