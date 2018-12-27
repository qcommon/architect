package therealfarfetchd.qcommon.architect.model;

import java.util.Objects;

import therealfarfetchd.qcommon.croco.Vec3;

public class ModelTransformMap {

    public static final ModelTransformMap IDENTITY = new ModelTransformMap(ModelTransform.IDENTITY, ModelTransform.IDENTITY, ModelTransform.IDENTITY, ModelTransform.IDENTITY, ModelTransform.IDENTITY, ModelTransform.IDENTITY, ModelTransform.IDENTITY, ModelTransform.IDENTITY);

    public final ModelTransform thirdPersonLeftHand;
    public final ModelTransform thirdPersonRightHand;
    public final ModelTransform firstPersonLeftHand;
    public final ModelTransform firstPersonRightHand;
    public final ModelTransform head;
    public final ModelTransform gui;
    public final ModelTransform ground;
    public final ModelTransform fixed;

    public ModelTransformMap(ModelTransform thirdPersonLeftHand, ModelTransform thirdPersonRightHand, ModelTransform firstPersonLeftHand, ModelTransform firstPersonRightHand, ModelTransform head, ModelTransform gui, ModelTransform ground, ModelTransform fixed) {
        this.thirdPersonLeftHand = thirdPersonLeftHand;
        this.thirdPersonRightHand = thirdPersonRightHand;
        this.firstPersonLeftHand = firstPersonLeftHand;
        this.firstPersonRightHand = firstPersonRightHand;
        this.head = head;
        this.gui = gui;
        this.ground = ground;
        this.fixed = fixed;
    }

    @Override
    public String toString() {
        return "ModelTransformMap{" +
            "thirdPersonLeftHand=" + thirdPersonLeftHand +
            ", thirdPersonRightHand=" + thirdPersonRightHand +
            ", firstPersonLeftHand=" + firstPersonLeftHand +
            ", firstPersonRightHand=" + firstPersonRightHand +
            ", head=" + head +
            ", gui=" + gui +
            ", ground=" + ground +
            ", fixed=" + fixed +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelTransformMap that = (ModelTransformMap) o;
        return thirdPersonLeftHand.equals(that.thirdPersonLeftHand) &&
            thirdPersonRightHand.equals(that.thirdPersonRightHand) &&
            firstPersonLeftHand.equals(that.firstPersonLeftHand) &&
            firstPersonRightHand.equals(that.firstPersonRightHand) &&
            head.equals(that.head) &&
            gui.equals(that.gui) &&
            ground.equals(that.ground) &&
            fixed.equals(that.fixed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(thirdPersonLeftHand, thirdPersonRightHand, firstPersonLeftHand, firstPersonRightHand, head, gui, ground, fixed);
    }

    public static class ModelTransform {

        public static final ModelTransform IDENTITY = new ModelTransform(Vec3.ORIGIN, Vec3.ORIGIN, new Vec3(1, 1, 1));

        public final Vec3 rotation;
        public final Vec3 translation;
        public final Vec3 scale;

        public ModelTransform(Vec3 rotation, Vec3 translation, Vec3 scale) {
            this.rotation = rotation;
            this.translation = translation;
            this.scale = scale;
        }

        @Override
        public String toString() {
            return "ModelTransform{" +
                "rotation=" + rotation +
                ", translation=" + translation +
                ", scale=" + scale +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ModelTransform that = (ModelTransform) o;
            return rotation.equals(that.rotation) &&
                translation.equals(that.translation) &&
                scale.equals(that.scale);
        }

        @Override
        public int hashCode() {
            return Objects.hash(rotation, translation, scale);
        }

    }

    public enum Type {
        ORIGIN,
        THIRD_PERSON_LEFT_HAND,
        THIRD_PERSON_RIGHT_HAND,
        FIRST_PERSON_LEFT_HAND,
        FIRST_PERSON_RIGHT_HAND,
        HEAD,
        GUI,
        GROUND,
        FIXED,
    }

}
