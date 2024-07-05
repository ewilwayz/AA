package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.*;
import ru.andrey.tgBot.entity.ClientOrder;

@RepositoryRestResource(
        collectionResourceRel = "clientOrders",
        path = "clientOrders"
)
public interface ClientOrderRepository extends
        JpaRepository<ClientOrder, Long>
{ }