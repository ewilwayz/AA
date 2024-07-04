package ru.andrey.tgBot;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.andrey.tgBot.entity.Category;
import ru.andrey.tgBot.entity.Client;
import ru.andrey.tgBot.entity.Product;
import ru.andrey.tgBot.repository.CategoryRepository;
import ru.andrey.tgBot.repository.ClientRepository;
import ru.andrey.tgBot.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class Dop_task{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createCategoriesAndProducts2() {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try (InputStream in = FillingTests.class.getResourceAsStream("/products.yaml")) {
            Map<String, List<Map<String, Object>>> data = objectMapper.readValue(in, new TypeReference<>() {});

            /*
             *   Обработка клиентов
             */
            List<Map<String, Object>> clients = data.get("clients");
            for (Map<String, Object> clientData : clients) {
                Client client = new Client();
                client.setExternalId(Long.parseLong(clientData.get("externalId").toString()));
                client.setFullName((String) clientData.get("fullName"));
                client.setPhoneNumber((String) clientData.get("phone"));
                client.setAddress((String) clientData.get("address"));
                clientRepository.save(client);
            }

            /*
             *   Обработка категорий и продуктов
             */
            List<Map<String, Object>> categories = data.get("categories");
            for (Map<String, Object> categoryData : categories){
                Category category = new Category();
                category.setName((String) categoryData.get("name"));
                categoryRepository.save(category);

                /*
                 *   Обработка продуктов в категории
                 */
                List<Map<String, Object>> products = (List<Map<String, Object>>) categoryData.get("products");
                for (Map<String, Object> productsData : products){
                    Product product = new Product();
                    product.setName((String) productsData.get("name"));
                    product.setDescription((String) productsData.get("description"));
                    product.setPrice(new BigDecimal(productsData.get("price").toString()));
                    product.setCategory(category);
                    productRepository.save(product);
                }

                /*
                 *   Обработка подкатегорий в категории
                 */
                List<Map<String, Object>> subcategories = (List<Map<String, Object>>) categoryData.get("subcategories");
                for (Map<String, Object> subcategoryData : subcategories) {
                    Category subcategory = new Category();
                    subcategory.setName((String) subcategoryData.get("name"));
                    subcategory.setParent(category);
                    categoryRepository.save(subcategory);

                    List<Map<String, Object>> subcategoryProducts = (List<Map<String, Object>>) subcategoryData.get("products");
                    for (Map<String, Object> subcategoryProductData : subcategoryProducts) {
                        Product subcategoryProduct = new Product();
                        subcategoryProduct.setName((String) subcategoryProductData.get("name"));
                        subcategoryProduct.setDescription((String) subcategoryProductData.get("description"));
                        subcategoryProduct.setPrice(new BigDecimal(subcategoryProductData.get("price").toString()));
                        subcategoryProduct.setCategory(subcategory);
                        productRepository.save(subcategoryProduct);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}