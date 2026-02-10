package com.infinityraider.infinitylib.network.serialization;

import com.infinityraider.infinitylib.InfinityLib;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class ByteBufUtil {
    public static ByteBuf writeBoolean(ByteBuf buf, boolean data) {
        buf.writeBoolean(data);
        return buf;
    }

    public static boolean readBoolean(ByteBuf buf) {
        return buf.readBoolean();
    }

    public static ByteBuf writeByte(ByteBuf buf, byte data) {
        buf.writeByte(data);
        return buf;
    }

    public static byte readByte(ByteBuf buf) {
        return buf.readByte();
    }

    public static ByteBuf writeShort(ByteBuf buf, short data) {
        buf.writeShort(data);
        return buf;
    }

    public static short readShort(ByteBuf buf) {
        return buf.readShort();
    }

    public static ByteBuf writeInt(ByteBuf buf, int data) {
        buf.writeInt(data);
        return buf;
    }

    public static int readInt(ByteBuf buf) {
        return buf.readInt();
    }

    public static ByteBuf writeLong(ByteBuf buf, long data) {
        buf.writeLong(data);
        return buf;
    }

    public static long readLong(ByteBuf buf) {
        return buf.readLong();
    }

    public static ByteBuf writeFloat(ByteBuf buf, float data) {
        buf.writeFloat(data);
        return buf;
    }

    public static float readFloat(ByteBuf buf) {
        return buf.readFloat();
    }

    public static ByteBuf writeDouble(ByteBuf buf, double data) {
        buf.writeDouble(data);
        return buf;
    }

    public static double readDouble(ByteBuf buf) {
        return buf.readDouble();
    }

    public static ByteBuf writeChar(ByteBuf buf, char data) {
        buf.writeChar(data);
        return buf;
    }

    public static char readChar(ByteBuf buf) {
        return buf.readChar();
    }

    public static ByteBuf writeString(ByteBuf buf, String string) {
        ByteBufUtils.writeUTF8String(buf, string);
        return buf;
    }
    
    public static String readString(ByteBuf buf) {
        return ByteBufUtils.readUTF8String(buf);
    }

    public static ByteBuf writeEntity(ByteBuf buf, Entity e) {
        if (e == null) {
            buf.writeInt(-1);
        } else {
            buf.writeInt(e.getEntityId());
            buf.writeInt(e.getEntityWorld().provider.getDimension());
        }
        return buf;
    }

    public static Entity readEntity(ByteBuf buf) {
        int id = buf.readInt();
        if(id < 0) {
            return null;
        }
        int dimension = buf.readInt();
        return InfinityLib.proxy.getEntityById(dimension, id);
    }

    public static ByteBuf writeTileEntity(ByteBuf buf, TileEntity tile) {
        writeInt(buf, tile.getWorld().provider.getDimension());
        return writeBlockPos(buf, tile.getPos());
    }

    public static TileEntity readTileEntity(ByteBuf buf) {
        int dimension = readInt(buf);
        BlockPos pos = readBlockPos(buf);
        World world = InfinityLib.proxy.getWorldByDimensionId(dimension);
        return world == null ? null : world.getTileEntity(pos);
    }

    public static ByteBuf writeBlockPos(ByteBuf buf, BlockPos pos) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        return buf;
    }

    public static BlockPos readBlockPos(ByteBuf buf) {
        return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static ByteBuf writeBlock(ByteBuf buf, Block block) {
        String blockName = block == null ? "null" : Block.REGISTRY.getNameForObject(block).toString();
        buf.writeInt(blockName.length());
        buf.writeBytes(blockName.getBytes());
        return buf;
    }

    public static Block readBlock(ByteBuf buf) {
        int itemNameLength = buf.readInt();
        String itemName = new String(buf.readBytes(itemNameLength).array());
        return Block.REGISTRY.getObject(new ResourceLocation(itemName));
    }

    public static ByteBuf writeItem(ByteBuf buf, Item item) {
        String itemName = item == null ? "null" : Item.REGISTRY.getNameForObject(item).toString();
        buf.writeInt(itemName.length());
        buf.writeBytes(itemName.getBytes());
        return buf;
    }

    public static Item readItem(ByteBuf buf) {
        int itemNameLength = buf.readInt();
        String itemName = new String(buf.readBytes(itemNameLength).array());
        return Item.REGISTRY.getObject(new ResourceLocation(itemName));
    }

    public static ByteBuf writeItemStack(ByteBuf buf, ItemStack stack) {
        ByteBufUtils.writeItemStack(buf, stack);
        return buf;
    }

    public static ItemStack readItemStack(ByteBuf buf) {
        return ByteBufUtils.readItemStack(buf);
    }

    public static ByteBuf writeNBT(ByteBuf buf, NBTTagCompound tag) {
        ByteBufUtils.writeTag(buf, tag);
        return buf;
    }

    public static NBTTagCompound readNBT(ByteBuf buf) {
        return ByteBufUtils.readTag(buf);
    }

    public static ByteBuf writeVec3d(ByteBuf buf, Vec3d data) {
        buf.writeDouble(data.x);
        buf.writeDouble(data.y);
        buf.writeDouble(data.z);
        return buf;
    }

    public static Vec3d readVec3d(ByteBuf buf) {
        return new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static ByteBuf writeTextComponent(ByteBuf buf, ITextComponent component) {
        return writeString(buf, ITextComponent.Serializer.componentToJson(component));
    }

    public static ITextComponent readTextComponent(ByteBuf buf) {
        return ITextComponent.Serializer.jsonToComponent(readString(buf));
    }
}
