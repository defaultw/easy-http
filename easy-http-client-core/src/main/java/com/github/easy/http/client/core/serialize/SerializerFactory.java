package com.github.easy.http.client.core.serialize;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Description
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/22 22:56
 */
public class SerializerFactory {
    
    private static final Map<String, SerializerService> SERIALIZERS = new HashMap<>();

    static {
        ServiceLoader<SerializerService> loader = ServiceLoader.load(SerializerService.class);
        for (SerializerService serializer : loader) {
            SERIALIZERS.put(serializer.getClass().getSimpleName(), serializer);
        }
    }

    public static SerializerService getSerializer(String name) {
        return SERIALIZERS.get(name);
    }

}
