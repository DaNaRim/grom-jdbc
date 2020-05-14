package jdbc.lesson3.homework1;

public class Demo {
    public static void main(String[] args) throws Exception {
        Solution solution = new Solution();


        solution.findProductsByPrice(300, 100);

        solution.findProductsByName("test");

        solution.findProductsWithEmptyDescription();
    }
}