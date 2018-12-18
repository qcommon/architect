package therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder;

import java.util.stream.Stream;

import therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.ShaderBuilder.Expr;
import therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.ShaderBuilder.Var;

abstract class GenExpr<T> implements Expr<T> {

    private final GLSLDataType<T> resultType;

    GenExpr(GLSLDataType<T> resultType) {
        this.resultType = resultType;
    }

    @Override
    public GLSLDataType<T> getDataType() {
        return resultType;
    }

}

class GenInfixExpr<T> extends GenExpr<T> {

    private final Expr<?> p1;
    private final Expr<?> p2;
    private final String op;

    GenInfixExpr(GLSLDataType<T> resultType, Expr<?> p1, Expr<?> p2, String op) {
        super(resultType);
        this.p1 = p1;
        this.p2 = p2;
        this.op = op;
    }

    @Override
    public String getExpression() {
        return String.format("(%s %s %s)", p1.getExpression(), op, p2.getExpression());
    }

}

class GenAccessExpr<T> extends GenExpr<T> {

    private final Expr<?> p1;
    private final String property;

    GenAccessExpr(GLSLDataType<T> resultType, Expr<?> p1, String property) {
        super(resultType);
        this.p1 = p1;
        this.property = property;
    }

    @Override
    public String getExpression() {
        return String.format("%s.%s", p1.getExpression(), property);
    }

}

class GenFnCall<T> extends GenExpr<T> {

    private final String fn;
    private final Expr<?>[] params;

    GenFnCall(GLSLDataType<T> resultType, String fn, Expr<?>... params) {
        super(resultType);
        this.fn = fn;
        this.params = params;
    }

    @Override
    public String getExpression() {
        String p = Stream.of(params).map(Expr::getExpression).reduce((a, b) -> String.format("%s, %s", a, b)).orElse("");
        return String.format("%s(%s)", fn, p);
    }
}

class GenConstExpr<T> extends GenExpr<T> {

    private final T t;

    GenConstExpr(GLSLDataType<T> resultType, T t) {
        super(resultType);
        this.t = t;
    }

    @Override
    public String getExpression() {
        return t.toString();
    }

}

class LocalVar<T> implements Var<T> {

    private final GLSLDataType<T> type;
    private final String varName;

    LocalVar(GLSLDataType<T> type, String varName) {
        this.type = type;
        this.varName = varName;
    }

    @Override
    public GLSLDataType<T> getDataType() {
        return type;
    }

    @Override
    public String getExpression() {
        return varName;
    }

    @Override
    public String getName() {
        return varName;
    }

}