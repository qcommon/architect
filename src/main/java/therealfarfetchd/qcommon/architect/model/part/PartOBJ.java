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
            TextureRef t = placeholder;
            if (face.tex != null) t = TextureRef.fromString(face.tex);
            switch (face.verts.size()) {
                case 3:
                    return new Tri(t, face.verts.get(0), face.verts.get(1), face.verts.get(2), face.rgba);
                case 4:
                    return new Quad(t, face.verts.get(0), face.verts.get(1), face.verts.get(2), face.verts.get(3), face.rgba);
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
