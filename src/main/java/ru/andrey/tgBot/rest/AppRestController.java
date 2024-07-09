package ru.andrey.tgBot.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.andrey.tgBot.entity.ClientOrder;
import ru.andrey.tgBot.entity.Product;
import ru.andrey.tgBot.service.AppService;

import java.util.List;
import java.util.Set;

@RestController
public class AppRestController {
    private final AppService appService;

    public AppRestController(AppService appService) {
        this.appService = appService;
    }

    /**
     * Получить список всех товаров определенной категории
     */
    @GetMapping("/rest/products/search")
    public List<Product> getProductByCategory(@RequestParam("categoryId") Long categoryId) {
        return appService.getProductByCategoryId(categoryId);
    }

    /**
     * Получить список всех заказов клиента
     */
    @GetMapping("/rest/clients/{id}/orders")
    public List<ClientOrder> getClientByOrders(@PathVariable Long id) {
        return appService.getClientByOrders(id);
    }

    /**
     * Получить список всех товаров во всех заказах клиента по идентификатору клиента
     */
    @GetMapping("/rest/clients/{clientId}/products")
    public Set<Product> getClientProducts(@PathVariable Long clientId) {
        return appService.getClientProducts(clientId);
    }

    /**
     * Получить {limit} самых популярных товаров среди клиентов
     */
    @GetMapping("/rest/products/popular")
    public List<Product> getTheMostPopularProducts(@RequestParam("limit") Integer limit) {
        return appService.getTheMostPopularProducts(limit);
    }
}
