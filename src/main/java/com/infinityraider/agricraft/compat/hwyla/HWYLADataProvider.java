package com.infinityraider.agricraft.compat.hwyla;

import com.infinityraider.agricraft.api.v1.misc.IAgriDisplayable;
import com.infinityraider.infinitylib.utility.WorldHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class HWYLADataProvider implements IWailaDataProvider {

    private final List<String> displayInfo = new ArrayList<>();

    @Nonnull
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> tooltip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        this.displayInfo.clear();

        World world = accessor.getWorld();
        BlockPos pos = accessor.getPosition();
        WorldHelper.getBlock(world, pos, IAgriDisplayable.class).ifPresent(e -> e.addDisplayInfo(this.displayInfo::add));
        WorldHelper.getTile(world, pos, IAgriDisplayable.class).ifPresent(e -> e.addDisplayInfo(this.displayInfo::add));
        tooltip.addAll(this.displayInfo);

        return tooltip;
    }

}
