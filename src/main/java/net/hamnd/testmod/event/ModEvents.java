package net.hamnd.testmod.event;

import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.item.ModItems;
import net.hamnd.testmod.entity.villager.ModVillagers;
import net.minecraft.client.Minecraft;
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

import static net.hamnd.testmod.utils.Calculator.*;
import static net.hamnd.testmod.utils.DebugRenderer.*;

import java.util.*;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID)
public class ModEvents {
    public static PlayerEntity player;
    public static World world;

    public static Set<SheepEntity> sheeps = new HashSet<SheepEntity>();
    public static Iterator<SheepEntity> iterator;

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
    public static void onTick(TickEvent.PlayerTickEvent event){
        if(world == null){
            world = Minecraft.getInstance().world;
            System.out.println("WORLD iniated");
            System.out.println(world);
        }
        if(player == null){
            player = event.player;
            System.out.println("PLAYER iniated");
            System.out.println(player);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(event.getSource().getTrueSource() instanceof PlayerEntity & event.getEntityLiving() instanceof SheepEntity){
            sheeps.add((SheepEntity) event.getEntityLiving());
            iterator = sheeps.iterator();
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        if(sheeps == null)return;
        if(event.getEntityLiving() instanceof SheepEntity){
            sheeps.remove((SheepEntity) event.getEntityLiving());
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if(world == null || sheeps.isEmpty())return;
        if(world.getGameTime() % 3 == 0){
            for (Iterator<SheepEntity> i = sheeps.iterator(); i.hasNext();){
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
        }
    }

    @SubscribeEvent
    public static void onJPressed(InputEvent.KeyInputEvent event){
        // J key
        if(event.getKey() == 74 & event.getAction() == GLFW.GLFW_PRESS){
            sendMsg(Integer.toString(sheeps.size()));
            rayBool = !rayBool;
        }
    }

    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event){
        MatrixStack matrixStack = event.getMatrixStack();
        if(sheeps == null || !rayBool)return;
        for (Iterator<SheepEntity> i = sheeps.iterator(); i.hasNext();){
            SheepEntity sheep = i.next();
            Vector3d sheepPosVec = getSmoothPosVec(sheep);
            Vector3d sheepPosHeadVec = getSmoothPosHeadVec(sheep);
            Vector3d sheepLeftEyeVec = getSmoothAllEye(sheep, -0.125F);
            Vector3d sheepRightEyeVec = getSmoothAllEye(sheep, 0.125F);
            drawLine(matrixStack, new Vector3d(0,0,0), sheepPosVec, 1, 0, 0, 1);
            drawLine(matrixStack, sheepPosVec, sheepPosHeadVec, 0, 1, 0, 1);
            drawLine(matrixStack, sheepPosHeadVec, sheepLeftEyeVec, 0, 0, 1, 1);
            drawLine(matrixStack, sheepPosHeadVec, sheepRightEyeVec, 0, 0, 1, 1);
        }
    }
}
