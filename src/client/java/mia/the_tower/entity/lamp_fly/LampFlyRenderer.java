package mia.the_tower.entity.lamp_fly;

import mia.the_tower.entity.void_moth.VoidMothModel;
import mia.the_tower.entity.void_moth.VoidMothRenderState;
import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LampFlyRenderer extends MobEntityRenderer<LampFlyEntity, LampFlyRenderState, LampFlyModel>{
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

    public LampFlyRenderer(EntityRendererFactory.Context context) {
        super(context, new LampFlyModel(context.getPart(LampFlyModel.LAMP_FLY)), 0.1f); //the last one is the shadow radius I believe
        this.addFeature(new EmissiveLayer<>(this, Identifier.of("the_tower","textures/entity/lamp_fly/lamp_fly.png")));

    }

    @Override
    public Identifier getTexture(LampFlyRenderState state) {
        return Identifier.of("the_tower", "textures/entity/lamp_fly/lamp_fly.png");
        //return LOCATION_BY_VARIANT.get(state.variant); //this is for variants
    }

    @Override
    public void render(LampFlyRenderState state, MatrixStack matrixStack,
                       VertexConsumerProvider vertexConsumerProvider, int i) {
        if(state.baby) {
            matrixStack.scale(0.3f, 0.3f, 0.3f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }

        super.render(state, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public LampFlyRenderState createRenderState() {
        return new LampFlyRenderState();
    }

    @Override
    public void updateRenderState(LampFlyEntity livingEntity, LampFlyRenderState livingEntityRenderState, float f) {
        super.updateRenderState(livingEntity, livingEntityRenderState, f);
        livingEntityRenderState.idleAnimationState.copyFrom(livingEntity.idleAnimationState);
        livingEntityRenderState.flyAnimationState.copyFrom(livingEntity.flyAnimationState);
        livingEntityRenderState.airborne = livingEntity.isInAir();

        //livingEntityRenderState.variant = livingEntity.getVariant();

    }

}
