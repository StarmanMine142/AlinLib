package ru.kelcuprum.alinlib.info;

import net.minecraft.client.User;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import ru.kelcuprum.alinlib.AlinLib;
import ru.kelcuprum.alinlib.config.Localization;

import static net.minecraft.world.item.Items.AIR;

public class Player {
    public static String getName(){
        return Stealth.getParsedName(AlinLib.MINECRAFT.getUser().getName());
    }
    public static String getUUID(){
        return AlinLib.MINECRAFT.getUser().getProfileId().toString();
    }
    public static String getProfileType(){
        return AlinLib.MINECRAFT.getUser().getType().getName();
    }

    public static boolean isLicenseAccount(){
        return AlinLib.MINECRAFT.getUser().getType() == User.Type.MSA || AlinLib.MINECRAFT.getUser().getType() == User.Type.MOJANG;
    }

    public static String getItemName(){
        if(AlinLib.MINECRAFT.player == null) return "";
        ItemStack main_hand = getItemInHand(false);
        ItemStack off_hand = getItemInHand(true);
        if(!main_hand.isEmpty()) return main_hand.getHoverName().getString();
        else if(!off_hand.isEmpty() && AlinLib.bariumConfig.getBoolean("VIEW.ITEM_OFF_HAND", false)) return off_hand.getHoverName().getString();
        else return "";
    }
    public static int getItemCount(){
        if(AlinLib.MINECRAFT.player == null) return 0;
        ItemStack main_hand = getItemInHand(false);
        ItemStack off_hand = getItemInHand(true);
        if(!main_hand.isEmpty()) return main_hand.getCount();
        else if(!off_hand.isEmpty() && AlinLib.bariumConfig.getBoolean("VIEW.ITEM_OFF_HAND", false)) return off_hand.getCount();
        else return 0;
    }

    public static ItemStack getItemInHand(boolean isOffhand){
        if(AlinLib.MINECRAFT.player == null) return AIR.getDefaultInstance();
        return AlinLib.MINECRAFT.player.getItemInHand(isOffhand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
    }

    public static double getHealth(){
        if(AlinLib.MINECRAFT.player == null) return 0;
        return Localization.round(AlinLib.MINECRAFT.player.getHealth()/2, 2);
    }
    public static double getMaxHealth(){
        if(AlinLib.MINECRAFT.player == null) return 0;
        return Localization.round(AlinLib.MINECRAFT.player.getAttributeValue(Attributes.MAX_HEALTH)/2, 2);
    }
    public static double getPercentHealth(){
        if(AlinLib.MINECRAFT.player == null) return 0;
        return Localization.round((AlinLib.MINECRAFT.player.getHealth()*100)/AlinLib.MINECRAFT.player.getMaxHealth(), 2);
    }
    public static double getArmor(){
        if(AlinLib.MINECRAFT.player == null) return 0;
        return Localization.round((double) AlinLib.MINECRAFT.player.getArmorValue() /2, 2);
    }
    public static int getHunger(){
        if(AlinLib.MINECRAFT.player == null) return 0;
        return AlinLib.MINECRAFT.player.getFoodData().getFoodLevel();
    }
    public static double getX(){
        if(AlinLib.MINECRAFT.getCameraEntity() == null) return 404;
        double x =  Stealth.getFunnyValueCoordinate(AlinLib.MINECRAFT.getCameraEntity().getX(), (AlinLib.MINECRAFT.isLocalServer() || AlinLib.MINECRAFT.isSingleplayer()) ? "single" : AlinLib.MINECRAFT.getCurrentServer().ip, World.getCodeName(), false);
        return Localization.getDoubleRounding(x, !AlinLib.bariumConfig.getBoolean("LOCALIZATION.EXTENDED_COORDINATES", false));
    }
    public static double getY(){
        if(AlinLib.MINECRAFT.getCameraEntity() == null) return 404;
        double y = AlinLib.MINECRAFT.getCameraEntity().getY();
        return Localization.getDoubleRounding(y, !AlinLib.bariumConfig.getBoolean("LOCALIZATION.EXTENDED_COORDINATES", false));
    }
    public static double getZ(){
        if(AlinLib.MINECRAFT.getCameraEntity() == null) return 404;
        double z =  Stealth.getFunnyValueCoordinate(AlinLib.MINECRAFT.getCameraEntity().getZ(), (AlinLib.MINECRAFT.isLocalServer() || AlinLib.MINECRAFT.isSingleplayer()) ? "single" : AlinLib.MINECRAFT.getCurrentServer().ip, World.getCodeName(), false);
        return Localization.getDoubleRounding(z, !AlinLib.bariumConfig.getBoolean("LOCALIZATION.EXTENDED_COORDINATES", false));
    }
    public static int getPing(){
        if(AlinLib.MINECRAFT.getCameraEntity() == null && !(AlinLib.MINECRAFT.isSingleplayer() || AlinLib.MINECRAFT.isLocalServer()) && AlinLib.MINECRAFT.getConnection() == null) return -1;
        if(AlinLib.MINECRAFT.getConnection().getPlayerInfo(AlinLib.MINECRAFT.getCameraEntity().getUUID()) != null) return AlinLib.MINECRAFT.getConnection().getPlayerInfo(AlinLib.MINECRAFT.player.getUUID()).getLatency();
        return 0;
    }
    public static Direction getDirection(){
        if(AlinLib.MINECRAFT.player == null) return Direction.NORTH;
        return AlinLib.MINECRAFT.player.getDirection();
    }
    public static String getDirection(boolean oneSymbol){
        Direction direction = getDirection();
        if(AlinLib.bariumConfig.getBoolean("STREAMER.STEALTH", false) && AlinLib.bariumConfig.getBoolean("STREAMER.STEALTH.DIRECTION", true)){
            switch (direction) {
                case NORTH -> direction = Direction.EAST;
                case SOUTH -> direction = Direction.WEST;

                case WEST -> direction = Direction.NORTH;
                case EAST -> direction = Direction.SOUTH;
            }
        }
        return  switch (direction) {
            case NORTH -> oneSymbol ? "N" : AlinLib.localization.getLocalization("north", false, false);
            case SOUTH -> oneSymbol ? "S" : AlinLib.localization.getLocalization("south", false, false);

            case WEST -> oneSymbol ? "W" : AlinLib.localization.getLocalization("west", false, false);
            case EAST -> oneSymbol ? "E" : AlinLib.localization.getLocalization("east", false, false);

            default -> oneSymbol ? "?" : AlinLib.localization.getLocalization("unknown", false, false);
        };
    }
    public static String getDebugDirection(boolean oneSymbol){
        Direction direction = getDirection();
        if(AlinLib.bariumConfig.getBoolean("STREAMER.STEALTH", false) && AlinLib.bariumConfig.getBoolean("STREAMER.STEALTH.DIRECTION", true)){
            switch (direction) {
                case NORTH -> direction = Direction.EAST;
                case SOUTH -> direction = Direction.WEST;

                case WEST -> direction = Direction.NORTH;
                case EAST -> direction = Direction.SOUTH;
            }
        }
        return switch (direction) {
            case NORTH -> oneSymbol ? "-Z" : AlinLib.localization.getLocalization("north.debug", false, false);
            case SOUTH -> oneSymbol ? "+Z" : AlinLib.localization.getLocalization("south.debug", false, false);
            case WEST -> oneSymbol ? "-X" : AlinLib.localization.getLocalization("west.debug", false, false);
            case EAST -> oneSymbol ? "+X" : AlinLib.localization.getLocalization("east.debug", false, false);

            default -> oneSymbol ? "?" : AlinLib.localization.getLocalization("unknown", false, false);
        };
    }
}
