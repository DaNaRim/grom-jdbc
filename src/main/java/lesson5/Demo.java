package lesson5;

import org.hibernate.Session;

public class Demo {
    public static void main(String[] args) throws Exception {
        Product product = new Product();
        product.setId(98);
        product.setName("table");
        product.setDescription("grey & blue");
        product.setPrice(80);

//        ProductRepository.save(product);
//        ProductRepository.delete(98);
//        ProductRepository.update(product);

//        save();
    }

    public static void save() {
        Session session = new HibernateUtils().createSessionFactory().openSession();

        session.getTransaction().begin();

        Product product = new Product();
        product.setId(99);
        product.setName("table");
        product.setDescription("grey & blue");
        product.setPrice(70);

        session.save(product);

        session.getTransaction().commit();

        System.out.println("Done");

        session.close();
    }
}
