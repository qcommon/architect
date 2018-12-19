package therealfarfetchd.qcommon.architect.client.dynrender;

import net.minecraft.entity.Entity;

import static com.mojang.blaze3d.platform.GLX.GL_ARRAY_BUFFER;
import static com.mojang.blaze3d.platform.GLX.glBindBuffer;
import static com.mojang.blaze3d.platform.GLX.glDeleteBuffers;
import static com.mojang.blaze3d.platform.GLX.glDeleteProgram;
import static com.mojang.blaze3d.platform.GLX.glUseProgram;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;

public class DynRenderImpl implements DynRender {

    private final int prog;
    private final int vbo;
    private final int vao;
    private final int count;

    private boolean valid = true;

    public DynRenderImpl(int prog, int vbo, int vao, int count) {
        this.prog = prog;
        this.vbo = vbo;
        this.vao = vao;
        this.count = count;
    }

    @Override
    public void draw(InputProvider ip, Entity camera, float delta, double x, double y, double z) {
        if (!isValid()) throw new IllegalStateException("Dynamic renderer not valid!");

        glUseProgram(prog);
        glBindVertexArray(vao);
        glBindBuffer(GL_ARRAY_BUFFER, vbo);

        glDrawArrays(GL_TRIANGLES, 0, count);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
        glUseProgram(0);
    }

    @Override
    public void destroy() {
        glDeleteProgram(prog);
        glDeleteBuffers(vbo);
        glDeleteVertexArrays(vao);
        valid = false;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

}
