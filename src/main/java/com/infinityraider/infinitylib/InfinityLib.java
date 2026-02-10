package com.infinityraider.infinitylib;

import com.infinityraider.infinitylib.config.IModConfiguration;
import com.infinityraider.infinitylib.config.ModConfiguration;
import com.infinityraider.infinitylib.modules.Module;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import com.infinityraider.infinitylib.network.MessageSetEntityDead;
import com.infinityraider.infinitylib.sound.MessagePlaySound;
import com.infinityraider.infinitylib.network.MessageSyncTile;
import com.infinityraider.infinitylib.proxy.IProxy;
import com.infinityraider.infinitylib.sound.MessageStopSound;
import infinityraider.infinitylib.Tags;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = Tags.LIBRARY_ID, name = Tags.LIBRARY_ID, version = Tags.LIBRARY_VERSION)
public class InfinityLib extends InfinityMod {

    @Mod.Instance(Tags.LIBRARY_ID)
    public static InfinityLib instance;

    @SidedProxy(
            clientSide = "com.infinityraider.infinitylib.proxy.ClientProxy",
            serverSide = "com.infinityraider.infinitylib.proxy.ServerProxy"
    )
    public static IProxy proxy;

    @Override
    public IProxy proxy() {
        return proxy;
    }

    @Override
    public String getModId() {
        return Tags.LIBRARY_ID;
    }

    @Override
    public IModConfiguration getConfiguration() {
        return ModConfiguration.getInstance();
    }

    @Override
    public void registerMessages(INetworkWrapper wrapper) {
        wrapper.registerMessage(MessageSetEntityDead.class);
        wrapper.registerMessage(MessageSyncTile.class);
        wrapper.registerMessage(MessagePlaySound.class);
        wrapper.registerMessage(MessageStopSound.class);
        Module.getActiveModules().stream().sorted().forEach(m -> m.registerMessages(wrapper));
    }
}