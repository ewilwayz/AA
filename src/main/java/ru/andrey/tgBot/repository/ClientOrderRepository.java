package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.andrey.tgBot.entity.ClientOrder;

@RepositoryRestResource(collectionResourceRel =
        "clientorders", path = "clientorders")
public interface ClientOrderRepository extends
        JpaRepository<ClientOrder, Long>
{ }