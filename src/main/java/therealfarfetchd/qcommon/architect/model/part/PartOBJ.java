package therealfarfetchd.qcommon.architect.model.part;

import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.loader.obj.PreparedOBJ;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Tri;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefAbsolute;

public class PartOBJ implements Part {

    private final List<Face> faces;

    public PartOBJ(PreparedOBJ obj) {
        TextureRef placeholder = new TextureRefAbsolute(new ResourceLocation(Architect.MODID, "white"));

        faces = obj.objects.values().parallelStream().flatMap(Collection::parallelStream).map(face -> {
            switch (face.verts.size()) {
                case 3:
                    return new Tri(placeholder, face.verts.get(0), face.verts.get(1), face.verts.get(2));
                case 4:
                    return new Quad(placeholder, face.verts.get(0), face.verts.get(1), face.verts.get(2), face.verts.get(3));
                default:
                    throw new IllegalStateException("Don't know how to handle a face with " + face.verts.size() + " vertices!");
            }
        }).collect(Collectors.toList());
    }

    @Override
    public List<Face> getFaces() {
        return faces;
    }

}
