package com.teach.javafxclient.controller.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.PropertyPreFilter;
import javafx.beans.property.SimpleBooleanProperty;

public class SimpleBooleanPropertyFilter implements PropertyPreFilter {
    @Override
    public boolean apply(JSONSerializer serializer, Object object, String name) {
        // 忽略 SimpleBooleanProperty 类型的字段
        return !(object instanceof SimpleBooleanProperty);
    }
}