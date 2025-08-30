import java.util.HashMap;
import java.util.Map;

interface MyMap {
  void set(String key, Integer value);

  Integer get(String key);

  Integer setAll(Integer value);
}

public class Tst1 implements MyMap {
  private final Map<String, Integer> map = new HashMap<>();

  @Override
  public void set(String key, Integer value) {
    map.put(key, value);
  }

  @Override
  public Integer get(String key) {
    return map.get(key);
    //        return 0;
  }

  @Override
  public Integer setAll(Integer value) {
    map.replaceAll((k, v) -> value);
    return value;
  }

  public static void main(String[] args) {
    MyMap myMap = new Tst1();
    myMap.set("a", 2);
    myMap.set("b", 3);
    System.out.println(myMap.get("a"));
    System.out.println(myMap.get("b"));
    myMap.setAll(4);
    System.out.println(myMap.get("a"));
    System.out.println(myMap.get("b"));
  }
}
