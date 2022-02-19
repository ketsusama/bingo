package be.ketsu.bingo.configuration;

import be.ketsu.bingo.configuration.annotation.Configuration;
import be.ketsu.bingo.utils.Serializations;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.bukkit.plugin.Plugin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;


@Slf4j
@UtilityClass
public class Configurations {

    /**
     * Create the config with the structure specified in the specified plugin folder.
     *
     * @param plugin    Plugin
     * @param structure Structure
     */
    @SneakyThrows
    public <T> T readOrCreateConfiguration(Plugin plugin, Class<T> structure) {
        if (!structure.isAnnotationPresent(Configuration.class))
            throw new RuntimeException("Tried to load a non-annotated configuration");

        var configName = structure.getAnnotation(Configuration.class).value();
        if (configName.isEmpty())
            configName = structure.getSimpleName();
        // On veut du YML
        if (!configName.endsWith(".yml"))
            configName = configName + ".yml";

        val pluginPath = Paths.get("plugins", plugin.getName(), configName);
        if (!Files.exists(pluginPath)) {
            Files.createDirectories(pluginPath.getParent());
            Files.createFile(pluginPath);

            val template = getResourceFileAsString(plugin.getClass(), "/" + configName);
            if (template != null) {
                Files.writeString(pluginPath, template);
            } else {
                Files.writeString(
                    pluginPath,
                    Serializations.serialize(structure.newInstance())
                );
            }
        }

        return Serializations.deserialize(
            Files.readString(pluginPath),
            structure
        );
    }

    /**
     * Writes the configuration with the values contained in the specified instance.
     *
     * @param plugin        Plugin
     * @param configuration Configuration
     * @param <T>           Configuration Type
     */
    @SneakyThrows
    public <T> void writeConfiguration(Plugin plugin, T configuration) {
        val structure = configuration.getClass();
        if (!structure.isAnnotationPresent(Configuration.class))
            throw new RuntimeException("Tried to load a non-annotated configuration");

        var configName = structure.getAnnotation(Configuration.class).value();
        if (configName.isEmpty())
            configName = structure.getSimpleName();
        // On veut du YML
        if (!configName.endsWith(".yml"))
            configName = configName + ".yml";

        val pluginPath = Paths.get("plugins", plugin.getName(), configName);
        Files.writeString(
            pluginPath,
            Serializations.serialize(configuration)
        );
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName path to the resource file
     * @return the file's contents
     */
    @SneakyThrows
    private String getResourceFileAsString(Class<?> classLoader, String fileName) {
        @Cleanup InputStream is = classLoader.getResourceAsStream(fileName);
        if (is == null) return null;
        @Cleanup InputStreamReader isr = new InputStreamReader(is);
        @Cleanup BufferedReader reader = new BufferedReader(isr);
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

}
