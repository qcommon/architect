package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.croco.Vec3;

public class PartTransformWrapper implements Part {

    private final Part wrapped;
    private final List<Transform> transforms;

    public PartTransformWrapper(Part wrapped, List<Transform> transforms) {
        this.wrapped = wrapped;
        this.transforms = transforms;
    }

    @Override
    public List<Face> getFaces() {
        return wrapped.getFaces().parallelStream().map(this::transform).collect(Collectors.toList());
    }

    private Face transform(Face f) {
        f = f.translate(new Vec3(-0.5f, -0.5f, -0.5f));
        for (Transform t : transforms) {
            f = t.transform(f);
        }
        f = f.translate(new Vec3(0.5f, 0.5f, 0.5f));
        return f;
    }

}
