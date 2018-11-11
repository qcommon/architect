package therealfarfetchd.qcommon.architect.model.part;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.model.Face;

public class PartOBJ implements Part {

    private final OBJRoot obj;

    public PartOBJ(OBJRoot obj) {
        this.obj = obj;
    }

    @Override
    public List<Face> getFaces() {
        // TODO
        return Collections.emptyList();
    }

}
