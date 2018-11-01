package therealfarfetchd.qcommon.architect.model;

import java.util.List;
import java.util.stream.Collectors;

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
        for (Transform t : transforms) {
            f = t.transform(f);
        }
        return f;
    }

}
