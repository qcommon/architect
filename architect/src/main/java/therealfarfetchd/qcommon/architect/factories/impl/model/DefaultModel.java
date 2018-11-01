package therealfarfetchd.qcommon.architect.factories.impl.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.Part;

public class DefaultModel implements Model {

    private final List<Part> parts;

    public DefaultModel(List<Part> parts) {
        this.parts = parts;
    }

    @Override
    public List<Part> getParts() {
        return parts;
    }

}
