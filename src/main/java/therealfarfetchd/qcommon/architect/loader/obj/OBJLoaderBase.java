package therealfarfetchd.qcommon.architect.loader.obj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.loader.GenLoader;
import therealfarfetchd.qcommon.architect.loader.ParseMessageContainer;

public abstract class OBJLoaderBase<T> extends GenLoader<T, List<String>> {

    protected static final Pattern regex = Pattern.compile("\\s*#.*$");

    @Nullable
    @Override
    protected List<String> loadSourceFromStream(ParseMessageContainer log, InputStream stream) {
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

}
