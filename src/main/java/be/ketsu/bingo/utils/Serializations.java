package be.ketsu.bingo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.val;

@UtilityClass
public class Serializations {

    private final String YAML_WEIRDO_CONSTANT = "---\n";

    private final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
        .registerModule(new ParameterNamesModule())
        .registerModule(new JavaTimeModule());

    /**
     * @param data The object that will be serialized
     * @return A serial version of the specified object
     */
    @SneakyThrows
    public String serialize(Object data) {
        val serialized = mapper.writeValueAsString(data);
        if (serialized.startsWith(YAML_WEIRDO_CONSTANT))
            return serialized.substring(YAML_WEIRDO_CONSTANT.length(), serialized.length() - 1);

        return serialized;
    }

    /**
     * @param serialized The serialize object in its text form
     * @param type       The type of object returned
     * @param <T>        the type
     * @return A new deserialized instance
     */
    @SneakyThrows
    public <T> T deserialize(String serialized, Class<T> type) {
        return mapper.readValue(serialized, type);
    }

}
