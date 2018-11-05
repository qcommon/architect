package therealfarfetchd.qcommon.architect.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.value.Value;

public interface Part {

    Value<List<Face>> getFaces();

}
