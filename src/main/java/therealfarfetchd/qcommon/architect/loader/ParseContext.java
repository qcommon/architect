package therealfarfetchd.qcommon.architect.loader;

public class ParseContext {

    public final ParseMessageContainer log;
    public final DataParserExt dp;
    public final float posScale;

    private ParseContext(String task) {
        this(task, 1f);
    }

    private ParseContext(String task, float posScale) {
        this.log = new ParseMessageContainer(task);
        this.posScale = posScale;
        this.dp = new DataParserExt(this);
    }

    private ParseContext(ParseMessageContainer log, float posScale) {
        this.log = log;
        this.dp = new DataParserExt(this);
        this.posScale = posScale;
    }

    private ParseContext(ParseMessageContainer log) {
        this.log = log;
        this.posScale = 1f;
        this.dp = new DataParserExt(this);
    }

    public ParseContext withScale(float scale) {
        return new ParseContext(log, scale);
    }

    public static ParseContext wrap(DataParser dp, ParseMessageContainer log) {
        float scale = 1f;

        if (dp instanceof DataParserExt) {
            scale = ((DataParserExt) dp).ctx.posScale;
        }

        return new ParseContext(log, scale);
    }

    public static ParseContext wrap(ParseMessageContainer log) {
        return new ParseContext(log);
    }

    public static ParseContext create(String task) {
        return new ParseContext(task);
    }

}
