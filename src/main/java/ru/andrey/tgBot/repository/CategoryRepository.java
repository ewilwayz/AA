package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.*;
import ru.andrey.tgBot.entity.Category;

@RepositoryRestResource(collectionResourceRel =
        "categories", path = "categories")
public interface CategoryRepository extends
        JpaRepository<Category, Long>
{ }
