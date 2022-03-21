package lesson3;

public class Demo {

    public static void main(String[] args) {
        ProductDAO productDAO = new ProductDAO();

        Product product = new Product(10, "test", "test description", 99);
        productDAO.save(product);

        System.out.println(productDAO.getProducts());
    }
}
