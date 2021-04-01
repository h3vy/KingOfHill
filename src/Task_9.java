public class Task_9 {
    public static void main(String[] args) {
        int quantity = 5;
        int sum = 1;
        int a = 0;
        int b = 0;

        for (int i = 0; i < quantity; i++ ){
            a = b;
            b = sum;
            sum = a + b;
            System.out.println(a + " ");
        }
    }
}
