/*
 */
package com.agricraft.agricore.json;

import com.agricraft.agricore.config.AgriConfigCategory;
import com.agricraft.agricore.config.AgriConfigurable;
import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.registry.AgriLoadableRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author RlonRyan
 */
public final class AgriLoader {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @AgriConfigurable(key = "Enable JSON Writeback", category = AgriConfigCategory.CORE, comment = "Set to false to disable automatic JSON writeback.")
    public static boolean writeback = true;

    static {
        AgriCore.getConfig().addConfigurable(AgriLoader.class);
    }

    private AgriLoader() {
    }

    public static void loadDirectory(Path dir, AgriLoadableRegistry... registries) {
        try {
            Files.walkFileTree(dir, new AgriFileWalker(dir, registries));
        } catch (IOException e) {
            AgriCore.getCoreLogger().debug("Unable to load directory: \"{0}\"!", dir);
        }
    }

    protected static <T extends AgriSerializable> void loadElement(Path root, Path location, AgriLoadableRegistry<T> registry) {

        // The Element
        T obj;

        // Ensure File Exists
        if (!Files.exists(location)) {
            AgriCore.getCoreLogger().warn("Tried to load non-existant File: \"{0}\"!", location);
            return;
        }

        // Attempt to load element.
        // If fails, return.
        try (Reader reader = Files.newBufferedReader(location)) {
            obj = GSON.fromJson(reader, registry.getElementClass());
            obj.setPath(root.relativize(location).toString().replaceAll("\\\\", "/"));
        } catch (IOException | JsonParseException | NullPointerException e) {
            // IOException is thrown when unable to read file.
            // JsonParseExeption is thrown when JSON format is wrong.
            // NullPointerException is thrown when the file is empty.
            AgriCore.getCoreLogger().warn("Unable to load Element: \"{0}\"!", location);
            AgriCore.getCoreLogger().trace(e);
            return;
        }

        // Writeback, to keep file formatted.
        // If fails, ignore.
        if (writeback) {
            AgriSaver.saveElement(location, obj);
        }

        // Register the Element.
        registry.registerElement(obj);

    }

}
