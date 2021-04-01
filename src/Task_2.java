import java.util.ArrayList;

public class Task_2 {
    public static void main(String[] args) {
        char[] reverse;
        String string = "Hello";
        reverse = string.toCharArray();
        for(int i = reverse.length - 1; i >= 0; i--){
            System.out.println(reverse[i]);
        }
    }
}
