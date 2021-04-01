import java.util.*;

public class Task_7 {
    public static void main(String[] args) {
        int number = 17;
        boolean isSimple = true;

        for(int i = 2; i <= number/2; i++){
            if (number % i == 0){
                isSimple = false;
                break;
            }
        }

        if (isSimple){
            System.out.println(number);
        }
    }
}
