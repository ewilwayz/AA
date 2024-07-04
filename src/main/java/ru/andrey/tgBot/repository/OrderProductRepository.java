package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.andrey.tgBot.entity.OrderProduct;

@RepositoryRestResource(collectionResourceRel =
        "categories", path = "categories")
public interface OrderProductRepository extends
        JpaRepository<OrderProduct, Long>
{ }
