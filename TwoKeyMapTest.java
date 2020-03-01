import org.junit.Test;

import static org.junit.Assert.*;

public class TwoKeyMapTest {

    @Test
    public void put() {
        TwoKeyMap<Integer, Integer, Integer> map = new TwoKeyMap<>();
        map.put(1,1,1);
        assertEquals("1 , 1 : 1\n",map.toString());
        map.put(1,2,3);
        assertEquals("1 , 1 : 1\n1 , 2 : 3\n",map.toString());
    }

    @Test
    public void get() {
        TwoKeyMap<Integer, Integer, Integer> map = new TwoKeyMap<>();
        map.put(1,1,1);
        map.put(1,2,3);
        assertEquals(Integer.valueOf(1),map.get(1,1));
        assertEquals(Integer.valueOf(3),map.get(1,2));
    }

    @Test
    public void containsKey() {
        TwoKeyMap<Integer, Integer, Integer> map = new TwoKeyMap<>();
        map.put(1,1,1);
        map.put(1,2,3);
        assert(map.containsKey(1,1));
        assert(map.containsKey(1,2));
    }
}