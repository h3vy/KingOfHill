import java.util.ArrayList;

public class Task_15 {
    public static void main(String[] args) {
        String test = "HOMEBOYSCLUB";
        String[] charArr = test.split("");
        ArrayList<String> echo = new ArrayList<>();

        for(int i = 0; i < charArr.length; i++){
            for(int j = i + 1; j < charArr.length; j++){
                if (charArr[i].equals(charArr[j])){
                    echo.add(charArr[i]);
                }
            }
        }

        System.out.println(echo);
    }
}
