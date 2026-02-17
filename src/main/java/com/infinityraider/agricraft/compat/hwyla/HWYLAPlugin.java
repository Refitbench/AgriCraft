package com.infinityraider.agricraft.compat.hwyla;

import com.infinityraider.agricraft.api.v1.plugin.AgriPlugin;
import com.infinityraider.agricraft.api.v1.plugin.IAgriPlugin;

@AgriPlugin
public class HWYLAPlugin implements IAgriPlugin {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "waila";
    }

    @Override
    public String getName() {
        return "HWYLA Integration";
    }

}