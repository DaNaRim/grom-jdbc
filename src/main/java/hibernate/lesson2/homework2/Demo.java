package hibernate.lesson2.homework2;

public class Demo {
    public static void main(String[] args) throws Exception {
        System.out.println(ProductDAO.findById(1).toString());
        System.out.println(ProductDAO.findByName("AA").toString());
        System.out.println(ProductDAO.findByContainedName("A").toString());
        System.out.println(ProductDAO.findByPrice(450, 50).toString());
        System.out.println(ProductDAO.findByNameSortedAsc("A").toString());
        System.out.println(ProductDAO.findByNameSortedDesc("A").toString());
        System.out.println(ProductDAO.findByPriceSortedDesc(450, 100).toString());
    }
}