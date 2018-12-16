package therealfarfetchd.qcommon.architect.loader;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import therealfarfetchd.qcommon.architect.Architect;

public class ParseContext {

    public enum MsgType {
        INFO(Level.INFO),
        WARN(Level.WARN),
        ERROR(Level.ERROR, true);

        public final boolean shouldFail;

        public final Level level;

        MsgType(Level level, boolean shouldFail) {
            this.shouldFail = shouldFail;
            this.level = level;
        }

        MsgType(Level level) {
            this(level, false);
        }
    }

    public final String task;

    private int errorCount = 0;

    private List<Entry> l = new ArrayList<>();

    public ParseContext(String task) {
        this.task = task;
    }

    public void msg(MsgType type, String message) {
        if (type.shouldFail) errorCount++;

        l.add(new Entry(type, message));
    }

    public void info(String message) {
        msg(MsgType.INFO, message);
    }

    public void warn(String message) {
        msg(MsgType.WARN, message);
    }

    public void error(String message) {
        msg(MsgType.ERROR, message);
    }

    public void into(ParseContext other) {
        for (Entry entry : l) {
            other.msg(entry.mt, entry.message);
        }
    }

    public void printMessages() {
        if (l.isEmpty()) return;
        Architect.INSTANCE.logger.info("Messages for '{}':", task);
        for (Entry entry : l) {
            Architect.INSTANCE.logger.log(entry.mt.level, String.format("%s", entry.message));
        }
        Architect.INSTANCE.logger.info("{} errors occurred.", errorCount);
    }

    public boolean isResultValid() {
        return errorCount == 0;
    }

    private static class Entry {
        public final MsgType mt;
        public final String message;

        private Entry(MsgType mt, String message) {
            this.mt = mt;
            this.message = message;
        }
    }

}
