public class Task_16 {
    public static void main(String[] args) {
        int[] arr = {1, 4, 5, 6, 12, 13, 108};
        int max = 0;
        int predmax = 0;

        for (int i = 0; i < arr.length; i++){
            if (arr[i] > max){
                predmax = max;
                max = arr[i];
            }
        }
        System.out.println(predmax);
    }
}
