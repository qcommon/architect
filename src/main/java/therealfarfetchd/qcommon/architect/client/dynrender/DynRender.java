package therealfarfetchd.qcommon.architect.client.dynrender;

import net.minecraft.entity.Entity;

public interface DynRender {

    void draw(InputProvider ip, Entity camera, float delta, double x, double y, double z);

    void destroy();

    boolean isValid();

    interface Input<T> {}

    interface InputProvider {

        @Deprecated
        InputProvider NULL = new InputProvider() {

            @SuppressWarnings("ConstantConditions")
            @Override
            public <T> T get(Input<T> input) {
                return null;
            }

        };

        <T> T get(Input<T> input);

    }

}
