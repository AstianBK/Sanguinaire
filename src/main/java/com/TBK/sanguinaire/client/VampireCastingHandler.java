package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.Sanguinaire;
import com.TBK.sanguinaire.common.keybind.SGKeybinds;
import com.TBK.sanguinaire.common.registry.SGSkillAbstract;
import com.TBK.sanguinaire.server.Util;
import com.TBK.sanguinaire.server.capability.SkillPlayerCapability;
import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.capability.SGCapability;
import com.TBK.sanguinaire.server.manager.SkillAbstractInstance;
import com.TBK.sanguinaire.server.network.PacketHandler;
import com.TBK.sanguinaire.server.network.messager.PacketKeySync;
import com.TBK.sanguinaire.server.skill.SkillAbstract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class VampireCastingHandler {

    private static final List<String> inputSequence = new ArrayList<>();
    private static boolean casting = false;
    public static int spellBloodCost = 2;

    public static boolean isCasting() {
        return casting;
    }

    public static void addInput(String input) {
        if (inputSequence.size() >= 6) return;
        inputSequence.add(input);
    }

    public static List<String> getInputSequence() {
        return inputSequence;
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;


        VampirePlayerCapability cap = SGCapability.getEntityVam(player, VampirePlayerCapability.class);
        if (cap == null || !cap.isVampire()) return;

        long window = Minecraft.getInstance().getWindow().getWindow();
        boolean altDown = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS || SGKeybinds.attackKey3.isDown();

        if (altDown && !casting) {
            casting = true;
        } else if (!altDown && casting) {
            casting = false;
            inputSequence.clear();
        }
    }


    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!casting) return;


        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if (!Util.isVampire(player)) return;
        String input = switch (event.getKey()) {
            case GLFW.GLFW_KEY_UP -> "UP";
            case GLFW.GLFW_KEY_DOWN -> "DOWN";
            case GLFW.GLFW_KEY_LEFT -> "LEFT";
            case GLFW.GLFW_KEY_RIGHT -> "RIGHT";
            default -> null;
        };

        if (input != null && event.getAction() == GLFW.GLFW_PRESS) {
            inputSequence.add(input);

            if (checkSpell(inputSequence, player)) {
                inputSequence.clear();
                casting = false;
            } else if (inputSequence.size() >= 6) {
                inputSequence.clear();
                casting = false;
            }
        }
    }

    private static boolean checkSpell(List<String> sequence, Player player) {
        SkillPlayerCapability cap = SkillPlayerCapability.get(player);
        for (SkillAbstractInstance skillAbstract : cap.getHotBarSkill().getSkills()){
            if(skillAbstract.getSkillAbstract().checkSequence(sequence)){
                PacketHandler.sendToServer(new PacketKeySync(0x52,1,-1,skillAbstract.getSkillAbstract().name));
            }
        }
        return false;
    }
}
