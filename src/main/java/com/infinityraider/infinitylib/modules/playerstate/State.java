package com.infinityraider.infinitylib.modules.playerstate;

import com.infinityraider.infinitylib.InfinityLib;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;

public class State {
    /** Player pointer */
    private final EntityPlayer player;

    /** Invisible to everything */
    private boolean invisible;
    /** Invulnerable to all damage */
    private boolean invulnerable;
    /** Non colliding with entities */
    private boolean ethereal;
    /** Mobs do not target the player */
    private boolean undetectable;

    State(EntityPlayer player) {
        this.player = player;
    }

    public EntityPlayer getPlayer() {
        return this.player;
    }

    public State setInvisible(boolean status) {
        if(status != this.invisible) {
            this.invisible = status;
            this.syncToClient();
        }
        return this;
    }

    public State setInvulnerable(boolean status) {
        if(status != this.invulnerable) {
            this.invulnerable = status;
            this.syncToClient();
        }
        return this;
    }

    public State setEthereal(boolean status) {
        if(status != this.ethereal) {
            this.ethereal = status;
            this.syncToClient();
        }
        return this;
    }

    public State setUndetectable(boolean status) {
        if(status != this.undetectable) {
            this.undetectable = status;
            this.syncToClient();
        }
        return this;
    }

    public boolean isInvisible() {
        return this.invisible;
    }

    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    public boolean isEthereal() {
        return this.ethereal;
    }

    public boolean isUndetectable() {
        return this.undetectable;
    }

    private void syncToClient() {
        if(InfinityLib.proxy.getEffectiveSide() == Side.SERVER) {
            new MessageSyncState(getPlayer(), this).sendToAll();
        }
    }
}
