package mia.the_tower.entity;

import com.google.common.collect.Maps;
import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class VoidMothRenderer extends MobEntityRenderer<VoidMothEntity, VoidMothRenderState, VoidMothModel>{
    //displays the mob

    //for variants
    /*
    private static final Map<VoidMothVariant, Identifier> LOCATION_BY_VARIANT =
            Util.make(Maps.newEnumMap(VoidMothVariant.class), map -> {
                map.put(VoidMothVariant.DEFAULT,
                        Identifier.of("the_tower", "textures/entity/void_moth/mantis.png"));
                map.put(VoidMothVariant.ORCHID,
                        Identifier.of("the_tower", "textures/entity/mantis/mantis_orchid.png"));
            });

     */

    public VoidMothRenderer(EntityRendererFactory.Context context) {
        super(context, new VoidMothModel(context.getPart(VoidMothModel.VOID_MOTH)), 0.3f); //the last one is the shadow radius I believe
    }

    @Override
    public Identifier getTexture(VoidMothRenderState state) {
        return Identifier.of("the_tower", "textures/entity/void_moth/void_moth.png");
        //return LOCATION_BY_VARIANT.get(state.variant); //this is for variants
    }

    @Override
    public void render(VoidMothRenderState state, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(state.baby) {
            matrixStack.scale(0.3f, 0.3f, 0.3f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(state, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public VoidMothRenderState createRenderState() {
        return new VoidMothRenderState();
    }

    @Override
    public void updateRenderState(VoidMothEntity livingEntity, VoidMothRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.idleAnimationState.copyFrom(livingEntity.idleAnimationState);
        livingEntityRenderState.flyAnimationState.copyFrom(livingEntity.flyAnimationState);
        livingEntityRenderState.airborne = livingEntity.isInAir();

        //livingEntityRenderState.variant = livingEntity.getVariant();

        livingEntityRenderState.perched = livingEntity.isPerched();
        livingEntityRenderState.perchFace = livingEntity.getPerchFace();
        if (livingEntityRenderState.perched && !livingEntityRenderState.idleAnimationState.isRunning()) {
            livingEntityRenderState.idleAnimationState.start(livingEntity.age);
        } else if (!livingEntityRenderState.perched) {
            livingEntityRenderState.idleAnimationState.stop();
        }
    }

}
