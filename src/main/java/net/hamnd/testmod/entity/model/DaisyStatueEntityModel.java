package net.hamnd.testmod.entity.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.IronGolemModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.passive.IronGolemEntity;

public class DaisyStatueEntityModel<T extends IronGolemEntity> extends IronGolemModel<T> {
    private final ModelRenderer body;
    private final ModelRenderer lowerbidon_r1;
    private final ModelRenderer middlebidon_r1;
    private final ModelRenderer lowerneck_r1;
    private final ModelRenderer lowerneck_r2;
    private final ModelRenderer upperbidon_r1;
    private final ModelRenderer Leftleg2;
    private final ModelRenderer LeftToe_r1;
    private final ModelRenderer rightToe_r1;
    private final ModelRenderer leg_r1;
    private final ModelRenderer Leftleg;
    private final ModelRenderer RightToe_r2;
    private final ModelRenderer leftToe_r2;
    private final ModelRenderer leg_r2;
    private final ModelRenderer RightArm;
    private final ModelRenderer LeftArm;
    private final ModelRenderer bb_main;
    private final ModelRenderer cube_r1;
    private final ModelRenderer cube_r2;
    private final ModelRenderer cube_r3;
    private final ModelRenderer cube_r4;
    private final ModelRenderer cube_r5;
    private final ModelRenderer cube_r6;
    private final ModelRenderer cube_r7;
    private final ModelRenderer cube_r8;
    private final ModelRenderer cube_r9;
    private final ModelRenderer cube_r10;
    private final ModelRenderer cube_r11;
    private final ModelRenderer cube_r12;
    private final ModelRenderer cube_r13;
    private final ModelRenderer cube_r14;
    private final ModelRenderer cube_r15;
    private final ModelRenderer cube_r16;
    private final ModelRenderer cube_r17;
    private final ModelRenderer cube_r18;
    private final ModelRenderer cube_r19;
    private final ModelRenderer cube_r20;
    private final ModelRenderer cube_r21;
    private final ModelRenderer cube_r22;

    public DaisyStatueEntityModel() {
        textureWidth = 256;
        textureHeight = 256;

        body = new ModelRenderer(this);
        body.setRotationPoint(0.0F, 10.0F, 0.0F);


        lowerbidon_r1 = new ModelRenderer(this);
        lowerbidon_r1.setRotationPoint(-2.0F, 4.0F, -1.0F);
        body.addChild(lowerbidon_r1);
        setRotationAngle(lowerbidon_r1, -0.0873F, 0.0F, 0.0F);
        lowerbidon_r1.setTextureOffset(0, 32).addBox(-11.0F, -13.0F, -9.5F, 26.0F, 9.0F, 19.0F, 0.0F, false);

        middlebidon_r1 = new ModelRenderer(this);
        middlebidon_r1.setRotationPoint(12.0F, -13.0F, 0.0F);
        body.addChild(middlebidon_r1);
        setRotationAngle(middlebidon_r1, -0.1309F, 0.0F, 0.0F);
        middlebidon_r1.setTextureOffset(0, 60).addBox(-24.0F, -10.0F, -7.0F, 24.0F, 18.0F, 15.0F, 0.0F, false);

        lowerneck_r1 = new ModelRenderer(this);
        lowerneck_r1.setRotationPoint(9.0F, -26.0F, -3.0F);
        body.addChild(lowerneck_r1);
        setRotationAngle(lowerneck_r1, -1.1345F, 0.0F, 0.0F);
        lowerneck_r1.setTextureOffset(0, 150).addBox(-12.0F, -3.0F, -8.5F, 6.0F, 6.0F, 8.0F, 0.0F, false);

        lowerneck_r2 = new ModelRenderer(this);
        lowerneck_r2.setRotationPoint(9.0F, -25.0F, 2.0F);
        body.addChild(lowerneck_r2);
        setRotationAngle(lowerneck_r2, -0.1745F, 0.0F, 0.0F);
        lowerneck_r2.setTextureOffset(142, 57).addBox(-13.0F, -6.0F, -6.5F, 8.0F, 9.0F, 16.0F, 0.0F, false);
        lowerneck_r2.setTextureOffset(58, 116).addBox(-5.0F, -6.0F, -9.5F, 10.0F, 9.0F, 19.0F, 0.0F, false);
        lowerneck_r2.setTextureOffset(0, 93).addBox(-23.0F, -6.0F, -9.5F, 10.0F, 9.0F, 19.0F, 0.0F, false);

        upperbidon_r1 = new ModelRenderer(this);
        upperbidon_r1.setRotationPoint(1.0F, -13.0F, 0.0F);
        body.addChild(upperbidon_r1);
        setRotationAngle(upperbidon_r1, -0.0873F, 0.0F, 0.0F);
        upperbidon_r1.setTextureOffset(0, 0).addBox(-14.5F, -10.0F, -9.5F, 27.0F, 13.0F, 19.0F, 0.0F, false);

        Leftleg2 = new ModelRenderer(this);
        Leftleg2.setRotationPoint(0.0F, 10.0F, 0.0F);
        Leftleg2.setTextureOffset(78, 88).addBox(6.0F, 4.0F, -10.0F, 13.0F, 9.0F, 19.0F, 0.0F, false);

        LeftToe_r1 = new ModelRenderer(this);
        LeftToe_r1.setRotationPoint(11.0F, 13.0F, -7.0F);
        Leftleg2.addChild(LeftToe_r1);
        setRotationAngle(LeftToe_r1, 0.0F, -0.0873F, 0.0F);
        LeftToe_r1.setTextureOffset(58, 102).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

        rightToe_r1 = new ModelRenderer(this);
        rightToe_r1.setRotationPoint(15.0F, 13.0F, -5.0F);
        Leftleg2.addChild(rightToe_r1);
        setRotationAngle(rightToe_r1, 0.0F, -0.0436F, 0.0F);
        rightToe_r1.setTextureOffset(28, 150).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 3.0F, 7.0F, 0.0F, false);

        leg_r1 = new ModelRenderer(this);
        leg_r1.setRotationPoint(14.0F, -2.0F, 1.0F);
        Leftleg2.addChild(leg_r1);
        setRotationAngle(leg_r1, 0.0F, 0.0F, -0.2618F);
        leg_r1.setTextureOffset(0, 121).addBox(-10.0F, -4.0F, -9.0F, 12.0F, 13.0F, 16.0F, 0.0F, false);

        Leftleg = new ModelRenderer(this);
        Leftleg.setRotationPoint(0.0F, 10.0F, 0.0F);
        Leftleg.setTextureOffset(78, 60).addBox(-19.0F, 4.0F, -10.0F, 13.0F, 9.0F, 19.0F, 0.0F, false);

        RightToe_r2 = new ModelRenderer(this);
        RightToe_r2.setRotationPoint(-11.0F, 13.0F, -7.0F);
        Leftleg.addChild(RightToe_r2);
        setRotationAngle(RightToe_r2, 0.0F, 0.0873F, 0.0F);
        RightToe_r2.setTextureOffset(58, 93).addBox(-2.0F, -3.0F, -5.0F, 4.0F, 3.0F, 6.0F, 0.0F, false);

        leftToe_r2 = new ModelRenderer(this);
        leftToe_r2.setRotationPoint(-15.0F, 13.0F, -5.0F);
        Leftleg.addChild(leftToe_r2);
        setRotationAngle(leftToe_r2, 0.0F, 0.0436F, 0.0F);
        leftToe_r2.setTextureOffset(142, 105).addBox(-2.0F, -3.0F, -6.0F, 4.0F, 3.0F, 7.0F, 0.0F, false);

        leg_r2 = new ModelRenderer(this);
        leg_r2.setRotationPoint(-14.0F, -2.0F, 1.0F);
        Leftleg.addChild(leg_r2);
        setRotationAngle(leg_r2, 0.0F, 0.0F, 0.2618F);
        leg_r2.setTextureOffset(116, 116).addBox(-2.0F, -4.0F, -9.0F, 12.0F, 13.0F, 16.0F, 0.0F, false);

        RightArm = new ModelRenderer(this);
        RightArm.setRotationPoint(0.0F, 24.0F, 0.0F);


        LeftArm = new ModelRenderer(this);
        LeftArm.setRotationPoint(19.0F, -13.0F, -2.0F);


        bb_main = new ModelRenderer(this);
        bb_main.setRotationPoint(0.0F, 24.0F, 0.0F);


        cube_r1 = new ModelRenderer(this);
        cube_r1.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r1);
        setRotationAngle(cube_r1, -0.2215F, 0.0624F, 0.1724F);
        cube_r1.setTextureOffset(120, 57).addBox(-3.6F, -0.5F, -0.7F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r2 = new ModelRenderer(this);
        cube_r2.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r2);
        setRotationAngle(cube_r2, -0.5989F, -0.8493F, 0.5213F);
        cube_r2.setTextureOffset(68, 111).addBox(-3.4F, -0.5F, -0.7F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r3 = new ModelRenderer(this);
        cube_r3.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r3);
        setRotationAngle(cube_r3, -2.0334F, -1.1821F, 2.0468F);
        cube_r3.setTextureOffset(58, 111).addBox(-3.3F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r4 = new ModelRenderer(this);
        cube_r4.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r4);
        setRotationAngle(cube_r4, -2.9843F, -0.081F, 2.7549F);
        cube_r4.setTextureOffset(110, 57).addBox(-3.3F, -0.5F, -1.0F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r5 = new ModelRenderer(this);
        cube_r5.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r5);
        setRotationAngle(cube_r5, 3.0625F, 1.0397F, 2.8354F);
        cube_r5.setTextureOffset(100, 57).addBox(-3.3F, -0.5F, -0.9F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r6 = new ModelRenderer(this);
        cube_r6.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r6);
        setRotationAngle(cube_r6, 0.0677F, 1.2021F, 0.2133F);
        cube_r6.setTextureOffset(90, 57).addBox(-3.3F, -0.5F, -0.9F, 3.0F, 1.0F, 2.0F, 0.0F, false);

        cube_r7 = new ModelRenderer(this);
        cube_r7.setRotationPoint(2.4F, -50.1771F, 10.2256F);
        bb_main.addChild(cube_r7);
        setRotationAngle(cube_r7, -0.3518F, -0.123F, 0.045F);
        cube_r7.setTextureOffset(58, 114).addBox(-0.5F, -0.8F, -0.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);

        cube_r8 = new ModelRenderer(this);
        cube_r8.setRotationPoint(2.4F, -45.5088F, 8.5445F);
        bb_main.addChild(cube_r8);
        setRotationAngle(cube_r8, -0.3491F, 0.0F, 0.0F);
        cube_r8.setTextureOffset(50, 150).addBox(-0.5F, -5.0F, -0.5F, 1.0F, 7.0F, 1.0F, 0.0F, false);

        cube_r9 = new ModelRenderer(this);
        cube_r9.setRotationPoint(-31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r9);
        setRotationAngle(cube_r9, 0.0311F, -0.4014F, -0.9118F);
        cube_r9.setTextureOffset(152, 43).addBox(-2.329F, 0.2735F, 3.0457F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r9.setTextureOffset(152, 37).addBox(-2.329F, -4.5265F, 2.4457F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r9.setTextureOffset(152, 31).addBox(-2.329F, -5.9265F, -2.1543F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r9.setTextureOffset(106, 25).addBox(-3.1639F, 0.7217F, -4.6448F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r9.setTextureOffset(92, 0).addBox(-0.9894F, -6.4261F, -6.3455F, 18.0F, 12.0F, 13.0F, 0.0F, false);

        cube_r10 = new ModelRenderer(this);
        cube_r10.setRotationPoint(31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r10);
        setRotationAngle(cube_r10, 0.0055F, 0.507F, 0.8557F);
        cube_r10.setTextureOffset(120, 159).addBox(1.5387F, 0.3283F, 3.2654F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r11 = new ModelRenderer(this);
        cube_r11.setRotationPoint(31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r11);
        setRotationAngle(cube_r11, 0.1241F, 0.4923F, 1.1137F);
        cube_r11.setTextureOffset(110, 159).addBox(0.9277F, -4.8993F, 2.66F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r12 = new ModelRenderer(this);
        cube_r12.setRotationPoint(31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r12);
        setRotationAngle(cube_r12, 0.1216F, 0.3846F, 1.1473F);
        cube_r12.setTextureOffset(100, 159).addBox(0.992F, -6.2421F, -2.1543F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r13 = new ModelRenderer(this);
        cube_r13.setRotationPoint(31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r13);
        setRotationAngle(cube_r13, -0.001F, 0.2288F, 0.7719F);
        cube_r13.setTextureOffset(154, 18).addBox(1.8577F, 0.9381F, -5.0464F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r14 = new ModelRenderer(this);
        cube_r14.setRotationPoint(-31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r14);
        setRotationAngle(cube_r14, 0.1216F, -0.3846F, -1.1473F);
        cube_r14.setTextureOffset(154, 12).addBox(-2.992F, -6.2421F, -2.1543F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r15 = new ModelRenderer(this);
        cube_r15.setRotationPoint(31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r15);
        setRotationAngle(cube_r15, 0.0311F, 0.4014F, 0.9118F);
        cube_r15.setTextureOffset(92, 25).addBox(-0.8361F, 0.7217F, -4.6448F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r15.setTextureOffset(148, 25).addBox(-1.671F, -5.9265F, -2.1543F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r15.setTextureOffset(134, 25).addBox(-1.671F, -4.5265F, 2.4457F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r15.setTextureOffset(120, 25).addBox(-1.671F, 0.2735F, 3.0457F, 4.0F, 3.0F, 3.0F, 0.0F, false);
        cube_r15.setTextureOffset(90, 32).addBox(-17.0106F, -6.4261F, -6.3455F, 18.0F, 12.0F, 13.0F, 0.0F, false);

        cube_r16 = new ModelRenderer(this);
        cube_r16.setRotationPoint(-31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r16);
        setRotationAngle(cube_r16, 0.1241F, -0.4923F, -1.1137F);
        cube_r16.setTextureOffset(154, 6).addBox(-2.9277F, -4.8993F, 2.66F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r17 = new ModelRenderer(this);
        cube_r17.setRotationPoint(-31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r17);
        setRotationAngle(cube_r17, 0.0055F, -0.507F, -0.8557F);
        cube_r17.setTextureOffset(154, 0).addBox(-3.5387F, 0.3283F, 3.2654F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r18 = new ModelRenderer(this);
        cube_r18.setRotationPoint(-31.643F, -19.0259F, -7.1448F);
        bb_main.addChild(cube_r18);
        setRotationAngle(cube_r18, -0.001F, -0.2288F, -0.7719F);
        cube_r18.setTextureOffset(152, 49).addBox(-3.8577F, 0.9381F, -5.0464F, 2.0F, 3.0F, 3.0F, 0.0F, false);

        cube_r19 = new ModelRenderer(this);
        cube_r19.setRotationPoint(-14.0F, -41.0F, -1.0F);
        bb_main.addChild(cube_r19);
        setRotationAngle(cube_r19, 0.0349F, -0.1222F, -0.6109F);
        cube_r19.setTextureOffset(56, 144).addBox(-7.0F, -3.0F, -3.0F, 10.0F, 11.0F, 12.0F, 0.0F, false);

        cube_r20 = new ModelRenderer(this);
        cube_r20.setRotationPoint(19.0F, -37.0F, -2.0F);
        bb_main.addChild(cube_r20);
        setRotationAngle(cube_r20, 0.0297F, 0.2705F, 0.5585F);
        cube_r20.setTextureOffset(134, 145).addBox(-2.0F, -1.0F, -1.0F, 10.0F, 7.0F, 7.0F, 0.0F, false);

        cube_r21 = new ModelRenderer(this);
        cube_r21.setRotationPoint(-19.0F, -37.0F, -2.0F);
        bb_main.addChild(cube_r21);
        setRotationAngle(cube_r21, 0.0297F, -0.2705F, -0.5585F);
        cube_r21.setTextureOffset(100, 145).addBox(-8.0F, -1.0F, -1.0F, 10.0F, 7.0F, 7.0F, 0.0F, false);

        cube_r22 = new ModelRenderer(this);
        cube_r22.setRotationPoint(14.0F, -41.0F, -1.0F);
        bb_main.addChild(cube_r22);
        setRotationAngle(cube_r22, 0.0349F, 0.1222F, 0.6109F);
        cube_r22.setTextureOffset(142, 82).addBox(-3.0F, -3.0F, -3.0F, 10.0F, 11.0F, 12.0F, 0.0F, false);
    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Leftleg2.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        Leftleg.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        RightArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        LeftArm.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        bb_main.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.body, this.Leftleg2, this.Leftleg, this.RightArm, this.LeftArm, this.bb_main);
    }

    @Override
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }
}