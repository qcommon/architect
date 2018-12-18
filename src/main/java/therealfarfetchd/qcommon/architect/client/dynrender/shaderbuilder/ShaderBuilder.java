package therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec4;

import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.cnst;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.faddf;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.fdivf;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.fsubf;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.ge;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.lt;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.mmulv;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.vec2;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.x;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.xy;
import static therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder.GLSLOps.y;

public class ShaderBuilder {

    private final String version;

    private final Set<String> extensions = new HashSet<>();

    private final Set<UniformImpl<?>> uniforms = new HashSet<>();
    private final Set<ShaderInputImpl<?>> inputs = new HashSet<>();
    private final Set<ShaderOutputImpl<?>> outputs = new HashSet<>();

    private String main;

    @Nullable private ScopeBuilderImpl scope = null;

    public ShaderBuilder(String version) {
        this.version = version;
    }

    public void useExtension(String extension) {
        extensions.add(extension);
    }

    public <T> Uniform<T> addUniform(String name, GLSLDataType<T> type) {
        UniformImpl<T> u = new UniformImpl<>(type, name);
        uniforms.add(u);
        return u;
    }

    public <T> ShaderInput<T> addInput(String name, GLSLDataType<T> type) {
        ShaderInputImpl<T> in = new ShaderInputImpl<>(type, name, false);
        inputs.add(in);
        return in;
    }

    public <T> ShaderOutput<T> addOutput(String name, GLSLDataType<T> type) {
        ShaderOutputImpl<T> in = new ShaderOutputImpl<>(type, name, false);
        outputs.add(in);
        return in;
    }

    public <T> ShaderInput<T> addImplicitInput(String name, GLSLDataType<T> type) {
        ShaderInputImpl<T> in = new ShaderInputImpl<>(type, name, true);
        inputs.add(in);
        return in;
    }

    public <T> ShaderOutput<T> addImplicitOutput(String name, GLSLDataType<T> type) {
        ShaderOutputImpl<T> in = new ShaderOutputImpl<>(type, name, true);
        outputs.add(in);
        return in;
    }

    public void main(Consumer<ScopeBuilder> mb) {
        scope = new ScopeBuilderImpl();
        mb.accept(new DynScopeBuilder());
        this.main = scope.getStatements();
        scope = null;
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append("#version ").append(version).append("\n");

        extensions.forEach(ext -> sb.append("#extension ").append(ext).append(": enable\n"));
        uniforms.stream().map(UniformImpl::define).forEach(sb::append);
        inputs.stream().map(ShaderInputImpl::define).forEach(sb::append);
        outputs.stream().map(ShaderOutputImpl::define).forEach(sb::append);

        sb.append("void main() {\n").append(main).append("}");

        return sb.toString();
    }

    interface Expr<T> {

        GLSLDataType<T> getDataType();

        String getExpression();

    }

    interface Var<T> extends Expr<T> {

        String getName();

    }

    interface ShaderInput<T> extends Expr<T> {}

    interface Uniform<T> extends ShaderInput<T> {}

    interface ShaderOutput<T> extends Var<T> {}

    private static class UniformImpl<T> implements Uniform<T> {

        private final GLSLDataType<T> type;
        private final String name;

        private UniformImpl(GLSLDataType<T> type, String name) {
            this.type = type;
            this.name = name;
        }

        public String define() {
            return String.format("uniform %s %s;\n", type.repr, name);
        }

        @Override
        public GLSLDataType<T> getDataType() {
            return type;
        }

        @Override
        public String getExpression() {
            return name;
        }

    }

    private static class ShaderInputImpl<T> implements ShaderInput<T> {

        private final GLSLDataType<T> type;
        private final String name;
        private final boolean implicit;

        private ShaderInputImpl(GLSLDataType<T> type, String name, boolean implicit) {
            this.type = type;
            this.name = name;
            this.implicit = implicit;
        }

        public String define() {
            if (implicit) return "";

            String pre = "";
            if (type == GLSLDataType.INT) pre += "flat ";
            return String.format("%sin %s %s;\n", pre, type.repr, name);
        }

        @Override
        public GLSLDataType<T> getDataType() {
            return type;
        }

        @Override
        public String getExpression() {
            return name;
        }

    }

    private static class ShaderOutputImpl<T> implements ShaderOutput<T> {

        private final GLSLDataType<T> type;
        private final String name;
        private final boolean implicit;

        private ShaderOutputImpl(GLSLDataType<T> type, String name, boolean implicit) {
            this.type = type;
            this.name = name;
            this.implicit = implicit;
        }

        public String define() {
            if (implicit) return "";

            String pre = "";
            if (type == GLSLDataType.INT) pre += "flat ";
            return String.format("%sout %s %s;\n", pre, type.repr, name);
        }

        @Override
        public GLSLDataType<T> getDataType() {
            return type;
        }

        @Override
        public String getExpression() {
            return name;
        }

        @Override
        public String getName() {
            return name;
        }

    }

    interface ScopeBuilder {

        <T> void set(Var<T> var, Expr<T> expr);

        void _if(Expr<Boolean> condition, Runnable a);

        void _if(Expr<Boolean> condition, Runnable a, Runnable b);

        <T> Expr<T> select(Expr<Boolean> condition, Supplier<Expr<T>> a, Supplier<Expr<T>> b);

        <T> Var<T> var(Expr<T> value);

    }

    private class DynScopeBuilder implements ScopeBuilder {

        @Override
        public <T> void set(Var<T> var, Expr<T> expr) {
            assert scope != null;
            scope.set(var, expr);
        }

        @Override
        public void _if(Expr<Boolean> condition, Runnable a) {
            assert scope != null;
            scope._if(condition, a);
        }

        @Override
        public void _if(Expr<Boolean> condition, Runnable a, Runnable b) {
            assert scope != null;
            scope._if(condition, a, b);
        }

        @Override
        public <T> Expr<T> select(Expr<Boolean> condition, Supplier<Expr<T>> a, Supplier<Expr<T>> b) {
            assert scope != null;
            return scope.select(condition, a, b);
        }

        @Override
        public <T> Var<T> var(Expr<T> value) {
            assert scope != null;
            return scope.var(value);
        }
    }

    private class ScopeBuilderImpl implements ScopeBuilder {

        private int varCounter = 0;

        private StringBuilder sb = new StringBuilder();

        private void addStatement(String s) {
            sb.append(s).append("\n");
        }

        @Override
        public <T> void set(Var<T> var, Expr<T> expr) {
            addStatement(String.format("%s = %s;", var.getName(), expr.getExpression()));
        }

        @Override
        public void _if(Expr<Boolean> condition, Runnable a) {
            ScopeBuilderImpl old = scope;
            scope = new ScopeBuilderImpl();
            a.run();
            addStatement(String.format("if (%s) {%s}", condition.getExpression(), scope.getStatements()));
            scope = old;
        }

        @Override
        public void _if(Expr<Boolean> condition, Runnable a, Runnable b) {
            ScopeBuilderImpl old = scope;
            scope = new ScopeBuilderImpl();
            a.run();
            String aCode = scope.getStatements();
            scope = new ScopeBuilderImpl();
            b.run();
            String bCode = scope.getStatements();
            addStatement(String.format("if (%s) {%s} else {%s}", condition.getExpression(), aCode, bCode));
            scope = old;
        }

        @Override
        public <T> Expr<T> select(Expr<Boolean> condition, Supplier<Expr<T>> a, Supplier<Expr<T>> b) {
            String varName = getVarName();

            ScopeBuilderImpl old = scope;
            scope = new ScopeBuilderImpl();
            Expr<T> res1 = a.get();
            String aCode = scope.getStatements();
            scope = new ScopeBuilderImpl();
            Expr<T> res2 = b.get();
            String bCode = scope.getStatements();
            addStatement(String.format("%s %s;", res1.getDataType().repr, varName));
            addStatement(String.format("if (%s) {%s%s = %s;} else {%s%s = %s;}", condition.getExpression(), aCode, varName, res1.getExpression(), bCode, varName, res2.getExpression()));
            scope = old;
            return new LocalVar<>(res1.getDataType(), varName);
        }

        @Override
        public <T> Var<T> var(Expr<T> value) {
            String varName = getVarName();
            addStatement(String.format("%s %s = %s;", value.getDataType(), varName, value.getExpression()));

            return new LocalVar<>(value.getDataType(), varName);
        }

        String getStatements() {
            return sb.toString();
        }

        private String getVarName() {
            return String.format("var%d", varCounter++);
        }

    }

    public static void test() {
        ShaderBuilder b = new ShaderBuilder("130");

        b.useExtension("GL_EXT_gpu_shader4");

        ShaderInput<Mat4> mvp = b.addImplicitInput("gl_ModelViewProjectionMatrix", GLSLDataType.MAT4);
        ShaderInput<Vec4> vert = b.addImplicitInput("gl_Vertex", GLSLDataType.VEC4);
        ShaderOutput<Vec4> pos = b.addImplicitOutput("gl_Position", GLSLDataType.VEC4);

        ShaderInput<Vec2> size = b.addUniform("size", GLSLDataType.VEC2);

        ShaderOutput<Vec2> vertPos = b.addOutput("vertPos", GLSLDataType.VEC2);
        ShaderOutput<Vec2> texc = b.addOutput("texc", GLSLDataType.VEC2);

        b.main(s -> {
            s.set(pos, mmulv(mvp, vert));
            s.set(vertPos, xy(vert));
            s.set(texc, vec2(fdivf(x(vert), x(size)), fsubf(cnst(1f), fdivf(y(vert), y(size)))));

            // testing if
            s._if(lt(cnst(1), cnst(4)), () -> {
                s.set(texc, vec2(cnst(5), cnst(3)));
            }, () -> {
                s.set(texc, vec2(cnst(145), cnst(111111)));
            });

            s.set(vertPos, s.select(ge(x(vert), y(vert)), () -> {
                return vec2(x(vert), faddf(x(vert), y(vert)));
            }, () -> {
                return vec2(faddf(x(vert), y(vert)), y(vert));
            }));
        });

        String code = b.build();
        System.out.println("\n" + code);
    }

}
