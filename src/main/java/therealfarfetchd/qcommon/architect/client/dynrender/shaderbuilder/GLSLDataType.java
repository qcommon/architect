package therealfarfetchd.qcommon.architect.client.dynrender.shaderbuilder;

import java.nio.FloatBuffer;
import java.util.function.ObjIntConsumer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;
import therealfarfetchd.qcommon.croco.Vec4;

public class GLSLDataType<T> {

    private static final FloatBuffer buf = BufferUtils.createFloatBuffer(16);

    public static final GLSLDataType<Boolean> BOOL = define("bool", GLSLDataType::writeBool);
    public static final GLSLDataType<Integer> INT = define("int", GLSLDataType::writeInt);
    public static final GLSLDataType<Float> FLOAT = define("float", GLSLDataType::writeFloat);
    public static final GLSLDataType<Vec2> VEC2 = define("vec2", GLSLDataType::writeVec2);
    public static final GLSLDataType<Vec3> VEC3 = define("vec3", GLSLDataType::writeVec3);
    public static final GLSLDataType<Vec4> VEC4 = define("vec4", GLSLDataType::writeVec4);
    public static final GLSLDataType<Mat4> MAT4 = define("mat4", GLSLDataType::writeMat4);

    public static final GLSLDataType<Integer> SAMPLER_2D = define("sampler2D", GLSLDataType::writeInt);

    public final String repr;
    private final ObjIntConsumer<T> fillOp;

    private GLSLDataType(String repr, ObjIntConsumer<T> fillOp) {
        this.repr = repr;
        this.fillOp = fillOp;
    }

    public void writeData(int location, T t) {
        fillOp.accept(t, location);
    }

    @Override
    public String toString() {
        return repr;
    }

    private static <T> GLSLDataType<T> define(String glslRepr, ObjIntConsumer<T> fillOp) {
        return new GLSLDataType<>(glslRepr, fillOp);
    }

    private static void writeBool(boolean data, int location) {
        GL20.glUniform1i(location, data ? 1 : 0);
    }

    private static void writeInt(int data, int location) {
        GL20.glUniform1i(location, data);
    }

    private static void writeFloat(float data, int location) {
        GL20.glUniform1f(location, data);
    }

    private static void writeVec2(Vec2 data, int location) {
        GL20.glUniform2f(location, data.x, data.y);
    }

    private static void writeVec3(Vec3 data, int location) {
        GL20.glUniform3f(location, data.x, data.y, data.z);
    }

    private static void writeVec4(Vec4 data, int location) {
        GL20.glUniform4f(location, data.x, data.y, data.z, data.w);
    }

    private static void writeMat4(Mat4 mat, int location) {
        buf.clear();
        mat.intoBuffer(buf);
        GL20.glUniformMatrix4fv(location, false, buf);
    }

}
