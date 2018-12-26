package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.model.AffineTransform;
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

    @Nullable
    @Override
    public RawPart getRaw() {
        final RawPart wrappedRaw = wrapped.getRaw();

        if (wrappedRaw != null) {
            return new RawWrapper(wrappedRaw);
        }

        return null;
    }

    private static class RawWrapper implements RawPart {

        private final RawPart wrapped;

        private RawWrapper(RawPart wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public List<Face> getFaces() {
            return wrapped.getFaces();
        }

        @Override
        public AffineTransform getTransform(StateProvider sp) {
            return wrapped.getTransform(sp); // TODO
        }

        @Override
        public ValueVariance<AffineTransform> getTransformVariance() {
            return wrapped.getTransformVariance(); // TODO
        }

        @Override
        public boolean showFace(StateProvider sp, int face) {
            return wrapped.showFace(sp, face);
        }

    }

}
