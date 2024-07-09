package ru.andrey.tgBot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.andrey.tgBot.entity.ClientOrder;
import ru.andrey.tgBot.entity.Product;
import ru.andrey.tgBot.repository.ClientOrderRepository;
import ru.andrey.tgBot.repository.OrderProductRepository;
import ru.andrey.tgBot.repository.ProductRepository;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AppService {
    private final ProductRepository productRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final OrderProductRepository orderProductRepository;

    public AppService(ProductRepository productRepository, ClientOrderRepository clientOrderRepository, OrderProductRepository orderProductRepository) {
        this.productRepository = productRepository;
        this.clientOrderRepository = clientOrderRepository;
        this.orderProductRepository = orderProductRepository;
    }

    /**
     * Получить список всех товаров определенной категории
     */
    public List<Product> getProductByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * Получить список всех заказов клиента
     */
    public List<ClientOrder> getClientByOrders(Long id) {
        return clientOrderRepository.findByClientId(id);
    }

    /**
     * Получить список всех товаров во всех заказах клиента по идентификатору клиента
     */
    public Set<Product> getClientProducts(Long clientId) {
        return orderProductRepository.findProductsByClientId(clientId);
    }

    /**
     * Получить {limit} самых популярных товаров среди клиентов
     */
    public List<Product> getTheMostPopularProducts(Integer limit) {
        PageRequest pageable = PageRequest.of(0, limit);
        return orderProductRepository.findMostPopularProducts(pageable);
    }
}

