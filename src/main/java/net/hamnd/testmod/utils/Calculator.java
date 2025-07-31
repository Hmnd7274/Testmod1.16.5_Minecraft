package net.hamnd.testmod.utils;

import net.hamnd.testmod.entity.model.CustomSheepModel;
import net.hamnd.testmod.event.ModEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class Calculator {
    //UTILS
    // /execute as @e run data modify entity @s NoAI set value 1b
    // On part du sud en tournant vers la droite pour l'axe Y(haut) de rotation

    public static Vector3d rotateAroundX(Vector3d v, double theta){
        double radians = Math.toRadians(theta);

        double newX = v.x;
        double newY = v.y * Math.cos(radians) - v.z * Math.sin(radians);
        double newZ = v.y * Math.sin(radians) + v.z * Math.cos(radians);

        return new Vector3d(newX, newY, newZ);
    }
    public static Vector3d rotateAroundY(Vector3d v, double theta) {
        double radians = Math.toRadians(theta);

        double newX = v.x * Math.cos(radians) + v.z * Math.sin(radians);
        double newY = v.y;
        double newZ = -v.x * Math.sin(radians) + v.z * Math.cos(radians);

        return new Vector3d(newX, newY, newZ);
    }
    public static Vector3d rotateAroundZ(Vector3d v, double theta) {
        double radians = Math.toRadians(theta);

        double newX = v.x * Math.cos(radians) - v.y * Math.sin(radians);
        double newY = v.x * Math.sin(radians) + v.y * Math.cos(radians);
        double newZ = v.z;

        return new Vector3d(newX, newY, newZ);
    }

    public static Vector3d additionner(Vector3d vec, Vector3d autreVecteur) {
        double xResultat = vec.x + autreVecteur.x;
        double yResultat = vec.y + autreVecteur.y;
        double zResultat = vec.z + autreVecteur.z;

        return new Vector3d(xResultat, yResultat, zResultat);
    }

    public static Vector3d multiply(Vector3d vec, float mul){
        return new Vector3d(vec.x * mul, vec.y * mul, vec.z * mul);
    }

    public static Vector3d smoothLerpManual(float partialTicks, Vector3d prev, Vector3d current) {
        double x = MathHelper.lerp(partialTicks, prev.x, current.x);
        double y = MathHelper.lerp(partialTicks, prev.y, current.y);
        double z = MathHelper.lerp(partialTicks, prev.z, current.z);
        return new Vector3d(x, y, z);
    }

    public static Vector3d getPosVec(LivingEntity entity){
        return new Vector3d(entity.getPosX(), entity.getPosY(), entity.getPosZ());
    }
    public static Vector3d getPrevPosVec(LivingEntity entity){
        return new Vector3d(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
    }

    public static Vector3d getSmoothPosVec(LivingEntity entity){
        return smoothLerpManual(Minecraft.getInstance().getRenderPartialTicks(), getPrevPosVec(entity), getPosVec(entity));
    }

    public static Vector3d getLookVec(LivingEntity entity){
        float pitch = entity.rotationPitch;
        if(entity instanceof SheepEntity){
            pitch = ((SheepEntity) entity).getHeadRotationAngleX(Minecraft.getInstance().getRenderPartialTicks());
        }
        return rotateAroundY(Calculator.rotateAroundX(new Vector3d(0, 0, 0.325F), pitch), -entity.rotationYawHead);
    }
    public static Vector3d getPrevLookVec(LivingEntity entity){
        float pitch = entity.prevRotationPitch;
        if(entity instanceof SheepEntity){
            pitch = ((SheepEntity) entity).getHeadRotationAngleX(Minecraft.getInstance().getRenderPartialTicks());
        }
        return rotateAroundY(Calculator.rotateAroundX(new Vector3d(0, 0, 0.325F), pitch), -entity.prevRotationYawHead);
    }

    public static Vector3d vargetLookVec(LivingEntity entity, float x){
        float eyeSpacing = 0.125F;
        float eyeDistance = 0.325F;
        float pitch = entity.rotationPitch;
        float yaw = entity.rotationYawHead;
        if(getSheepModel(entity) instanceof CustomSheepModel){
            CustomSheepModel<?> model = (CustomSheepModel<?>) getSheepModel(entity);
            if(model != null){
                pitch = model.getHeadRotXPitch() * 180;
                yaw = model.getHeadRotYYaw() * 180;
                debug("Sheep");
            }
        }
//        if(entity instanceof SheepEntity){
//            pitch = ((SheepEntity) entity).getHeadRotationAngleX(Minecraft.getInstance().getRenderPartialTicks());
//            debug("EHEH");
//            yaw = ((SheepEntity) entity).getHeadRotationPointY(Minecraft.getInstance().getRenderPartialTicks());
//        }
        if(entity instanceof PigEntity){
            eyeSpacing = 0.14F;
            eyeDistance = 0.350F;
        }
        return rotateAroundY(Calculator.rotateAroundX(new Vector3d(eyeSpacing * x, 0, eyeDistance), pitch), -yaw);
    }
    public static Vector3d vargetPrevLookVec(LivingEntity entity, float x){
        float eyeSpacing = 0.125F;
        float eyeDistance = 0.325F;
        float pitch = entity.prevRotationPitch;
        float yaw = entity.prevRotationYawHead;
        if(getSheepModel(entity) instanceof CustomSheepModel){
            CustomSheepModel<?> model = (CustomSheepModel<?>) getSheepModel(entity);
            if(model != null){
                pitch = model.getHeadRotXPitch() * 180;
                yaw = model.getHeadRotYYaw() * 180;
//                debug("WORK");
            }
        }


//        if(entity instanceof SheepEntity){
//
//            EntityRenderer<? extends Entity> renderer = Minecraft.getInstance().getRenderManager().getRenderer(entity);
//            if (renderer instanceof LivingRenderer) {
//                QuadrupedModel<?> entityModel = (QuadrupedModel<?>) ((LivingRenderer<?, ?>) renderer).getEntityModel();
//            return entityModel.head.rotateAngleX; // Gets the pitch of the sheep's head
//            float headPitchDegrees = entityModel.bipedHead.rotateAngleX * (180F / (float) Math.PI);
//            }
//        }
        if(entity instanceof PigEntity){
            eyeSpacing = 0.14F;
            eyeDistance = 0.350F;
        }
        return rotateAroundY(Calculator.rotateAroundX(new Vector3d(eyeSpacing * x, 0, eyeDistance), pitch), -yaw);
    }

    public static Vector3d getSmoothLookVec(LivingEntity entity){
        return smoothLerpManual(Minecraft.getInstance().getRenderPartialTicks(), getPrevLookVec(entity), getLookVec(entity));
    }


    public static Vector3d getPosHeadVec(LivingEntity entity){
        float heightMultiplier = 0.85F;
        if(entity.isChild()){
            heightMultiplier = 0.85F;
        }
//        else if(entity instanceof SheepEntity){
//            toReturn = additionner(getPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * heightMultiplier, 0.6F), -entity.renderYawOffset));
//        }
//        else if(entity instanceof PigEntity){
//            toReturn = additionner(getPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.renderYawOffset));
//        }
//        else if(entity instanceof CowEntity){
//            toReturn = additionner(getPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.renderYawOffset));
//        }
//        else{
//            toReturn = additionner(getPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.renderYawOffset));
//        }
        return additionner(getPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * heightMultiplier, 0.6F), -entity.renderYawOffset));
    }
    public static Vector3d getPrevPosHeadVec(LivingEntity entity){
        Vector3d toReturn = null;
        if(entity instanceof SheepEntity){
            toReturn = additionner(getPrevPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.prevRenderYawOffset));
        }
        else if(entity instanceof PigEntity){
            toReturn = additionner(getPrevPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.prevRenderYawOffset));
        }
        else if(entity instanceof CowEntity){
            toReturn = additionner(getPrevPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.prevRenderYawOffset));
        }
        else{
            toReturn = additionner(getPrevPosVec(entity), Calculator.rotateAroundY(new Vector3d(0, entity.getHeight() * 0.85F, 0.6F), -entity.prevRenderYawOffset));
        }
        return toReturn;
    }


    public static Vector3d vargetPosHeadVec(LivingEntity entity, float x){
        return additionner(getPosVec(entity), Calculator.rotateAroundY(new Vector3d(x, entity.getHeight() * 0.95F, 0.6F), -entity.renderYawOffset));
    }
    public static Vector3d vargetPrevPosHeadVec(LivingEntity entity, float x){
        return additionner(getPrevPosVec(entity), Calculator.rotateAroundY(new Vector3d(x, entity.getHeight() * 0.95F, 0.6F), -entity.prevRenderYawOffset));
    }

    public static Vector3d getSmoothPosHeadVec(LivingEntity entity){
        return smoothLerpManual(Minecraft.getInstance().getRenderPartialTicks(), getPrevPosHeadVec(entity), getPosHeadVec(entity));
    }

    public static Vector3d getPosHeadLookVec(LivingEntity entity){
        return additionner(getPosHeadVec(entity), getLookVec(entity));
    }
    public static Vector3d getPrevPosHeadLookVec(LivingEntity entity){
        return additionner(getPrevPosHeadVec(entity), getPrevLookVec(entity));
    }

    public static Vector3d vargetPosHeadLookVec(LivingEntity entity, float x){
        return additionner(getPosHeadVec(entity), vargetLookVec(entity, x));
    }
    public static Vector3d vargetPrevPosHeadLookVec(LivingEntity entity, float x){
        return additionner(getPrevPosHeadVec(entity), vargetPrevLookVec(entity, x));
    }

    public static Vector3d getSmoothAllEye(LivingEntity entity, float x){
        return smoothLerpManual(Minecraft.getInstance().getRenderPartialTicks(), vargetPrevPosHeadLookVec(entity, x), vargetPosHeadLookVec(entity, x));
    }


    public static EntityModel<?> getSheepModel(LivingEntity entity){
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getRenderManager().getRenderer(entity);
        // Check if it's our custom renderer
        if (renderer != null) {
            return ((LivingRenderer<?, ?>) renderer).getEntityModel();
        }
        sendMsg("ModelBuged");
        return null; // Not a custom model
    }


    //DEBUGGING FUNCTIONS
    public static String vecToString(Vector3d vec) {
        return ("\n\n\n" + vec.x + "\n" + vec.y + "\n" + vec.z);
    }

    public static void debug(String str){
        System.out.println(str);
    }

    public static void sendMsg(String str){
        ITextComponent msg = new StringTextComponent(str);
        ModEvents.player.sendMessage(msg, ModEvents.player.getUniqueID());
    }
}
