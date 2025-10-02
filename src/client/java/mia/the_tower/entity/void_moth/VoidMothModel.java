package mia.the_tower.entity.void_moth;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;


public class VoidMothModel extends EntityModel<VoidMothRenderState>{
    //deals with how the mob looks like

        public static final EntityModelLayer VOID_MOTH = new EntityModelLayer(Identifier.of("the_tower", "void_moth"), "main");

        private final ModelPart full_head;
        private final ModelPart void_moth;

    public VoidMothModel(ModelPart void_moth) {
        super(void_moth);
        this.void_moth = root.getChild("void_moth");
        this.full_head = this.void_moth.getChild("full_head");


    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData void_moth = modelPartData.addChild("void_moth", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 21.0F, -5.0F));

        ModelPartData right_wing = void_moth.addChild("right_wing", ModelPartBuilder.create(), ModelTransform.pivot(-1.0F, 1.0F, 4.0F));

        ModelPartData cube_r1 = right_wing.addChild("cube_r1", ModelPartBuilder.create().uv(0, 28).cuboid(-21.0F, 0.0F, 0.0F, 21.0F, 0.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(-0.5F, -3.0F, -3.0F, 0.0F, -0.0873F, -0.1745F));

        ModelPartData left_wing = void_moth.addChild("left_wing", ModelPartBuilder.create(), ModelTransform.pivot(1.0F, 1.0F, 4.0F));

        ModelPartData cube_r2 = left_wing.addChild("cube_r2", ModelPartBuilder.create().uv(0, 48).cuboid(0.0F, 0.0F, 0.0F, 21.0F, 0.0F, 20.0F, new Dilation(0.0F)), ModelTransform.of(0.5F, -3.0F, -3.0F, 0.0F, 0.0873F, 0.1745F));

        ModelPartData body = void_moth.addChild("body", ModelPartBuilder.create().uv(0, 68).cuboid(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 6.0F, new Dilation(0.0F))
                .uv(34, 68).cuboid(-1.5F, -3.5F, 2.5F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 4.0F));

        ModelPartData lower_wings = void_moth.addChild("lower_wings", ModelPartBuilder.create().uv(0, 0).cuboid(-9.0F, -2.0F, -3.0F, 19.0F, 0.0F, 28.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 2.0F, 4.0F));

        ModelPartData full_head = void_moth.addChild("full_head", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 3.0F, 5.0F));

        ModelPartData head = full_head.addChild("head", ModelPartBuilder.create().uv(20, 68).cuboid(-1.5F, -4.0F, -6.0F, 3.0F, 3.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, -0.5F, 1.0F));

        ModelPartData right_antena = full_head.addChild("right_antena", ModelPartBuilder.create(), ModelTransform.pivot(0.15F, -1.5F, -1.4F));

        ModelPartData cube_r3 = right_antena.addChild("cube_r3", ModelPartBuilder.create().uv(64, 68).cuboid(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.6F, -2.5F, -3.5F, 0.0F, 0.0436F, 0.4363F));

        ModelPartData left_antena = full_head.addChild("left_antena", ModelPartBuilder.create(), ModelTransform.pivot(-0.15F, -1.5F, -1.4F));

        ModelPartData cube_r4 = left_antena.addChild("cube_r4", ModelPartBuilder.create().uv(68, 68).cuboid(-1.0F, -6.0F, 0.0F, 2.0F, 6.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(-0.6F, -2.5F, -3.5F, 0.0F, -0.0436F, -0.4363F));

        ModelPartData front_legs = void_moth.addChild("front_legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 3.0F, 5.0F));

        ModelPartData cube_r5 = front_legs.addChild("cube_r5", ModelPartBuilder.create().uv(56, 68).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F))
                .uv(48, 68).cuboid(3.0F, 0.0F, -1.0F, 1.0F, 0.0F, 3.0F, new Dilation(0.0F)), ModelTransform.of(-2.0F, -0.7F, -3.0F, 0.6981F, 0.0F, 0.0F));

        ModelPartData back_legs = void_moth.addChild("back_legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 3.0F, 5.0F));

        ModelPartData cube_r6 = back_legs.addChild("cube_r6", ModelPartBuilder.create().uv(72, 70).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(48, 71).cuboid(-3.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -0.7F, -1.0F, -0.6981F, 0.0F, 0.0F));

        ModelPartData cube_r7 = back_legs.addChild("cube_r7", ModelPartBuilder.create().uv(72, 68).cuboid(0.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
                .uv(54, 71).cuboid(-3.0F, 0.0F, -1.0F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F)), ModelTransform.of(1.0F, -0.7F, 1.0F, -0.6981F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }


    @Override
    public void setAngles(VoidMothRenderState state) {
        super.setAngles(state);
        this.setHeadAngles(state.yawDegrees, state.pitch);

        this.setHeadAngles(state.yawDegrees, state.pitch);

        if (state.perched) {
            // Pose the whole model for the attachment
            switch (state.perchFace) {
                case UP -> { // hanging from ceiling
                    this.root.pitch = (float)Math.PI; // flip upside down
                }
                case NORTH, SOUTH, EAST, WEST -> {
                    // yaw the body to face out from the wall
                    float yawOut = state.perchFace.getPositiveHorizontalDegrees(); // maps dir -> degrees
                    this.root.yaw = (float)Math.toRadians(yawOut + 180f); // face away from wall
                    // optional: slight pitch toward the wall
                    this.root.pitch = 0.2f;
                }
                default -> {}
            }
            this.animate(state.idleAnimationState, VoidMothAnimations.IDLE, state.age, 1.0f);
            return;
        }

        if (state.airborne) {
            // Airborne: flap
            this.animate(state.flyAnimationState, VoidMothAnimations.FLY, state.age, 1.0f);
        } else {
            // On ground: walk cycle driven by limb fields (1.21.4 names)
            this.animateWalking(
                    VoidMothAnimations.WALK,
                    state.limbFrequency,
                    state.limbAmplitudeMultiplier,
                    2.0f,  // speed multiplier – increase if you want faster flaps while “walking”
                    1.0f   // degree – how strong the motion is
            );
        }
        this.animate(state.idleAnimationState, VoidMothAnimations.IDLE, state.age, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -7.0F, 7.0F);
        headPitch = MathHelper.clamp(headPitch, -7.0F, 7.0F);

        this.full_head.yaw = headYaw * 0.017453292F;
        this.full_head.pitch = headPitch * 0.017453292F;
    }


}


