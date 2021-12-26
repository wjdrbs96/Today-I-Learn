package ExampleCode;

import java.util.HashMap;
import java.util.Map;

public class DateFormatEx1 {
    public static void main(String[] args) {
        TestMap<DTO> testMap = new TestMap<>();
        TestMap<DTO> add = testMap.add("lee", new DTO("Hi", "Name"));
    }
}

class DTO {
    private String id;
    private String name;

    public DTO(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

class TestMap<T> {
    private Map<String, T> hm = new HashMap<>();

    public TestMap<T> add(String key, T value) {
        hm.put(key, value);
        return this;
    }
}
