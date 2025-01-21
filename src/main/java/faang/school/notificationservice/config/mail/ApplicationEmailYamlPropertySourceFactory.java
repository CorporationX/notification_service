package faang.school.notificationservice.config.mail;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

public class ApplicationEmailYamlPropertySourceFactory implements PropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        Resource res = resource.getResource();

        yaml.setResources(res);
        Properties properties = yaml.getObject();

        Map<String, Object> map = new HashMap<>();
        assert properties != null;
        for (String key : properties.stringPropertyNames()) {
            map.put(key, properties.get(key));
        }

        return new MapPropertySource(name != null ? name : Objects.requireNonNull(res.getFilename()), map);
    }
}
