package apiTest;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class General {

    @Test
    public void testSet() {
        Set<Integer> numbers = new HashSet<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(10); // ignored - no duplicates

        System.out.println(numbers); // [20, 10] (order not guaranteed)
        Assert.assertTrue(numbers.contains(10));
        Assert.assertEquals(numbers.size(), 2);
    }

    @Test
    public void testArrayList() {
        List<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");

        System.out.println(fruits.get(1)); // Banana
        System.out.println("Size: " + fruits.size());

        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        Assert.assertEquals(fruits.get(1), "Banana");
        Assert.assertEquals(fruits.size(), 3);
    }

    @Test
    public void testStreamsAPI() {
        List<String> names = Arrays.asList("Rony", "Alice", "Bob", "Charlie");

        names.stream()
                .filter(name -> name.length() > 3) // filter
                .sorted()                          // sort
                .map(String::toUpperCase)          // convert to uppercase
                .forEach(System.out::println);     // print
    }

    @Test
    public void testCollectionsSort() {
        List<Integer> numbers = Arrays.asList(5, 1, 7, 3, 2);

        Collections.sort(numbers); // natural order
        System.out.println(numbers); // [1, 2, 3, 5, 7]
        Assert.assertEquals(numbers, Arrays.asList(1, 2, 3, 5, 7));

        Collections.reverse(numbers); // reverse order
        System.out.println(numbers); // [7, 5, 3, 2, 1]
        Assert.assertEquals(numbers, Arrays.asList(7, 5, 3, 2, 1));
    }

    // LIFO – last in, first out
    @Test
    public void testStack() {
        Stack<Integer> stack = new Stack<>();
        stack.push(10);
        stack.push(20);
        stack.push(30);

        System.out.println(stack.pop());   // 30
        System.out.println(stack.peek());  // 20

        Assert.assertEquals(stack.peek(), 20);
        Assert.assertEquals(stack.size(), 2);
    }

    // FIFO – first in, first out
    @Test
    public void testQueue() {
        Queue<String> queue = new LinkedList<>();
        queue.add("A");
        queue.add("B");
        queue.add("C");

        System.out.println(queue.poll());  // A
        System.out.println(queue.peek());  // B

        Assert.assertEquals(queue.poll(), "A");
        Assert.assertEquals(queue.peek(), "B");
        Assert.assertEquals(queue.size(), 2);
    }

    // Add/remove by index
    @Test
    public void testLinkedList() {
        LinkedList<String> list = new LinkedList<>();
        list.add("One");
        list.add("Two");
        list.addFirst("Zero");
        list.addLast("Three");

        System.out.println(list); // [Zero, One, Two, Three]
        Assert.assertEquals(list.getFirst(), "Zero");
        Assert.assertEquals(list.getLast(), "Three");
    }

    @Test
    public void testHashMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Rony", 30);
        map.put("Alice", 25);

        System.out.println(map.get("Rony")); // 30
        for (String key : map.keySet()) {
            System.out.println(key + " -> " + map.get(key));
        }

        Assert.assertEquals(map.get("Rony"), (Integer) 30);
        Assert.assertTrue(map.containsKey("Alice"));
    }
}