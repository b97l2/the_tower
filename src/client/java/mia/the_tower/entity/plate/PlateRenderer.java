package mia.the_tower.entity.plate;

import mia.the_tower.initialisation.entity.custom.PlateEntity;
import net.fabricmc.fabric.api.client.model.loading.v1.FabricBakedModelManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.item.map.MapState;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class PlateRenderer<T extends PlateEntity> extends EntityRenderer<T, PlateRenderState> {
    public static final int field_32933 = 30;
    private final ItemModelManager itemModelManager;
    private final MapRenderer mapRenderer;
    private final BlockRenderManager blockRenderManager;

    private static final Identifier PLATE_MODEL = Identifier.of("the_tower", "block/plate");

    public PlateRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemModelManager = context.getItemModelManager();
        this.mapRenderer = context.getMapRenderer();
        this.blockRenderManager = context.getBlockRenderManager();
    }



    public void render(PlateRenderState plateRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        super.render(plateRenderState, matrixStack, vertexConsumerProvider, i);
        matrixStack.push();
        if (!plateRenderState.invisible) {
            matrixStack.push();
            matrixStack.translate(-0.5F, -0.03125F, -0.5F);
            var baked = ((FabricBakedModelManager)blockRenderManager.getModels().getModelManager()).getModel(PLATE_MODEL);
            this.blockRenderManager.getModelRenderer().render(matrixStack.peek(), vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE)), (BlockState)null, baked, 1.0F, 1.0F, 1.0F, i, OverlayTexture.DEFAULT_UV);
            matrixStack.pop();
        }

        matrixStack.translate(0.0F, 0.0F, 0.0F);
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));

        if (plateRenderState.mapId != null) {
            int j = plateRenderState.rotation % 4 * 2;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)j * 360.0F / 8.0F));
            matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
            matrixStack.translate(-64.0F, -64.0F, 0.0F);
            int k = this.getLight(plateRenderState.glow, 15728850, i);
            this.mapRenderer.draw(plateRenderState.mapRenderState, matrixStack, vertexConsumerProvider, true, k);
        } else if (!plateRenderState.itemRenderState.isEmpty()) {
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)plateRenderState.rotation * 360.0F / 8.0F));
            int j = this.getLight(plateRenderState.glow, 15728880, i);
            matrixStack.scale(0.5F, 0.5F, 0.5F);
            plateRenderState.itemRenderState.render(matrixStack, vertexConsumerProvider, j, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();
    }

    private int getLight(boolean glow, int glowLight, int regularLight) {
        return glow ? glowLight : regularLight;
    }

    public Vec3d getPositionOffset(PlateRenderState plateRenderState) {
        return Vec3d.ZERO;
    }

    protected boolean hasLabel(T itemFrameEntity, double d) {
        return MinecraftClient.isHudEnabled() && this.dispatcher.targetedEntity == itemFrameEntity && itemFrameEntity.getHeldItemStack().getCustomName() != null;
    }

    protected Text getDisplayName(T itemFrameEntity) {
        return itemFrameEntity.getHeldItemStack().getName();
    }

    public PlateRenderState createRenderState() {
        return new PlateRenderState();
    }

    public void updateRenderState(T itemFrameEntity, PlateRenderState plateRenderState, float f) {
        super.updateRenderState(itemFrameEntity, plateRenderState, f);
        plateRenderState.facing = itemFrameEntity.getHorizontalFacing();
        ItemStack itemStack = itemFrameEntity.getHeldItemStack();
        this.itemModelManager.updateForNonLivingEntity(plateRenderState.itemRenderState, itemStack, ModelTransformationMode.FIXED, itemFrameEntity);
        plateRenderState.rotation = itemFrameEntity.getRotation();
        plateRenderState.glow = false;
        plateRenderState.mapId = null;
        if (!itemStack.isEmpty()) {
            MapIdComponent mapIdComponent = itemFrameEntity.getMapId(itemStack);
            if (mapIdComponent != null) {
                MapState mapState = itemFrameEntity.getWorld().getMapState(mapIdComponent);
                if (mapState != null) {
                    this.mapRenderer.update(mapIdComponent, mapState, plateRenderState.mapRenderState);
                    plateRenderState.mapId = mapIdComponent;
                }
            }
        }

    }
}
