/*
 */
package com.infinityraider.agricraft.utility;

import com.agricraft.agricore.config.AgriConfigCategory;
import com.agricraft.agricore.config.AgriConfigurable;
import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.log.AgriLogger;
import com.agricraft.agricore.util.TypeHelper;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import infinityraider.infinitylib.Tags;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Removes the annoying model errors. Credits go to TTerrag.
 */
@SideOnly(Side.CLIENT)
public class ModelErrorSuppressor {

    private static final List<String> IGNORES = TypeHelper.asList(Tags.MOD_ID, Tags.MOD_ID + "item");

    @AgriConfigurable(key = "Suppress Model Errors", category = AgriConfigCategory.DEBUG, comment = "Set to true to prevent any annoying AgriCraft model loading errors from spamming the log.")
    private static boolean supressErrors = true;

    @SubscribeEvent
    public void onModelBakePost(ModelBakeEvent event) {
        if (supressErrors) {
            Map<ResourceLocation, Exception> loadingExceptions = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "loadingExceptions");
            Set<ModelResourceLocation> missingVariants = ReflectionHelper.getPrivateValue(ModelLoader.class, event.getModelLoader(), "missingVariants");

            int exceptionStartingCount = loadingExceptions.size();
            int missingStartingCount = missingVariants.size();

            List<ResourceLocation> errored = loadingExceptions.keySet().stream().filter((r) -> IGNORES.contains(r.getNamespace())).collect(Collectors.toList());
            List<ResourceLocation> missing = missingVariants.stream().filter((r) -> IGNORES.contains(r.getNamespace())).collect(Collectors.toList());

            loadingExceptions.entrySet().removeIf(e -> IGNORES.contains(e.getKey().getNamespace()));
            missing.removeIf(rl -> IGNORES.contains(rl.getNamespace()));

            AgriLogger logger = AgriCore.getLogger(Tags.MOD_ID);
            logger.info("Suppressed {0} Model Loading Errors!", exceptionStartingCount - errored.size());
            logger.info("Suppressed {0} Missing Model Variants!", missingStartingCount - missing.size());
        }
    }

    static {
        AgriCore.getConfig().addConfigurable(ModelErrorSuppressor.class);
    }

}
