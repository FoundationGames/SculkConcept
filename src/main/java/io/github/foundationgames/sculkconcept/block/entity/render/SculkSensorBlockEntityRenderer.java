package io.github.foundationgames.sculkconcept.block.entity.render;

import io.github.foundationgames.sculkconcept.SculkConcept;
import io.github.foundationgames.sculkconcept.block.entity.SculkSensorBlockEntity;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;

public class SculkSensorBlockEntityRenderer extends BlockEntityRenderer<SculkSensorBlockEntity> {
    private SculkSensorModel model;

    public SculkSensorBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(SculkSensorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
        matrices.translate(-0.5, -1.5, 0.5);
        SculkSensorModel model = new SculkSensorModel(entity);
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityCutout(SculkConcept.id("textures/entity/sculk_sensor/sculk_sensor_detectors.png"))), tickDelta, light, overlay, 1f, 1f, 1f, 1f);

        matrices.pop();
    }

    public static class SculkSensorModel extends Model {
        private final SculkSensorBlockEntity entity;
        private final ModelPart detector1;
        private final ModelPart detector2;
        private final ModelPart detector3;
        private final ModelPart detector4;
        private final ModelPart pdetector1;
        private final ModelPart pdetector2;
        private final ModelPart pdetector3;
        private final ModelPart pdetector4;

        public SculkSensorModel(SculkSensorBlockEntity entity) {
            super(RenderLayer::getEntityCutout);
            this.entity = entity;
            textureWidth = 16;
            textureHeight = 16;
            detector1 = new ModelPart(this);
            detector1.setPivot(5.0F, 16.0F, 5.0F);
            setRotationAngle(detector1, 0.0F, -0.7854F, 0.0F);
            detector1.setTextureOffset(0, 1).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
            detector2 = new ModelPart(this);
            detector2.setPivot(-5.0F, 16.0F, 5.0F);
            setRotationAngle(detector2, 0.0F, 0.7854F, 0.0F);
            detector2.setTextureOffset(0, 1).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
            detector3 = new ModelPart(this);
            detector3.setPivot(-5.0F, 16.0F, -5.0F);
            setRotationAngle(detector3, 0.0F, 2.3562F, 0.0F);
            detector3.setTextureOffset(0, 1).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
            detector4 = new ModelPart(this);
            detector4.setPivot(5.0F, 16.0F, -5.0F);
            setRotationAngle(detector4, 0.0F, -2.3562F, 0.0F);
            detector4.setTextureOffset(0, 1).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);

            pdetector1 = new ModelPart(this);
            pdetector1.setPivot(5.0F, 16.0F, 5.0F);
            setRotationAngle(pdetector1, 0.0F, -0.7854F, 0.0F);
            pdetector1.setTextureOffset(0, -6).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
            pdetector2 = new ModelPart(this);
            pdetector2.setPivot(-5.0F, 16.0F, 5.0F);
            setRotationAngle(pdetector2, 0.0F, 0.7854F, 0.0F);
            pdetector2.setTextureOffset(0, -6).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
            pdetector3 = new ModelPart(this);
            pdetector3.setPivot(-5.0F, 16.0F, -5.0F);
            setRotationAngle(pdetector3, 0.0F, 2.3562F, 0.0F);
            pdetector3.setTextureOffset(0, -6).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
            pdetector4 = new ModelPart(this);
            pdetector4.setPivot(5.0F, 16.0F, -5.0F);
            setRotationAngle(pdetector4, 0.0F, -2.3562F, 0.0F);
            pdetector4.setTextureOffset(0, -6).addCuboid(0.0F, -7.0F, -3.0F, 0.0F, 7.0F, 6.0F, 0.0F, false);
        }

        public void render(MatrixStack matrixStack, VertexConsumer buffer, float tickDelta, int light, int overlay, float red, float green, float blue, float alpha) {
            if(entity.isPowered()) {
                float r = entity.getAnimationProgress() > 0 ? (float) (Math.cos((entity.getAnimationProgress() + tickDelta) * 2) / 1.9f) * (1.0f - ((float)entity.getAnimationProgress() / entity.animationLength)) : 0;
                //System.out.println(light);
                light = 15728866; //15728864;
                pdetector1.roll = r;
                pdetector2.roll = r;
                pdetector3.roll = r;
                pdetector4.roll = r;
                pdetector1.pitch = -r * 0.75f;
                pdetector2.pitch = r * 0.75f;
                pdetector3.pitch = r * 0.75f;
                pdetector4.pitch = -r * 0.75f;
                pdetector1.render(matrixStack, buffer, light, overlay);
                pdetector2.render(matrixStack, buffer, light, overlay);
                pdetector3.render(matrixStack, buffer, light, overlay);
                pdetector4.render(matrixStack, buffer, light, overlay);
            } else {
                detector1.render(matrixStack, buffer, light, overlay);
                detector2.render(matrixStack, buffer, light, overlay);
                detector3.render(matrixStack, buffer, light, overlay);
                detector4.render(matrixStack, buffer, light, overlay);
            }
        }
        public void setRotationAngle(ModelPart bone, float x, float y, float z) {
            bone.pitch = x;
            bone.yaw = y;
            bone.roll = z;
        }

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        }
    }
}
