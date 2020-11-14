package io.github.foundationgames.sculkconcept.callback;

import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItemCallbacks {
    public static void init() {
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            itemUsed(playerEntity, world, hand, playerEntity.getStackInHand(hand));
            return TypedActionResult.pass(playerEntity.getStackInHand(hand));
        });
    }

    public static void itemUsed(PlayerEntity player, World world, Hand hand, ItemStack stack) {
        if(stack.getItem() == Items.FISHING_ROD) vibration(world, player.getPos().add(0, 1.2, 0), 69);
        else if(stack.getItem() == Items.SHIELD) vibration(world, player.getPos().add(0, 1, 0), 50);
    }

    public static void itemUsing(ServerPlayerEntity player, World world, Hand hand, ItemStack stack, int timeUsed, int timeLeft) {
        if((stack.getItem().isFood() || stack.getItem().getUseAction(stack) == UseAction.DRINK) && timeUsed > 8 && timeUsed % 5 == 0) {
            vibration(world, player.getPos().add(0, 1.26, 0), 65);
        }
    }

    private static void vibration(World world, Vec3d pos, int radius) {
        if(!world.isClient()) {
            VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), radius);
        }
    }
    private static void vibration(World world, BlockPos pos, int radius) {
        vibration(world, new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), radius);
    }
}
