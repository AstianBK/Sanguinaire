package com.TBK.sanguinaire.client;

import com.TBK.sanguinaire.server.capability.VampirePlayerCapability;
import com.TBK.sanguinaire.server.capability.SGCapability;
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

    // ------------------------------
    // Client tick for Alt detection
    // ------------------------------
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        VampirePlayerCapability cap = SGCapability.getEntityVam(player, VampirePlayerCapability.class);
        if (cap == null || !cap.isVampire()) return;

        long window = Minecraft.getInstance().getWindow().getWindow();
        boolean altDown = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;

        if (altDown && !casting) {
            casting = true;
        } else if (!altDown && casting) {
            casting = false;
            inputSequence.clear();
        }
    }

    // ------------------------------
    // Key input while casting
    // ------------------------------
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        if (!casting) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        VampirePlayerCapability cap = SGCapability.getEntityVam(player, VampirePlayerCapability.class);
        if (cap == null || !cap.isVampire()) return;

        String input = switch (event.getKey()) {
            case GLFW.GLFW_KEY_UP -> "UP";
            case GLFW.GLFW_KEY_DOWN -> "DOWN";
            case GLFW.GLFW_KEY_LEFT -> "LEFT";
            case GLFW.GLFW_KEY_RIGHT -> "RIGHT";
            default -> null;
        };

        if (input != null && event.getAction() == GLFW.GLFW_PRESS) {
            inputSequence.add(input);

            if (checkSpell(inputSequence, cap, player)) {
                inputSequence.clear();
                casting = false;
            } else if (inputSequence.size() >= 6) {
                inputSequence.clear();
                casting = false;
            }
        }
    }

    // ------------------------------
    // Spell check
    // ------------------------------
    private static boolean checkSpell(List<String> sequence, VampirePlayerCapability cap, Player player) {
        // Example: LEFT LEFT LEFT --> levitation
        if (sequence.size() == 3) {
            if (sequence.get(0).equals("LEFT") &&
                    sequence.get(1).equals("LEFT") &&
                    sequence.get(2).equals("LEFT")) {

                if (cap.getBlood() >= spellBloodCost) {
                    cap.loseBlood(spellBloodCost);

                    player.level().getEntities(player, player.getBoundingBox().inflate(5), e -> e instanceof LivingEntity)
                            .forEach(e -> ((LivingEntity) e).addEffect(new MobEffectInstance(MobEffects.LEVITATION, 12 * 20, 0)));

                    player.level().playSound(null, player.blockPosition(), SoundEvents.BEEHIVE_ENTER,
                            player.getSoundSource(), 1.0F, 1.0F);

                    return true;
                }
            }
        }
        return false;
    }
}
