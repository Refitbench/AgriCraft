/*
 */
package com.agricraft.agricore.json;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.registry.AgriLoadableRegistry;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

/**
 *
 * @author Ryan
 */
public class AgriFileWalker extends SimpleFileVisitor<Path> {

    public static final String MOD_PREFIX = "mod_";

    private final AgriLoadableRegistry<? extends AgriSerializable>[] registries;
    private final Path root;

    public AgriFileWalker(Path root, AgriLoadableRegistry<? extends AgriSerializable>... registries) {
        this.root = Objects.requireNonNull(root);
        this.registries = Objects.requireNonNull(registries);
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        // Filter null files.
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);

        // Get Localized Directory
        final String loc = root.relativize(dir).toString();

        // Get Directory Name
        final String dirname = dir.getFileName().toString();

        if (!dirname.startsWith(MOD_PREFIX)) {
            if (!loc.isEmpty() && !loc.contains("\\")) {
                AgriCore.getCoreLogger().debug("Loading: \"json:{0}\"!", loc);
            }
            return FileVisitResult.CONTINUE;
        }

        // Get Mod Name
        final String modid = dirname.substring(4);

        if (AgriCore.getValidator().isValidMod(modid)) {
            AgriCore.getCoreLogger().debug("Loading: \"json:{0}\" for mod: \"{1}\"!", loc, modid);
            return FileVisitResult.CONTINUE;
        } else {
            AgriCore.getCoreLogger().debug("Skipping: \"json:{0}\" for missing mod: \"{1}\"!", loc, modid);
            return FileVisitResult.SKIP_SUBTREE;
        }
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        // Filter Null
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);

        // Boolean Marker to Detect Skipped JSON Files.
        boolean wasAccepted = false;

        // Load Registries
        for (AgriLoadableRegistry<? extends AgriSerializable> r : registries) {
            if (r.acceptsElement(file.getFileName().toString())) {
                AgriLoader.loadElement(root, file, r);
                wasAccepted = true;
            }
        }

        // If the file was not accepted, give the user a warning.
        if (!wasAccepted && file.getFileName().toString().toLowerCase().endsWith(".json")) {
            AgriCore.getCoreLogger().info(
                    "Found a JSON file that was not accepted by any loader! Perhaps it has the wrong name?\n{0}",
                    file
            );
        }

        return FileVisitResult.CONTINUE;
    }

}
