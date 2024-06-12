package io.github.vuhoangha.netty;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class URI {

    @Getter
    private String path;

    private final Map<String, String> queries = new HashMap<>();


    public String getQuery(String key) {
        return queries.getOrDefault(key, null);
    }


    public void clear() {
        path = null;
        queries.clear();
    }


}
