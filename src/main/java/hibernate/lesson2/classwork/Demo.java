package hibernate.lesson2.classwork;

import java.util.Arrays;
import java.util.List;

public class Demo {
    public static void main(String[] args) {
        Product product = new Product();
        product.setName("table new2");
        product.setDescription("grey & blue");
        product.setPrice(70);

        Product product1 = new Product();
        product1.setName("table new111");
        product1.setDescription("grey & blue");
        product1.setPrice(70);

        Product product2 = new Product();
        product2.setName("table new222");
        product2.setDescription("grey & blue");
        product2.setPrice(70);

        Product product3 = new Product();
        product3.setName("table new333");
        product3.setDescription("grey & blue");
        product3.setPrice(70);

        List<Product> products = Arrays.asList(product1, product2, product3);

        ProductDAO.save(product);
        product.setName("SuperTable");
        ProductDAO.update(product);
        ProductDAO.delete(product);

        ProductDAO.saveProducts(products);
        int i = 0;
        for (Product product4 : products) {
            product4.setName(Integer.toString(i++));
        }
        ProductDAO.updateProducts(products);
        ProductDAO.deleteProducts(products);
    }
}