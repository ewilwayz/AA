package ru.andrey.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.andrey.tgBot.entity.Category;

import java.util.List;

@RepositoryRestResource(
        collectionResourceRel = "categories",
        path = "categories"
)
public interface CategoryRepository extends
        JpaRepository<Category, Long>
{
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL")
    List<Category> findCategoriesByParentIdIsNull();

    List<Category> findAll();

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId")
    List<Category> findCategoriesByParentId(@Param("parentId") Long parentId);
}