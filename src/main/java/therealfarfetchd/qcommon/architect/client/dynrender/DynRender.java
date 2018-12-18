package therealfarfetchd.qcommon.architect.client.dynrender;

import net.minecraft.entity.Entity;

public interface DynRender {

    void draw(InputProvider ip, Entity camera, float delta, float x, float y, float z);

    void destroy();

    boolean isValid();

    interface Input<T> {}

    interface InputProvider {

        <T> T get(Input<T> input);

    }

}
