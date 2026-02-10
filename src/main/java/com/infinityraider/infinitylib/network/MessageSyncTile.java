package com.infinityraider.infinitylib.network;

import com.infinityraider.infinitylib.InfinityLib;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageSyncTile extends MessageBase<IMessage> {
    private String className;
    private TileEntity tile;
    private NBTTagCompound tag;
    private boolean renderUpdate;

    public MessageSyncTile() {
        super();
    }

    public MessageSyncTile(TileEntity tile, boolean renderUpdate) {
        this();
        this.className = tile.getClass().getName();
        this.tile = tile;
        this.tag = tile.writeToNBT(new NBTTagCompound());
        this.renderUpdate = renderUpdate;
    }

    @Override
    public Side getMessageHandlerSide() {
        return Side.CLIENT;
    }

    @Override
    protected void processMessage(MessageContext ctx) {
        World world = InfinityLib.proxy.getClientWorld();
        if (this.tile != null && this.tile.getClass().toString().equals(this.className)) {
            this.tile.readFromNBT(this.tag);
            if (this.renderUpdate) {
                world.markBlockRangeForRenderUpdate(this.tile.getPos(), this.tile.getPos());
            }
        }
    }

    @Override
    protected IMessage getReply(MessageContext ctx) {
        return null;
    }
}
