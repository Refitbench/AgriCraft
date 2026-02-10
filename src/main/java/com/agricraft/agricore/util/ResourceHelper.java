/*
 */
package com.agricraft.agricore.util;

import com.agricraft.agricore.core.AgriCore;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * CHANGED IN FORK: No longer packs org.reflections library that incurs a huge memory usage
 * @author RlonRyan, Rongmario
 */
public class ResourceHelper {
    public static Set<String> findResources(Predicate<String> nameFilter) {
        StackTraceElement callerElement = Thread.currentThread().getStackTrace()[2];
        String callerClass = callerElement.getClassName();
        String callerMethod = callerElement.getMethodName();
        throw new RuntimeException("[Caller: " + callerClass + "::" + callerMethod + "] - new AgriCraft does not support any type of findResources.");
    }

    public static Set<String> findJsonResources() {
        Set<File> visited = new HashSet<>();
        Set<String> resources = new LinkedHashSet<>();
        String[] exts = new String[] { "jar", "zip" };

        for (URL url : Launch.classLoader.getSources()) {
            File file = toFile(url);
            if (file == null || !visited.add(file)) {
                continue;
            }
            String path = file.getAbsolutePath().replace('\\', '/');
            if (path.contains("/mods/") || (FMLLaunchHandler.isDeobfuscatedEnvironment())) {
                if (file.isDirectory()) {
                    scanDirectory(file.toPath(), resources);
                } else if (FilenameUtils.isExtension(file.getName(), exts)) {
                    scanZip(file, resources);
                }
            }
        }

        return resources;
    }

    /**
     * Copies a file from inside the jar to the specified location outside the
     * jar, retaining the file name. The default copy action is to not overwrite
     * an existing file.
     *
     * @param from the location of the internal resource.
     * @param to the location to copy the resource to.
     * @param overwrite if the copy task should overwrite existing files.
     */
    public static void copyResource(String from, Path to, boolean overwrite) {
        try {
            if (overwrite || !Files.exists(to)) {
                Files.createDirectories(to.getParent());
                Files.copy(getResourceAsStream(from), to, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            AgriCore.getLogger("AgriCraft").error(
                    "Unable to copy Jar resource: \"{0}\" to: \"{1}\"!",
                    from,
                    to
            );
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the requested resource by using the current thread's class
     * loader or the AgriCore class loader.
     *
     * @param location the location of the desired resource stream.
     * @return the resource, as a stream.
     */
    public static InputStream getResourceAsStream(String location) {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(location);
        return in != null ? in : ResourceHelper.class.getResourceAsStream(location);
    }

    private static File toFile(URL url) {
        try {
            URI uri = url.toURI();
            String s = uri.toString();
            if (s.startsWith("jar:")) {
                int bang = s.indexOf("!/");
                if (bang != -1) {
                    s = s.substring(4, bang);
                }
                uri = URI.create(s);
            }
            if ("file".equalsIgnoreCase(uri.getScheme())) {
                return new File(uri);
            }
        } catch (URISyntaxException ignored) { }
        return null;
    }

    private static void scanZip(File jarFile, Set<String> out) {
        try (ZipFile zip = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> e = zip.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                String name = entry.getName();
                if (name.endsWith(".json") && name.startsWith("json/defaults/")) {
                    out.add(name);
                }
            }
        } catch (IOException ignored) { }
    }

    private static void scanDirectory(Path root, Set<String> out) {
        try (Stream<Path> stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().endsWith(".json"))
                    .filter(p -> p.startsWith("json/defaults/"))
                    .map(p -> root.relativize(p).toString().replace('\\', '/'))
                    .forEach(out::add);
        } catch (IOException ignored) { }
    }

}
