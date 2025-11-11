package mia.the_tower.entity.lamp_fly;

import mia.the_tower.entity.void_moth.VoidMothAnimations;
import mia.the_tower.entity.void_moth.VoidMothRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class LampFlyModel extends EntityModel<LampFlyRenderState> {


    public static final EntityModelLayer LAMP_FLY = new EntityModelLayer(Identifier.of("the_tower", "lamp_fly"), "main");

    private final ModelPart full;

    public LampFlyModel(ModelPart full) {
        super(full);
        this.full = root.getChild("full");
    }


        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            ModelPartData full = modelPartData.addChild("full", ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 18.0F, 0.0F));

            ModelPartData legs = full.addChild("legs", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 1.0F, 0.0F));

            ModelPartData cube_r1 = legs.addChild("cube_r1", ModelPartBuilder.create().uv(4, 4).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                    .uv(2, 6).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, -0.7F, 0.48F, 0.0F, 0.0F));

            ModelPartData cube_r2 = legs.addChild("cube_r2", ModelPartBuilder.create().uv(4, 5).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                    .uv(4, 6).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.0F, 0.48F, 0.0F, 0.0F));

            ModelPartData cube_r3 = legs.addChild("cube_r3", ModelPartBuilder.create().uv(0, 6).cuboid(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F))
                    .uv(6, 4).cuboid(-1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, 0.0F, 0.8F, 0.48F, 0.0F, 0.0F));

            ModelPartData antenaeleft = full.addChild("antenaeleft", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

            ModelPartData cube_r4 = antenaeleft.addChild("cube_r4", ModelPartBuilder.create().uv(2, 4).cuboid(-1.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, -0.9F, 0.0F, 0.0F, -0.3491F));

            ModelPartData antenaeright = full.addChild("antenaeright", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

            ModelPartData cube_r5 = antenaeright.addChild("cube_r5", ModelPartBuilder.create().uv(0, 4).cuboid(0.0F, -2.0F, 0.0F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F)), ModelTransform.of(0.0F, -1.0F, -0.9F, 0.0F, 0.0F, 0.3491F));
            return TexturedModelData.of(modelData, 8, 8);
        }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -7.0F, 7.0F);
        headPitch = MathHelper.clamp(headPitch, -7.0F, 7.0F);

        this.full.yaw = headYaw * 0.017453292F;
        this.full.pitch = headPitch * 0.017453292F;
    }

    @Override
    public void setAngles(LampFlyRenderState state) {
        super.setAngles(state);
        this.setHeadAngles(state.yawDegrees, state.pitch);

        this.setHeadAngles(state.yawDegrees, state.pitch);

        if (state.airborne) {
            // Airborne: flap
            this.animate(state.flyAnimationState, LampFlyAnimations.FLY, state.age, 1.0f);
        }
        this.animate(state.idleAnimationState, LampFlyAnimations.FLY, state.age, 1f);
    }
    }

