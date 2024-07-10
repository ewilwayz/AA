package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.andrey.tgBot.entity.Product;

import java.util.List;

@RepositoryRestResource(
        collectionResourceRel = "products",
        path = "products"
)
public interface ProductRepository extends
        JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);
}