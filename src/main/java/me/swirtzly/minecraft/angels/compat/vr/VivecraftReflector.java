package me.swirtzly.minecraft.angels.compat.vr;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public abstract class VivecraftReflector {
    public abstract boolean init();

    public abstract boolean isVRPlayer(PlayerEntity player);

    public abstract Vec3d getHMDPos(PlayerEntity player);

    public abstract Vec3d getHMDRot(PlayerEntity player);
}