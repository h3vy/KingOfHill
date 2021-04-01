public class Task_8 {
    public static void main(String[] args) {
        String input = "2002";
        String copy = reverseString(input);
        if (input.equals(copy)){
            System.out.println("Да");
        }
        else {
            System.out.println("Нет");
        }

    }

    public static String reverseString(String str){
        return new StringBuilder(str).reverse().toString();
    }
}
