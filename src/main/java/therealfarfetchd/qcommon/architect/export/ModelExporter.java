package therealfarfetchd.qcommon.architect.export;

import therealfarfetchd.qcommon.architect.model.Model;

public interface ModelExporter<T> {

    T export(Model model);

}
