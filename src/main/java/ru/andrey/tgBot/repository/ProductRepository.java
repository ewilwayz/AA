package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.*;
import ru.andrey.tgBot.entity.Category;
import ru.andrey.tgBot.entity.Product;

@RepositoryRestResource(
        collectionResourceRel = "products",
        path = "products"
)
public interface ProductRepository extends
        JpaRepository<Product, Long>
{
    Product findByNameAndCategory(String productName, Category subcategory);
}
