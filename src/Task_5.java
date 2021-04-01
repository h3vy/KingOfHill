import java.util.HashMap;
import java.util.Map;

public class Task_5 {
    public static void main(String[] args) {
        String[] words;
        HashMap<String,Integer> map = new HashMap<>();
        String input = "Hello world world hello hello";

        words = input.split(" ");
        for(int i = 0; i < words.length ; i++){
            if (map.containsKey(words[i])){
                int count = map.get(words[i]);
                count++;
                map.put(words[i], count);
            }
            else {
                map.put(words[i], 1);
            }
        }

        for(Map.Entry<String,Integer> entry: map.entrySet()){
            Integer count = entry.getValue();
            String name = entry.getKey();
            System.out.println(name + " " + count);
        }
    }
}
