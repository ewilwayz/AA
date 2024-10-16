package ru.andrey.tgBot.telegramApi;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.andrey.tgBot.entity.*;
import ru.andrey.tgBot.repository.ClientRepository;
import ru.andrey.tgBot.repository.ClientOrderRepository;
import ru.andrey.tgBot.repository.CategoryRepository;
import ru.andrey.tgBot.repository.ProductRepository;
import ru.andrey.tgBot.repository.OrderProductRepository;
import ru.andrey.tgBot.service.AppService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TelegramBotConnection {
    private final ClientRepository clientRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final CategoryRepository categoryRepository;
    private final OrderProductRepository orderProductRepository;
    private final AppService entitiesService;
    Map<Long, List<Product>> selectedProducts = new HashMap<>();
    private Map<Long, Integer> userStates = new HashMap<>();
    private TelegramBot bot;

    public TelegramBotConnection(AppService entitiesService, ClientRepository clientRepository, ClientOrderRepository clientOrderRepository, CategoryRepository categoryRepository, ProductRepository productRepository, CategoryRepository categoryRepository1, OrderProductRepository orderProductRepository) {
        this.entitiesService = entitiesService;
        this.clientRepository = clientRepository;
        this.clientOrderRepository = clientOrderRepository;
        this.categoryRepository = categoryRepository1;
        this.orderProductRepository = orderProductRepository;
    }

    @Value("${telegram.bot.token}")
    private String botToken;

    @PostConstruct
    public void createConnection() {
        bot = new TelegramBot(botToken);
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::update);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void update(Update update){
        long chatId;
        if(update.callbackQuery() != null){
            chatId = update.callbackQuery().from().id();
            Client client = entitiesService.getClientByExternalId(chatId);

        if(client != null) {
            String[] callback = update.callbackQuery().data().split(":");
            editOrder(client.getId(), callback, chatId);
        }
        else {
            registrationMessage(chatId);
        }

        return;
        }
        chatId = update.message().chat().id();
        //Первое обращение к боту
        if(update.message().text().equals("/start")){
            Client client = entitiesService.getClientByExternalId(chatId);

            if(client != null) {
                bot.execute(new SendMessage(chatId,
                        "Бот готов к работе"));

                sendMainMenu(chatId, null);
            }
            else {
                registrationMessage(chatId);
            }

            return;
        }

        Client client = entitiesService.getClientByExternalId(chatId);

        if (client != null){
            switch (update.message().text()){
                case "Редактировать информацию" -> {
                    bot.execute(new SendMessage(chatId,
                            "Информация:\n" + client.getFullName() + "," + client.getPhoneNumber() + "," + client.getAddress()));
                    clientInformationEditMessage(chatId);
                }
                case "Информация клиента" -> bot.execute(new SendMessage(chatId,
                        "Имя клиента: " + client.getFullName() + "\nНомер телефона: " + client.getPhoneNumber() +
                                "\nАдрес: " + client.getAddress() + "\nВнутренний ID: " + client.getId()));
                case "Оформить заказ" -> {
                    ClientOrder clientOrder = clientOrderRepository
                            .findNewOrderByClientId(client.getId());

                    if(clientOrder.getTotal().doubleValue() > 0) {
                        clientOrder.setStatus(2);

                        clientOrderRepository.save(clientOrder);

                        bot.execute(new SendMessage(chatId,
                                "Заказ успешно оформлен. ID: " + clientOrder.getId()));

                        newClientOrder(client);
                    }
                    else{
                        bot.execute(new SendMessage(chatId,
                                "В заказе пока нет товаров"));
                    }
                }
                case "Просмотреть заказ" -> {
                    ClientOrder clientOrder = clientOrderRepository
                            .findNewOrderByClientId(client.getId());

                    List<OrderProduct> orderProducts = orderProductRepository
                            .findByClientOrder(clientOrder);
                    StringBuilder orderMessage = new StringBuilder();
                    for(OrderProduct orderProduct : orderProducts){
                        int productCount = orderProduct.getCountProduct();
                        orderMessage.append(orderProduct.getProduct().getName()).append(" х")
                                .append(productCount).append(" Общая цена: ")
                                .append(orderProduct.getProduct().getPrice().doubleValue() * productCount)
                                .append("р\n");
                    }

                    bot.execute(new SendMessage(chatId, orderMessage + "\nИтого: " + clientOrder.getTotal() + "р"));
                }
                case "В основное меню" -> {
                    sendMainMenu(chatId, null);
                }
                //Если клиент ввел название категории, пробует регистрировать аккаунт или ввел нестандартную команду
                default -> {
                    Long categoryId = entitiesService.getCategoryByName(update.message().text());
                    if (categoryId != null) {
                        sendMainMenu(chatId, categoryId);

                        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                        for (Product product : entitiesService.search(null, categoryId)) {
                            InlineKeyboardButton button = new
                                    InlineKeyboardButton(product.getName() + ".\nЦена: " + product.getPrice())
                                    .callbackData("add:" + product.getId());

                            markupInline.addRow(button);
                        }

                        bot.execute(new SendMessage(chatId, update.message().text() + ":")
                                .replyMarkup(markupInline));
                    } else
                        registration(update, client);
                }
            }
        }
        else
            registration(update, null);
    }
    //Регистрация нового пользователя или обновление данных аккаунта
    public void registration(Update update, Client client) {
        Long chatId = update.message().chat().id();
        try {
            String[] parts = update.message().text().split(",");
            String fullName = parts[0];
            String phoneNumber = parts[1];
            String address = parts[2];

            Client newClient = client;
            if (newClient == null) {
                newClient = new Client();

                entitiesService.saveClient(newClient);
            } else {
                newClient.setFullName(fullName);
                newClient.setPhoneNumber(phoneNumber);
                newClient.setAddress(address);

                entitiesService.saveClient(newClient);
            }

            newClientOrder(newClient);

            bot.execute(new SendMessage(chatId,
                    "Информация о клиенте изменена"));

            sendMainMenu(chatId, null);
        }
        //Неизвестная команда или ошибка парсинга
        catch (Exception e){
            System.out.println("Неизвестная команда. Ошибка парсинга клиента: " + e.getMessage());

            if(client == null) {
                registrationMessage(chatId);
            }
            else {
                bot.execute(new SendMessage(chatId,
                        "Я не знаю такой команды."));

                sendMainMenu(chatId, null);
            }
        }
    }
    public void registrationMessage(Long chatId){
        bot.execute(new SendMessage(chatId,
                "Привет! Я Бот для автоматизации доставки заказов. Вначале нужно зарегистрироваться.\n" +
                        "Введи через запятую:\n|Полное Имя,Номер телефона(10 символов),Адрес|\n" +
                        "Без пробелов после запятых\n" +
                        "\n author @wayzwayzwayz"));
    }
    private void sendMainMenu(Long chatId, Long parentId) {
        List<KeyboardButton> categories = entitiesService.getCategoryByParent(parentId)
                .stream()
                .map(category -> new KeyboardButton(category.getName())).toList();

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
        markup.resizeKeyboard(true);
        markup.addRow(new KeyboardButton("Информация клиента"),new KeyboardButton("Редактировать информацию клиента"));
        markup.addRow(new KeyboardButton("Просмотреть заказ"), new KeyboardButton("Оформить заказ"));
        markup.addRow(new KeyboardButton("В основное меню"));
        bot.execute(new SendMessage(chatId,
                "Товары").replyMarkup(markup));
    }

    public void clientInformationEditMessage(Long chatId){
        bot.execute(new SendMessage(chatId,
                "Все как и в прошлый раз. Только теперь для редактирования.\n" +
                        "Введи через запятую:\n|Полное Имя,Номер телефона(10 символов),Адрес|\n" +
                        "Без пробелов после запятых"));
    }

    private void newClientOrder(Client client){
        ClientOrder clientOrder = new ClientOrder(client, 1, new BigDecimal(0));
        clientOrderRepository.save(clientOrder);
    }
    public void editOrder(Long clientId, String[] callback, Long chatId){
        Long productId = null;
        try {
            productId = Long.parseLong(callback[1]);
        } catch (Exception e) {
            System.out.println("Ошибка парсинга: " + e.getMessage());
        }

        if (productId != null) {
            ClientOrder clientOrder = clientOrderRepository
                    .findNewOrderByClientId(clientId);

            OrderProduct orderProduct = orderProductRepository
                    .findProductByClientOrderId(clientOrder.getId(), productId);

            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            String text = "";
            switch (callback[0]) {
                case "add" -> {
                    if (orderProduct == null) {
                        orderProduct = new OrderProduct(clientOrder,
                                entitiesService.getProductById(productId), 1);
                    } else {
                        orderProduct.setCountProduct(orderProduct.getCountProduct() + 1);
                    }
                    orderProductRepository.save(orderProduct);

                    clientOrder.setTotal(clientOrder.getTotal().add(orderProduct.getProduct().getPrice()));
                    clientOrderRepository.save(clientOrder);

                    InlineKeyboardButton button = new
                            InlineKeyboardButton("Добавить еще 1 к заказу")
                            .callbackData("add:" + orderProduct.getProduct().getId());

                    markupInline.addRow(button);

                    text = orderProduct.getProduct().getName() + " успешно добавлен к заказу";
                }
            }

            bot.execute(new SendMessage(chatId,
                    text).replyMarkup(markupInline));
        }
    }
}