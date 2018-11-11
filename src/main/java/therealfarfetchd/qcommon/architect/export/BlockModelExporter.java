package therealfarfetchd.qcommon.architect.export;

import therealfarfetchd.qcommon.architect.model.Model;

public class BlockModelExporter implements ModelExporter<BlockModelExporter.ExportData> {

    public static BlockModelExporter INSTANCE = new BlockModelExporter();

    @Override
    public ExportData export(Model model) {
        return null;
    }

    public static class ExportData {

    }

}
