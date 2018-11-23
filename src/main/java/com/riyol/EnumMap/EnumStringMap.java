package com.riyol.EnumMap;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

public class EnumStringMap<T extends Enum<T> & EnumString> {
    @NonNull
    private final Map<String,T> map;

    public EnumStringMap(@NonNull Class<T> enumeration) {
        map = codeToEnumMap(enumeration);
    }

    @NonNull
    public T get(String code) {
        T status = map.get(code);
        if (status == null) {
//            throw new IllegalArgumentException("code=" + code);
        }
        return status;
    }

    @NonNull
    private Map<String,T> codeToEnumMap(@NonNull Class<T> enumeration) {
        Map<String,T> ret = new HashMap<>();
        for (T value : enumeration.getEnumConstants()) {
            ret.put(value.code(), value);
        }
        return ret;
    }

    public int size() {
        return map.size();
    }
}
