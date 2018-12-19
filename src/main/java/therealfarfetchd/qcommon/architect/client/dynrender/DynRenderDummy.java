package therealfarfetchd.qcommon.architect.client.dynrender;

import net.minecraft.entity.Entity;

public class DynRenderDummy implements DynRender {

    @Override
    public void draw(InputProvider ip, Entity camera, float delta, double x, double y, double z) {}

    @Override
    public void destroy() {}

    @Override
    public boolean isValid() {
        return true;
    }

}
