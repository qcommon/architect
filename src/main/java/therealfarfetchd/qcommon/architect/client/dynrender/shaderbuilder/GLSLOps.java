package therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder;

import therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.ShaderBuilder.Expr;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec4;

public class GLSLOps {

    public static <T> Expr<T> cnst(GLSLDataType<T> type, T t) {
        return new GenConstExpr<>(type, t);
    }

    public static Expr<Boolean> cnst(boolean b) {
        return new GenConstExpr<>(GLSLDataType.BOOL, b);
    }

    public static Expr<Float> cnst(float f) {
        return new GenConstExpr<>(GLSLDataType.FLOAT, f);
    }

    public static <T> Expr<Boolean> eq(Expr<T> a, Expr<T> b) {
        return new GenInfixExpr<>(GLSLDataType.BOOL, a, b, "==");
    }

    public static <T> Expr<Boolean> lt(Expr<T> a, Expr<T> b) {
        return new GenInfixExpr<>(GLSLDataType.BOOL, a, b, "<");
    }

    public static <T> Expr<Boolean> le(Expr<T> a, Expr<T> b) {
        return new GenInfixExpr<>(GLSLDataType.BOOL, a, b, "<=");
    }

    public static <T> Expr<Boolean> gt(Expr<T> a, Expr<T> b) {
        return new GenInfixExpr<>(GLSLDataType.BOOL, a, b, ">");
    }

    public static <T> Expr<Boolean> ge(Expr<T> a, Expr<T> b) {
        return new GenInfixExpr<>(GLSLDataType.BOOL, a, b, ">=");
    }

    public static Expr<Vec4> mmulv(Expr<Mat4> mat, Expr<Vec4> vec) {
        return new GenInfixExpr<>(GLSLDataType.VEC4, mat, vec, "*");
    }

    public static Expr<Vec2> xy(Expr<?> vec) {
        return new GenAccessExpr<>(GLSLDataType.VEC2, vec, "xy");
    }

    public static Expr<Float> x(Expr<?> vec) {
        return new GenAccessExpr<>(GLSLDataType.FLOAT, vec, "x");
    }

    public static Expr<Float> y(Expr<?> vec) {
        return new GenAccessExpr<>(GLSLDataType.FLOAT, vec, "y");
    }

    public static Expr<Vec2> vec2(Expr<Float> x, Expr<Float> y) {
        return new GenFnCall<>(GLSLDataType.VEC2, "vec2", x, y);
    }

    public static Expr<Float> faddf(Expr<Float> a, Expr<Float> b) {
        return new GenInfixExpr<>(GLSLDataType.FLOAT, a, b, "+");
    }

    public static Expr<Float> fsubf(Expr<Float> a, Expr<Float> b) {
        return new GenInfixExpr<>(GLSLDataType.FLOAT, a, b, "-");
    }

    public static Expr<Float> fmulf(Expr<Float> a, Expr<Float> b) {
        return new GenInfixExpr<>(GLSLDataType.FLOAT, a, b, "*");
    }

    public static Expr<Float> fdivf(Expr<Float> a, Expr<Float> b) {
        return new GenInfixExpr<>(GLSLDataType.FLOAT, a, b, "/");
    }

}
