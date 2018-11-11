package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Face;

public interface Part {

    Part EMPTY = new EmptyPart();

    List<Face> getFaces();

}
