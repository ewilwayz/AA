package ru.andrey.tgBot.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.andrey.tgBot.entity.ClientOrder;
import ru.andrey.tgBot.entity.OrderProduct;
import ru.andrey.tgBot.entity.Product;

import java.util.List;
import java.util.Set;

@RepositoryRestResource(
        collectionResourceRel = "orderProducts",
        path = "orderProducts"
)
public interface OrderProductRepository extends
        JpaRepository<OrderProduct, Long> {
    @Query("select op.product " +
            "from OrderProduct op " +
            "where op.clientOrder.client.id = :clientId")
    Set<Product> findProductsByClientId(@Param("clientId") Long clientId);

    @Query("select op.product " +
            "from OrderProduct op " +
            "group by op.product " +
            "order by sum(op.countProduct) desc")
    List<Product> findMostPopularProducts(PageRequest pageable);

    List<OrderProduct> findByClientOrder(ClientOrder clientOrder);

    OrderProduct findProductByClientOrderId(Long clientOrderId, Long productId);
}
