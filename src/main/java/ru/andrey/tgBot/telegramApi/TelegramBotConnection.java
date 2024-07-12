package ru.andrey.tgBot.telegramApi;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import ru.andrey.tgBot.entity.*;
import ru.andrey.tgBot.repository.ClientRepository;
import ru.andrey.tgBot.repository.ClientOrderRepository;
import ru.andrey.tgBot.repository.CategoryRepository;
import ru.andrey.tgBot.repository.ProductRepository;
import ru.andrey.tgBot.repository.OrderProductRepository;
import ru.andrey.tgBot.service.AppService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TelegramBotConnection {
    private final ClientRepository clientRepository;
    private final ClientOrderRepository clientOrderRepository;
    private final ProductRepository productRepository;
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
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository1;
        this.orderProductRepository = orderProductRepository;
    }

    @PostConstruct
    public void createConnection() {
        bot = new TelegramBot("7057407427:AAHkYM-jRf0-NkEtSnWCX4jUliNCI31jxxk");
        bot.setUpdatesListener(new TelegramUpdatesListener());
    }

    private void processClientData(String userText, Long chatId, TelegramBot bot, Map<Long, Integer> userStates, Update update) {
        String[] userData = userText.substring("Данные: ".length()).split("; ");

        if (userData.length >= 3) {
            Client client = new Client();
            client.setExternalId(chatId);
            client.setFullName(userData[0]);
            client.setPhoneNumber(userData[1]);
            client.setAddress(userData[2]);
            clientRepository.save(client);

            ClientOrder clientOrder = new ClientOrder();
            clientOrder.setClient(client);
            clientOrder.setStatus(1);
            clientOrder.setTotal(BigDecimal.valueOf(0));
            clientOrderRepository.save(clientOrder);

            SendMessage message = new SendMessage(chatId, "Данные успешно записаны, продолжайте работу");
            bot.execute(message);

            List<KeyboardButton> categories = categoryRepository.findCategoriesByParentIdIsNull()
                    .stream()
                    .map(category -> new KeyboardButton(category.getName()))
                    .collect(Collectors.toList());
            ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
            markup.resizeKeyboard(true);
            markup.addRow(new KeyboardButton("Оформить заказ"));
            markup.addRow(new KeyboardButton("Основное меню"));
            bot.execute(new SendMessage(update.message().chat().id(), "Выберите товары  :P").replyMarkup(markup));

            userStates.put(chatId, 1);
        } else {
            SendMessage errorMessage = new SendMessage(chatId, "Неверный формат ввода. Пожалуйста, введите данные в следующем формате: <Имя; Адрес; Номер>.");
            bot.execute(errorMessage);
            SendMessage retryMessage = new SendMessage(chatId, "Введите данные пользователя заново:");
            bot.execute(retryMessage);
        }
    }

    private void processCategorySelection(String userText, TelegramBot bot, Update update) {
        switch (userText) {
            case "Пицца",
                 "Классические роллы", "Запеченные роллы", "Сладкие роллы", "Наборы",
                 "Классические бургеры", "Острые бургеры",
                 "Газированные напитки", "Энергетические напитки", "Соки", "Другие" -> {
                List<Product> products = entitiesService.getCategoryProducts(userText);
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                for (Product product : products) {
                    InlineKeyboardButton button = new
                            InlineKeyboardButton(String.format("Товар %s. Цена %.2f руб.",
                            product.getName(), product.getPrice()))
                            .callbackData(String.format("product:%d", product.getId()));
                    markup.addRow(button);
                }
                bot.execute(new SendMessage(update.message().chat().id(),
                        "Выберите категории").replyMarkup(markup));
            }
            case "Роллы" -> {
                List<KeyboardButton> categories = categoryRepository.findCategoriesByParentId(2L)
                        .stream()
                        .map(category -> new KeyboardButton(category.getName()))
                        .collect(Collectors.toList());
                ReplyKeyboardMarkup markup = new
                        ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
                markup.resizeKeyboard(true);
                markup.addRow(new KeyboardButton("Оформить заказ"));
                markup.addRow(new KeyboardButton("Основное меню"));
                bot.execute(new SendMessage(update.message().chat().id(),
                        "Подкатегории").replyMarkup(markup));
            }
            case "Бургеры" -> {
                List<KeyboardButton> categories = categoryRepository.findCategoriesByParentId(3L)
                        .stream()
                        .map(category -> new KeyboardButton(category.getName()))
                        .collect(Collectors.toList());
                ReplyKeyboardMarkup markup = new
                        ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
                markup.resizeKeyboard(true);
                markup.addRow(new KeyboardButton("Оформить заказ"));
                markup.addRow(new KeyboardButton("Основное меню"));
                bot.execute(new SendMessage(update.message().chat().id(),
                        "Подкатегории").replyMarkup(markup));
            }
            case "Напитки" -> {
                List<KeyboardButton> categories = categoryRepository.findCategoriesByParentId(4L)
                        .stream()
                        .map(category -> new KeyboardButton(category.getName()))
                        .collect(Collectors.toList());
                ReplyKeyboardMarkup markup = new
                        ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
                markup.resizeKeyboard(true);
                markup.addRow(new KeyboardButton("Оформить заказ"));
                markup.addRow(new KeyboardButton("Основное меню"));
                bot.execute(new SendMessage(update.message().chat().id(),
                        "Подкатегории").replyMarkup(markup));
            }
            case "Основное меню" -> {
                /*
                 * Возврат в основное меню
                 */
                List<KeyboardButton> categories = categoryRepository.findCategoriesByParentIdIsNull()
                        .stream()
                        .map(category -> new KeyboardButton(category.getName())).toList();

                defaultMessage(update, categories);
            }
            case "Оформить заказ" -> {
                /*
                 * Оформление заказа
                 */
                sendSummaryMessage(update.message().chat().id());
            }
            default -> {
            }
        }
    }

    private void defaultMessage(Update update, List<KeyboardButton> categories) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(categories.toArray(KeyboardButton[]::new));
        markup.resizeKeyboard(true);
        new KeyboardButton("Оформить заказ");
        markup.addRow(new KeyboardButton("Основное меню"));
        bot.execute(new SendMessage(update.message().chat().id(),
                "Товары").replyMarkup(markup));
    }

    void sendSummaryMessage(long chatId) {
        List<Product> products = selectedProducts.getOrDefault(chatId, new ArrayList<>());
        if (!products.isEmpty()) {
            StringBuilder orderSummary = new StringBuilder("Заказ:\n");
            BigDecimal totalCost = BigDecimal.ZERO;

            for (Product product : products) {
                orderSummary.append(product.getName()).append(". Цена: ").append(product.getPrice()).append(" руб.\n");
                totalCost = totalCost.add(product.getPrice());
            }

            ClientOrder clientOrder = new ClientOrder();
            Client client = clientRepository.findByExternalId(chatId);
            clientOrder.setClient(client);
            clientOrder.setStatus(1);
            clientOrder.setTotal(totalCost);
            clientOrderRepository.save(clientOrder);

            for (Product product : products) {
                OrderProduct orderProduct = new OrderProduct();
                orderProduct.setClientOrder(clientOrder);
                orderProduct.setProduct(product);
                orderProduct.setCountProduct(1);
                orderProductRepository.save(orderProduct);
            }

            selectedProducts.remove(chatId);

            orderSummary.append("Общая стоимость: ").append(totalCost).append(" р.");
            bot.execute(new SendMessage(chatId, orderSummary.toString()));
        } else {
            bot.execute(new SendMessage(chatId, "Ошибка: Заказ пуст."));
        }
    }


    void handleCallback(String callbackData, long chatId) {
        if (callbackData.startsWith("product:")) {
            int productId = Integer.parseInt(callbackData.replace("product:", ""));
            Product selectedProduct = entitiesService.getProductById(productId);

            List<Product> products = selectedProducts.getOrDefault(chatId, new ArrayList<>());
            products.add(selectedProduct);
            selectedProducts.put(chatId, products);

            bot.execute(new SendMessage(chatId, "Товар " + selectedProduct.getName() + " добавлен в заказ."));
        }
    }

    private class TelegramUpdatesListener implements UpdatesListener {

        @Override
        public int process(List<Update> updates) {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }

        private void processUpdate(Update update) {
            if (update == null) {
                return;
            }
            if (update.callbackQuery() != null) {
                String callbackData = update.callbackQuery().data();
                Long chatId = update.callbackQuery().message().chat().id();
                handleCallback(callbackData, chatId);
            } else if (update.message() != null) {
                String userText = update.message().text();
                Long chatId = update.message().chat().id();
                Integer state = userStates.getOrDefault(chatId, 0);
                switch (state) {
                    /*
                     * Стартовое сообщение, добавление клиента
                     */
                    case 0 -> {
                        if (userText.startsWith("/start") && !userText.startsWith("Данные: ")) {
                            SendMessage greetingMessage = new SendMessage(update.message().chat().id(), "Здравствуйте! Это Телеграм-бот для автоматизации службы доставки\n\n" +
                                    "author: @wayzwayzwayz");
                            bot.execute(greetingMessage);
                            SendMessage clientsMessage = new SendMessage(update.message().chat().id(), "Введите данные клиента ;)\n\n" +
                                    "Пример ввода: Данные: Василий; 79786785432; Балаклава");
                            bot.execute(clientsMessage);
                        } else if (userText.startsWith("Данные: "))
                            processClientData(userText, chatId, bot, userStates, update);
                    }
                    /*
                     * Работа с подкатегориями и товарами
                     */
                    case 1 -> processCategorySelection(userText, bot, update);
                }
            }
        }
    }
}

