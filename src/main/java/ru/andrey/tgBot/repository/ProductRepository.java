package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.andrey.tgBot.entity.Category;
import ru.andrey.tgBot.entity.Product;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@RepositoryRestResource(
        collectionResourceRel = "products",
        path = "products"
)
public interface ProductRepository extends
        JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    Product findProductById(int productId);

    List<Product> findProductsByCategoryName(String userText);

    @Query("select p " +
            "from Product p " +
            "where LOWER(p.name) like concat('%', LOWER(:searchNameString), '%')")
    List<Product> search(String searchNameString);

}