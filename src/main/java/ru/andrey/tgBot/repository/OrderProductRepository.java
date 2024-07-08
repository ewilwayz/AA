package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.*;
import ru.andrey.tgBot.entity.OrderProduct;

@RepositoryRestResource(
        collectionResourceRel = "orderProducts",
        path = "orderProducts"
)
public interface OrderProductRepository extends
        JpaRepository<OrderProduct, Long>
{ }
