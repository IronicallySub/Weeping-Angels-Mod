package me.swirtzly.minecraft.angels.compat.vr;

import me.swirtzly.minecraft.angels.WeepingAngels;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

public class ServerReflector extends VivecraftReflector {
    // Keeps track of what part of Vivecraft is detected.
    // 0 = Vivecraft Client/Non-VR companion
    // 1 = Vivecraft Forge Extensions
    // -1 = Vanilla Forge
    public int enabled = -1;

    // *** Vivecraft Client/Non-VR Companion methods and fields ***
    private Field fVivePlayers;

    private Method mGetHMDDir;
    private Method mGetHMDPos;
    private Method isVR;

    // *** Vivecraft Forge Extensions methods and fields ***
    private Constructor<?> conQuaternion;

    private Field fHead;
    private Field fController0;
    private Field fController1;

    private Field fSeated;
    private Field fWorldScale;

    private Field fPosX;
    private Field fPosY;
    private Field fPosZ;
    private Field fRotW;
    private Field fRotX;
    private Field fRotY;
    private Field fRotZ;

    private Method mGetPlayerData;
    private Method mHasPlayerData;

    private Method mVecMultiply;

    /**
     * Initializes the bridge. Since reflection is used we detect if the classes/methods/fields are
     * present, and store them for later use. Must be run once before any other function is used, likely
     * during post-init.
     *
     * @return Returns true if any version of vivecraft was detected.
     */
    @Override
    public boolean init() {
        enabled = 0;
        WeepingAngels.LOGGER.info("Checking for Vivecraft Client...");
        try {

            //Detect classes and fields from the Vivecraft Client/Non-VR Companion
            //Vivecraft Client/Non-VR
            Class<?> cNetworkHelper = Class.forName("org.vivecraft.api.NetworkHelper");
            Class<?> cVivePlayer = Class.forName("org.vivecraft.api.ServerVivePlayer");

            fVivePlayers = cNetworkHelper.getDeclaredField("vivePlayers");

            mGetHMDDir = cVivePlayer.getDeclaredMethod("getHMDDir");
            mGetHMDPos = cVivePlayer.getDeclaredMethod("getHMDPos");
            isVR = cVivePlayer.getMethod("isVR");

            WeepingAngels.LOGGER.info("Vivecraft Client detected! Enabling compatibility features.");

        }catch (Exception e){
            enabled = -1;
        }

        if(enabled<0)
        {
            //Vivecraft Forge Extensions
            enabled = 1;
            try{
                Class<?> cVRPlayerData = Class.forName("com.techjar.vivecraftforge.util.VRPlayerData");
                Class<?> cObjectInfo = Class.forName("com.techjar.vivecraftforge.util.VRPlayerData$ObjectInfo");

                fHead = cVRPlayerData.getDeclaredField("head");
                fController0 = cVRPlayerData.getDeclaredField("controller0");
                fController1 = cVRPlayerData.getDeclaredField("controller1");

                fSeated = cVRPlayerData.getDeclaredField("seated");
                fWorldScale = cVRPlayerData.getDeclaredField("worldScale");

                fPosX = cObjectInfo.getDeclaredField("posX");
                fPosY = cObjectInfo.getDeclaredField("posY");
                fPosZ = cObjectInfo.getDeclaredField("posZ");
                fRotW = cObjectInfo.getDeclaredField("rotW");
                fRotX = cObjectInfo.getDeclaredField("rotX");
                fRotY = cObjectInfo.getDeclaredField("rotY");
                fRotZ = cObjectInfo.getDeclaredField("rotZ");

                Class<?> cPlayerTracker = Class.forName("com.techjar.vivecraftforge.util.PlayerTracker");

                mGetPlayerData = cPlayerTracker.getMethod("getPlayerData", PlayerEntity.class);
                mHasPlayerData = cPlayerTracker.getMethod("hasPlayerData", PlayerEntity.class);

                Class<?> cQuaternion = Class.forName("com.techjar.vivecraftforge.util.Quaternion");

                conQuaternion = cQuaternion.getConstructor(float.class,float.class,float.class,float.class);
                mVecMultiply = cQuaternion.getMethod("multiply", Vec3d.class);

                WeepingAngels.LOGGER.info("Vivecraft Forge Extensions detected! Enabling compatability features.");
            }catch (Exception e)
            {
                enabled = -1;
            }
        }

        if(enabled<0)
            WeepingAngels.LOGGER.info("Vivecraft not detected!");

        return enabled>=0;
    }

    /**
     * Checks to see if the player is currently using a VR headset.
     *
     * @param player The Player
     * @return Returns true if the player is listed in the VR player lists.
     */
    @Override
    public boolean isVRPlayer(PlayerEntity player) {
        if(enabled<0)return false;
        try {
            UUID uuid = player.getUniqueID();
            if(enabled==0) {
                Map<UUID,?> vivePlayers = (Map<UUID,? extends Object>)fVivePlayers.get(null);
                if (vivePlayers.containsKey(uuid)) {
                    Object vivePlayer = vivePlayers.get(uuid);
                    return (boolean) isVR.invoke(vivePlayer);
                }
            }
            else if(enabled==1) {
                return (boolean) mHasPlayerData.invoke(null, player);
            }

        } catch (Exception e) {
            WeepingAngels.LOGGER.warn("Vivecraft Server: Unknown Error Parsing isVRPlayer", e);
        }

        return false;
    }

    /**
     * Gets the position of the player's headset. Seems to be in world-space
     *
     * @param player The Player
     * @return The position
     */
    @Override
    public Vec3d getHMDPos(PlayerEntity player) {
        try {
            if(enabled==0) {
                UUID uuid = player.getUniqueID();
                //Network Character - attempt to get from NetworkHelper
                Map<UUID,?> vivePlayers = (Map<UUID,? extends Object>)fVivePlayers.get(null);
                Object vivePlayer = vivePlayers.get(uuid);
                return (Vec3d) mGetHMDPos.invoke(vivePlayer);
            }
            else if(enabled==1)
            {
                Object playerHead = fHead.get(mGetPlayerData.invoke(null, player));

                float X = fPosX.getFloat(playerHead);
                float Y = fPosY.getFloat(playerHead);
                float Z = fPosZ.getFloat(playerHead);

                return new Vec3d(X, Y, Z);
            }

        } catch (Exception e) {
            WeepingAngels.LOGGER.warn("Vivecraft Server: Unknown Error Parsing getHMDPos", e);
        }
        return player.getPositionVec().add(0, 1.62, 0);
    }


    /**
     * Gets a vector representing the direction the headset is facing. Does not include side-to-side tilts.
     *
     * @param player The Player
     * @return The direction vector
     */
    @Override
    public Vec3d getHMDRot(PlayerEntity player) {
        try {
            UUID uuid = player.getUniqueID();
            if (enabled == 0) {
                //Network Character - attempt to get from NetworkHelper
                Map<UUID, ?> vivePlayers = (Map<UUID, ? extends Object>) fVivePlayers.get(null);
                Object vivePlayer = vivePlayers.get(uuid);
                return (Vec3d) mGetHMDDir.invoke(vivePlayer);
            } else if (enabled == 1) {
                Object playerHead = fHead.get(mGetPlayerData.invoke(null, player));

                float W = fRotW.getFloat(playerHead);
                float X = fRotX.getFloat(playerHead);
                float Y = fRotY.getFloat(playerHead);
                float Z = fRotZ.getFloat(playerHead);

                Object quaternion = conQuaternion.newInstance(W, X, Y, Z);

                //mNormalize.invoke(quaternion);

                return (Vec3d) mVecMultiply.invoke(quaternion, new Vec3d(0, 0, -1));
            }
        } catch (Exception e) {
            WeepingAngels.LOGGER.warn("Vivecraft Server: Unknown Error Parsing getHMDRot", e);
        }
        return player.getLookVec();
    }
}
