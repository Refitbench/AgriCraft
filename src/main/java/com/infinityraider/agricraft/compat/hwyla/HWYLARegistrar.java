package com.infinityraider.agricraft.compat.hwyla;

import com.infinityraider.agricraft.api.v1.misc.IAgriDisplayable;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class HWYLARegistrar implements IWailaPlugin {

    @Override
    public void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new HWYLADataProvider(), IAgriDisplayable.class);
    }

}
