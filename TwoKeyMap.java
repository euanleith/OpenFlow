import java.util.HashMap;
import java.util.Map;

class TwoKeyMap<Key1, Key2, Value> {

    private HashMap<Key1, HashMap<Key2, Value>> map;

    TwoKeyMap() {
        map = new HashMap<>();
    }

    public void put(Key1 key1, Key2 key2, Value value) {
        if (!map.containsKey(key1)) {
            map.put(key1, new HashMap<>());
        }
        map.get(key1).put(key2, value);
    }

    public Value get(Key1 key1, Key2 key2) {
        return map.get(key1).get(key2);
    }

    public boolean containsKey(Key1 key1, Key2 key2) {
        return map.containsKey(key1) && map.get(key1).containsKey(key2);
    }

    @Override
    public String toString() {
        String out = "";
        for (Map.Entry<Key1, HashMap<Key2, Value>> m : map.entrySet()) {
            for (Map.Entry<Key2, Value> n : m.getValue().entrySet()) {
                out += m.getKey() + " , " + n.getKey() + " : " + n.getValue() + "\n";
            }
        }
        return out;
    }
}