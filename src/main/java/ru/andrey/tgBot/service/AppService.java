package ru.andrey.tgBot.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.andrey.tgBot.entity.Category;
import ru.andrey.tgBot.entity.Client;
import ru.andrey.tgBot.entity.ClientOrder;
import ru.andrey.tgBot.entity.Product;
import ru.andrey.tgBot.repository.*;

import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AppService {
    private final ProductRepository productRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;

    public AppService(ProductRepository productRepository, ClientOrderRepository clientOrderRepository, OrderProductRepository orderProductRepository, ClientRepository clientRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.clientOrderRepository = clientOrderRepository;
        this.orderProductRepository = orderProductRepository;
        this.clientRepository = clientRepository;
        this.categoryRepository = categoryRepository;
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

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow();
    }
    public List<Product> getCategoryProducts(String userText) {
        return productRepository.findProductsByCategoryName(userText);
    }

    public Client getClientByExternalId(Long id){
        return clientRepository.findByExternalId(id);
    }

    public List<Category> getCategoryByParent(Long parentCategoryId){
        return categoryRepository.findByParentId(parentCategoryId);
    }

    public Long getCategoryByName(String name){
        return categoryRepository.findFirstIdByName(name);
    }

    public List<Product> search(String searchNameString, Long categoryId){
        if(categoryId != null)
            return productRepository.findByCategoryId(categoryId);
        else
            return productRepository.search(searchNameString);
    }
    public void saveClient(Client client){
        clientRepository.save(client);
    }
}


