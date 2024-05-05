package com.github.easyhttp.common.serializer;

import com.github.easyhttp.common.serializer.interfaces.SerializerService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 序列化统一接口
 *
 * @author wangxiaojiang
 * @version v1.0.0
 * @create 2024/4/25 23:40
 */
public class SerializerManager {

    private final List<SerializerService> serializerServices;

    public SerializerManager() {
        serializerServices = new ArrayList<>();
        ServiceLoader<SerializerService> loader = ServiceLoader.load(SerializerService.class);
        for (SerializerService serializerService : loader) {
            serializerServices.add(serializerService);
        }
        serializerServices.sort(Comparator.comparingInt(SerializerService::getPriority).reversed());
    }

    public SerializerService getSerializer() {
        if (serializerServices.isEmpty()) {
            throw new RuntimeException("No serializer found");
        }
        // 返回优先级最高的序列化器
        return serializerServices.get(0);
    }

}
