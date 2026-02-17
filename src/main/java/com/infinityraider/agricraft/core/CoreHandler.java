package com.infinityraider.agricraft.core;

import com.agricraft.agricore.config.AgriConfigAdapter;
import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.json.AgriLoader;
import com.agricraft.agricore.log.AgriLogger;
import com.agricraft.agricore.plant.AgriMutation;
import com.agricraft.agricore.plant.AgriPlant;
import com.agricraft.agricore.plant.AgriSoil;
import com.agricraft.agricore.util.ResourceHelper;
import com.infinityraider.agricraft.api.v1.AgriApi;
import com.infinityraider.agricraft.api.v1.mutation.IAgriMutation;
import com.infinityraider.agricraft.api.v1.plant.IAgriPlant;
import com.infinityraider.agricraft.api.v1.soil.IAgriSoil;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

import infinityraider.infinitylib.Tags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static infinityraider.infinitylib.Tags.MOD_ID;

public final class CoreHandler {

    private static Path configDir;
    private static Path jsonDir;
    private static Path defaultDir;
    private static Configuration config;

    private CoreHandler() {
    }

    public static Configuration getConfig() {
        return config;
    }

    public static Path getConfigDir() {
        return configDir;
    }

    public static Path getJsonDir() {
        return jsonDir;
    }

    public static void preInit(FMLPreInitializationEvent event) {
        // Setup Config.
        configDir = event.getSuggestedConfigurationFile().getParentFile().toPath().resolve(Tags.MOD_ID);
        config = new Configuration(configDir.resolve("config.cfg").toFile());

        // Setup Plant Dir.
        jsonDir = configDir.resolve("json");
        defaultDir = jsonDir.resolve("defaults");

        // Setup Provider
        AgriConfigAdapter provider = new ModProvider(config);
        MinecraftForge.EVENT_BUS.register(provider);

        // Initialize AgriCore
        AgriCore.init(new ModLogger(), new ModTranslator(), new ModValidator(), new ModConverter(), provider);

        // Transfer Defaults
        ResourceHelper.findJsonResources().forEach(r -> ResourceHelper.copyResource(r, configDir.resolve(r), false));

        // Load the JSON files.
        loadJsons();
    }

    public static void init() {
        // Save settings!
        AgriCore.getConfig().save();

        // Load JSON Stuff
        initSoils();
        initPlants();
        initMutations();
    }

    public static void loadJsons() {
        AgriLogger logger = AgriCore.getLogger(MOD_ID);
        // Load the core!
        logger.info("Attempting to read AgriCraft JSONs!");
        AgriLoader.loadDirectory(defaultDir, AgriCore.getSoils(), AgriCore.getPlants(), AgriCore.getMutations());
        logger.info("Finished trying to read AgriCraft JSONs!");
    }

    public static void initSoils() {
        AgriLogger logger = AgriCore.getLogger(MOD_ID);
        // Announce Progress
        logger.info("Registering Soils!");

        // See if soils are valid...
        final int raw = AgriCore.getSoils().getAll().size();
        AgriCore.getSoils().validate();
        final int count = AgriCore.getSoils().getAll().size();

        // Transfer
        AgriCore.getSoils().getAll().stream().filter(AgriSoil::isEnabled).map(JsonSoil::new).forEach(AgriApi.getSoilRegistry()::add);

        // Display Soils
        logger.info("Registered Soils ({0}/{1}):", count, raw);
        for (IAgriSoil soil : AgriApi.getSoilRegistry().all()) {
            logger.info(" - {0}", soil.getName());
        }
    }

    public static void initPlants() {
        AgriLogger logger = AgriCore.getLogger(MOD_ID);
        // Announce Progress
        logger.info("Registering Plants!");

        // See if plants are valid...
        final int raw = AgriCore.getPlants().getAll().size();
        AgriCore.getPlants().validate();
        final int count = AgriCore.getPlants().getAll().size();

        // Transfer
        AgriCore.getPlants().validate();
        AgriCore.getPlants().getAll().stream().filter(AgriPlant::isEnabled).map(JsonPlant::new).forEach(AgriApi.getPlantRegistry()::add);

        // Display Plants
        logger.info("Registered Plants ({0}/{1}):", count, raw);
        for (IAgriPlant plant : AgriApi.getPlantRegistry().all()) {
            logger.info(" - {0}", plant.getPlantName());
        }
    }

    public static void initMutations() {
        AgriLogger logger = AgriCore.getLogger(MOD_ID);
        // Announce Progress
        logger.info("Registering Mutations!");

        // See if mutations are valid...
        final int raw = AgriCore.getMutations().getAll().size();
        AgriCore.getMutations().validate();
        final int count = AgriCore.getMutations().getAll().size();

        // Transfer
        AgriCore.getMutations().getAll().stream()
                .filter(AgriMutation::isEnabled)
                .map(JsonHelper::wrap)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(AgriApi.getMutationRegistry()::add);

        // Display Mutations
        logger.info("Registered Mutations ({0}/{1}):", count, raw);
        for (IAgriMutation mutation : AgriApi.getMutationRegistry().all()) {
            logger.info(" - {0}", mutation);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void loadTextures(Consumer<ResourceLocation> consumer) {
        AgriCore.getPlants().getAll().stream()
                .flatMap(plant -> plant.getTexture().getAllTextures().stream())
                .distinct()
                .map(ResourceLocation::new)
                .forEach(consumer);
    }

}
