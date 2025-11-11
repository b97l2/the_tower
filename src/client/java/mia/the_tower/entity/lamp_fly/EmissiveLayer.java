package mia.the_tower.entity.lamp_fly;

import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EmissiveLayer<S extends LampFlyRenderState, M extends LampFlyModel>
        extends FeatureRenderer<S, M> {

    private final RenderLayer layer;

    public EmissiveLayer(FeatureRendererContext<S, M> ctx, Identifier tex) {
        super(ctx);
        this.layer = RenderLayer.getEyes(tex); // full-bright render layer
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vcp, int light,
                       S entity, float limbAngle, float limbDist) {
        var vc = vcp.getBuffer(layer);
        this.getContextModel().render(
            matrices, vc,
            LightmapTextureManager.MAX_LIGHT_COORDINATE, // full-bright
            OverlayTexture.DEFAULT_UV
        );
    }
}
