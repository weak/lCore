package com.qrakn.honcho.command.adapter.impl;

import com.qrakn.honcho.command.adapter.CommandTypeAdapter;

import java.util.HashMap;
import java.util.Map;

public class BooleanTypeAdapter implements CommandTypeAdapter {
    private static final Map<String, Boolean> MAP;

    static {
        (MAP = new HashMap<>()).put("true", true);
        BooleanTypeAdapter.MAP.put("yes", true);
        BooleanTypeAdapter.MAP.put("false", false);
        BooleanTypeAdapter.MAP.put("no", false);
    }

    @Override
    public <T> T convert(final String string, final Class<T> type) {
        return type.cast(BooleanTypeAdapter.MAP.get(string.toLowerCase()));
    }
}
