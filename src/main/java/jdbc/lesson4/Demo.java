package jdbc.lesson4;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        Product product1 = new Product(4, "!!!", "!!!!", 7777);
        Product product2 = new Product(5, "!!!", "!!!!", 7777);
        Product product3 = new Product(5, "!!!", "!!!!", 7777);

        List<Product> products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);

        TransactionDemo.save(products);
    }
}