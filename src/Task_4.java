public class Task_4 {
    public static void main(String[] args) {
        int i = 3;
        int j = 5;
        System.out.println("До:" + "i = " + i + ", j = " + j);
        i = i + j;
        j = i - j;
        i = i - j;
        System.out.println("После:" + "i = " + i + ", j = " + j);
    }
}
