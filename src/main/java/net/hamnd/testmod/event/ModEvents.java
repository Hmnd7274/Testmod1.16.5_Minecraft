package net.hamnd.testmod.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.patchy.BlockedServers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.entity.ModEntityTypes;
import net.hamnd.testmod.entity.custom.CameraEntity;
import net.hamnd.testmod.entity.villager.ModVillagers;
import net.hamnd.testmod.item.ModItems;
import net.hamnd.testmod.utils.Calculator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Method;
import java.util.List;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID)
public class ModEvents {
    public static PlayerEntity player;
    public static World world;
    public static ServerWorld serverWorld;
    public static boolean isInCutscene;
    public static boolean overlay;
    public static CameraEntity cam = null;
    public static CameraEntity creeper;
    public static CreeperEntity myCutsceneCreeper = null;

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.TOOLSMITH) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            ItemStack stack = new ItemStack(ModItems.EIGHT_BALL.get(), 1);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    stack, 10, 8, 0.02F));
        }

        if(event.getType() == ModVillagers.JUMP_MASTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            ItemStack stack = new ItemStack(ModItems.MEJOBERRY.get(), 15);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    stack, 10, 8, 0.02F));

            ItemStack stack2 = new ItemStack(ModItems.MEJOBERRY_SEEDS.get(), 6);
            int villagerLevel2 = 1;

            trades.get(villagerLevel2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    stack2, 10, 8, 0.02F));
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent.PlayerTickEvent event){
        if(world == null){
            world = Minecraft.getInstance().world;
            System.out.println("WORLD iniated");
            System.out.println(world);
        }
        if(serverWorld == null && !event.player.world.isRemote()){
            serverWorld = (ServerWorld) event.player.world;
            System.out.println("SERVERWORLD iniated");
            System.out.println(serverWorld);
        }
        if(player == null){
            player = Minecraft.getInstance().player;
            System.out.println("PLAYER iniated");
            System.out.println(player);
        }
    }
//    
//    @SubscribeEvent
//    public static void onKeyPressed(InputEvent.KeyInputEvent event){
////        Calculator.sendMsg(String.valueOf(event.getKey()));
//        // J key
//        if(event.getKey() == 74 && event.getAction() == GLFW.GLFW_PRESS) {
//            isInCutscene = !isInCutscene;
//            Minecraft mc = Minecraft.getInstance();
//            if(!isInCutscene) mc.setRenderViewEntity(player);
//            if(!isInCutscene) return;
//            World world = mc.world;
//
//            // Create our camera entity
//            cam = new CameraEntity(ModEntityTypes.CAMERA_ENTITY.get(), world); // type is irrelevant here
//            assert world != null;
//            creeper = new CameraEntity(ModEntityTypes.CAMERA_ENTITY.get(), serverWorld);
//            creeper.setPosition(player.getPosX(), player.getPosY() + 5, player.getPosZ());
//            serverWorld.addEntity(creeper);
//        }
//        if(event.getKey() == 75 && event.getAction() == GLFW.GLFW_PRESS) {
//            overlay = !overlay;
//            // Tell MC to render from it
//            Minecraft.getInstance().setRenderViewEntity(creeper);
//        }
//        if(event.getKey() == 76 && event.getAction() == GLFW.GLFW_PRESS) {
////            myCutsceneCreeper = new CreeperEntity(EntityType.CREEPER, world);
////            myCutsceneCreeper.setPosition(player.getPosX(), player.getPosY(), player.getPosZ());
//            Minecraft.getInstance().setRenderViewEntity(player);
//        }
//        
//        if(event.getKey() == 77 && event.getAction() == GLFW.GLFW_PRESS) {
//            Calculator.sendMsg("77");
//            creeper.rotationPitch = 90F;
//            creeper.rotateTowards(0, 90);
//            
//        }
//        if(event.getKey() == 78 && event.getAction() == GLFW.GLFW_PRESS) {
//            Calculator.sendMsg("78");
//            creeper.rotationPitch = 180F;
//            creeper.rotateTowards(0, 180);
//            
//        }
//        if(event.getKey() == 79 && event.getAction() == GLFW.GLFW_PRESS) {
//            Calculator.sendMsg("79");
//            creeper.rotationPitch = 270F;
//            creeper.rotateTowards(0, 270);
//        }
//    }

    @SubscribeEvent
    public static void onFireBlockDestroy(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.FIRE
                && event.getWorld() instanceof ServerWorld) {
            if (event.getPlayer().getHeldItemMainhand().getItem() != Items.DIAMOND_HOE) return;
            // Create an ItemStack (here vanilla Fire Block Item)
            ItemStack stack = new ItemStack(ModItems.FIRE_ITEM.get(), 1);

            // Create the item entity at some coordinates
            ItemEntity entity = new ItemEntity(event.getWorld(),
                    event.getPos().getX(),
                    event.getPos().getY(),
                    event.getPos().getZ(),
                    stack);
            event.getWorld().addEntity(entity);
        }
    }
    
    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event){
        if(cam == null) return;
        cam.setPosition(player.getPosX(), player.getPosY() + 5, player.getPosZ());
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (myCutsceneCreeper != null) {
            Minecraft mc = Minecraft.getInstance();
            IRenderTypeBuffer.Impl buffer = mc.getRenderTypeBuffers().getBufferSource();
            ActiveRenderInfo info = mc.gameRenderer.getActiveRenderInfo();
            MatrixStack matrixStack = event.getMatrixStack();
            assert mc.world != null;
            int packedLight = WorldRenderer.getCombinedLight(mc.world, new BlockPos(myCutsceneCreeper.getPositionVec()));

            double x = myCutsceneCreeper.getPosX();
            double y = myCutsceneCreeper.getPosY();
            double z = myCutsceneCreeper.getPosZ();

            mc.getRenderManager().renderEntityStatic(myCutsceneCreeper, x, y, z, 0.0f, event.getPartialTicks(), matrixStack, buffer, packedLight);
            buffer.finish();
        }
    }

    @Mod.EventBusSubscriber(modid = TestMod.MOD_ID, value = Dist.CLIENT)
    public static class ClientEvents {

//        @SubscribeEvent
//        public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
//            if (!isInCutscene) return;
//
//            Minecraft mc = Minecraft.getInstance();
//            ActiveRenderInfo info = event.getInfo();
//
//            // Nouvelle position caméra (ex: décalée de 2 blocs sur X et +3 sur Y)
//            Vector3d camPos = new Vector3d(
//                    player.getPosX() + 2,
//                    player.getPosY() + 3,
//                    player.getPosZ()
//            );
//            if(m == null){
//                try {
//                    // Récupération de la méthode privée setPosition(Vec3d)
//                    Calculator.sendMsg("applied");
//                    m = ActiveRenderInfo.class.getDeclaredMethod("setPosition", Vector3d.class);
//                    m.setAccessible(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//                // Application de la position custom
//                m.invoke(info, camPos);
//        }

        @SubscribeEvent
        public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
            if (overlay) {
                event.setCanceled(true); // coupe le HUD, y compris main/objets
            }
        }

        @SubscribeEvent
        public static void onRenderHand(RenderHandEvent event){
            if (overlay) {
                event.setCanceled(true); // coupe le HUD, y compris main/objets
            }
        }
    }
}












/* package net.hamnd.testmod.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.entity.model.CustomSheepModel;
import net.hamnd.testmod.entity.villager.ModVillagers;
import net.hamnd.testmod.item.ModItems;
import net.hamnd.testmod.utils.EntityCryHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static net.hamnd.testmod.utils.Calculator.*;
import static net.hamnd.testmod.utils.DebugRenderer.*;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID)
public class ModEvents {
    public static PlayerEntity player;
    public static World world;
    public static ServerWorld serverWorld;

    public static Set<SheepEntity> cryingEntities = new HashSet<>();
    public static Iterator<SheepEntity> iterator;
    public static SheepEntity sheep;
    public static float stored;

    public static float debugBodyYaw = 0;
    public static float debugHeadYaw = 0;
    public static float debugHeadPitch = 0;
    public static Vector3d rayTest;

    public static boolean rayBool;

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.TOOLSMITH) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            ItemStack stack = new ItemStack(ModItems.EIGHT_BALL.get(), 1);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    stack, 10, 8, 0.02F));
        }

        if(event.getType() == ModVillagers.JUMP_MASTER.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            ItemStack stack = new ItemStack(ModItems.MEJOBERRY.get(), 15);
            int villagerLevel = 1;

            trades.get(villagerLevel).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    stack, 10, 8, 0.02F));

            ItemStack stack2 = new ItemStack(ModItems.MEJOBERRY_SEEDS.get(), 6);
            int villagerLevel2 = 1;

            trades.get(villagerLevel2).add((trader, rand) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 8),
                    stack2, 10, 8, 0.02F));
        }

    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event){
        if(world == null){
            world = Minecraft.getInstance().world;
            System.out.println("WORLD iniated");
            System.out.println(world);
        }
        if(serverWorld == null && !event.world.isRemote()){
            serverWorld = (ServerWorld) event.world;
            System.out.println("SERVERWORLD iniated");
            System.out.println(serverWorld);
        }
        if(player == null){
            player = Minecraft.getInstance().player;
            System.out.println("PLAYER iniated");
            System.out.println(player);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(event.getSource().getTrueSource() instanceof PlayerEntity){
            if(event.getEntityLiving() instanceof SheepEntity){
                cryingEntities.add((SheepEntity) event.getEntityLiving());
                iterator = cryingEntities.iterator();
                sheep = (SheepEntity) event.getEntityLiving();
                sendMsg("added");
            }
        }
        if(event.getEntityLiving() instanceof SheepEntity){
            if(getSheepModel(event.getEntityLiving()) instanceof CustomSheepModel){
                sendMsg("YESSS");
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        if(cryingEntities == null)return;
        cryingEntities.remove(event.getEntityLiving());
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(world == null || sheep == null)return;
        CustomSheepModel<?> model = (CustomSheepModel<?>) getSheepModel(sheep);
        assert model != null;
        if(sheep.renderYawOffset != stored){
            sendMsg(Float.toString(sheep.renderYawOffset));
            stored = sheep.renderYawOffset;
        }
        if(cryingEntities.isEmpty() || world == null)return;
        if(world.getGameTime() % 3 == 0){
            for (Iterator<SheepEntity> i = cryingEntities.iterator(); i.hasNext();){
                SheepEntity sheep = i.next();
                Vector3d sheepLeftEyeVec = getSmoothAllEye(sheep, -0.125F);
                Vector3d sheepRightEyeVec = getSmoothAllEye(sheep, 0.125F);
                world.addParticle(ParticleTypes.FALLING_WATER,
                        sheepLeftEyeVec.x,
                        sheepLeftEyeVec.y,
                        sheepLeftEyeVec.z, 0, 0, 0);
                world.addParticle(ParticleTypes.FALLING_WATER,
                        sheepRightEyeVec.x,
                        sheepRightEyeVec.y,
                        sheepRightEyeVec.z, 0, 0, 0);
            }
            EntityCryHandler.makeEntityCry(cryingEntities);
        }
    }

    @SubscribeEvent
    public static void onJPressed(InputEvent.KeyInputEvent event){
        // J key
        if(event.getKey() == 74 && event.getAction() == GLFW.GLFW_PRESS){
            sendMsg(Integer.toString(cryingEntities.size()));
            rayBool = !rayBool;
        }
        // K key
//        if(event.getKey() == 75 && event.getAction() == GLFW.GLFW_PRESS){
//            sendMsg(serverWorld.toString());
//        }
        if(sheep == null)return;
        CustomSheepModel<?> model = (CustomSheepModel<?>) getSheepModel(sheep);
        if(model == null)return;
        if(event.getKey() == 75 && event.getAction() == GLFW.GLFW_PRESS){

            rayTest = multiply(model.gettkt(), 0.1F);
            sendMsg(Float.toString(sheep.renderYawOffset));

            rayTest = additionner(getPosVec(sheep), rotateAroundY(multiply(model.gettkt(), 0.1F), sheep.renderYawOffset));
        }

        if(event.getKey() == 76 && event.getAction() == GLFW.GLFW_PRESS){
            model.setHeadYaw(90);
            sendMsg("headYaw : 90");
        }
//B
        if(event.getKey() == 66 && event.getAction() == GLFW.GLFW_PRESS){
            rayTest = null;
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event){
        MatrixStack matrixStack = event.getMatrixStack();
        if(cryingEntities == null || !rayBool)return;
        for(Iterator<SheepEntity> i = cryingEntities.iterator(); i.hasNext();){
            SheepEntity sheep = i.next();
            Vector3d sheepPosVec = getSmoothPosVec(sheep);
            Vector3d sheepPosHeadVec = getSmoothPosHeadVec(sheep);
            Vector3d sheepLeftEyeVec = getSmoothAllEye(sheep, -1);
            Vector3d sheepRightEyeVec = getSmoothAllEye(sheep, 1);
            drawLine(matrixStack, new Vector3d(0,0,0), sheepPosVec, 1, 0, 0, 1);

            if(rayTest != null){
                drawLine(matrixStack, sheepPosVec, rayTest, 1,1,0,1);
            }
            else {
                drawLine(matrixStack, sheepPosVec, sheepPosHeadVec, 0, 1, 0, 1);
                drawLine(matrixStack, sheepPosHeadVec, sheepLeftEyeVec, 0, 0, 1, 1);
                drawLine(matrixStack, sheepPosHeadVec, sheepRightEyeVec, 0, 0, 1, 1);
            }
        }
    }
}
*/
