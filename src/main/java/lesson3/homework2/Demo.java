package lesson3.homework2;

public class Demo {
    public static void main(String[] args) {
        Solution solution = new Solution();

        System.out.println(solution.testSavePerformance()); //141684
        System.out.println(solution.testDeleteByIdPerformance()); //138189
        solution.testSavePerformance();
        System.out.println(solution.testDeletePerformance()); //1437
        solution.testSavePerformance();
        System.out.println(solution.testSelectByIdPerformance()); //135622
        System.out.println(solution.testSelectPerformance()); //1484
    }
}