public class Task_3 {
    public static void main(String[] args) {
        int i = 3;
        int j = 5;
        int k = 0;
        System.out.println("До:" + "i = " + i + ", j = " + j);
        k = j;
        j = i;
        i = k;
        System.out.println("После:" + "i = " + i + ", j = " + j);
    }
}
