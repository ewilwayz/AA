package ru.andrey.tgBot;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.andrey.tgBot.entity.Category;
import ru.andrey.tgBot.entity.Client;
import ru.andrey.tgBot.entity.Product;
import ru.andrey.tgBot.repository.CategoryRepository;
import ru.andrey.tgBot.repository.ClientRepository;
import ru.andrey.tgBot.repository.ProductRepository;

@SpringBootTest

public class FillingTests {


    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    void createCategoriesAndProducts() {
        //--- Создание основных категорий ---
        Category pizza = new Category(); //--Пицца
        pizza.setName("Пицца");
        pizza.setParent(null);
        categoryRepository.save(pizza);

        Category rolls = new Category(); //--Роллы
        rolls.setName("Роллы");
        rolls.setParent(null);
        categoryRepository.save(rolls);

        Category burgers = new Category(); //--Бургеры
        burgers.setName("Бургеры");
        burgers.setParent(null);
        categoryRepository.save(burgers);

        Category drinks = new Category(); //--Напитки
        drinks.setName("Напитки");
        drinks.setParent(null);
        categoryRepository.save(drinks);

        //--- Создание подкатегорий для категории "Роллы" ---
        Category classicRolls = new Category();
        classicRolls.setName("Классические роллы");
        classicRolls.setParent(rolls);
        categoryRepository.save(classicRolls);

        // Создание подкатегории запеченные роллы
        Category bakedRolls = new Category();
        bakedRolls.setName("Запеченные роллы");
        bakedRolls.setParent(rolls);
        categoryRepository.save(bakedRolls);

        // Создание подкатегории запеченные роллы
        Category sweetRolls = new Category();
        sweetRolls.setName("Сладкие роллы");
        sweetRolls.setParent(rolls);
        categoryRepository.save(sweetRolls);

        // Создание подкатегории запеченные роллы
        Category setRolls = new Category();
        setRolls.setName("Наборы");
        setRolls.setParent(rolls);
        categoryRepository.save(setRolls);

        // Создание подкатегории запеченные классических бургеров
        Category classicBurgers = new Category();
        classicBurgers.setName("Классические бургеры");
        classicBurgers.setParent(burgers);
        categoryRepository.save(classicBurgers);

        // Создание подкатегории острые бургеры
        Category spicyBurgers = new Category();
        spicyBurgers.setName("Острые бургеры");
        spicyBurgers.setParent(burgers);
        categoryRepository.save(spicyBurgers);

        //Создание подкатегории для газированных напитков
        Category carbonatedDrinks = new Category();
        carbonatedDrinks.setName("Газированные напитки");
        carbonatedDrinks.setParent(drinks);
        categoryRepository.save(carbonatedDrinks);

        //Создание подкатегории для энергетических напитков
        Category energeticDrinks = new Category();
        energeticDrinks.setName("Энергетические напитки");
        energeticDrinks.setParent(drinks);
        categoryRepository.save(energeticDrinks);

        //Создание подкатегории для соков
        Category juiceDrinks = new Category();
        juiceDrinks.setName("Соки");
        juiceDrinks.setParent(drinks);
        categoryRepository.save(juiceDrinks);

        //Создание подкатегории для других
        Category otherDrinks = new Category();
        otherDrinks.setName("Соки");
        otherDrinks.setParent(drinks);
        categoryRepository.save(otherDrinks);

        //--- Создание товаров для категорий ---

        //Товары для категория "Пицца"
        Product pizza1 = new Product();
        pizza1.setName("Маргарита");
        pizza1.setDescription("Традиционная итальянская пицца");
        pizza1.setPrice(789.99);
        pizza1.setCategory(pizza);
        productRepository.save(pizza1);

        Product pizza2 = new Product();
        pizza2.setName("Пепперони");
        pizza2.setDescription("Острая разновидность салями итало-американского происхождения");
        pizza2.setPrice(889.99);
        pizza2.setCategory(pizza);
        productRepository.save(pizza2);

        Product pizza3 = new Product();
        pizza3.setName("4 Сыра");
        pizza3.setDescription("Разновидность пиццы в итальянской кухне, покрытая комбинацией из четырёх видов сыра, с томатным соусом или без него");
        pizza3.setPrice(989.99);
        pizza3.setCategory(pizza);
        productRepository.save(pizza3);

        //--- Товары для категория "Роллы" ---

        //Товары для категория "Классические Роллы"
        Product rollClassic1 = new Product();
        rollClassic1.setName("Филадельфия");
        rollClassic1.setDescription("Разновидность роллов, обычно сделанные из копченого лосося, сливочного сыра и огурца");
        rollClassic1.setPrice(599.99);
        rollClassic1.setCategory(classicRolls);
        productRepository.save(rollClassic1);

        Product rollClassic2 = new Product();
        rollClassic2.setName("Калифорния");
        rollClassic2.setDescription("Разновидность роллов, приготовляемая вывернутым рисом наружу");
        rollClassic2.setPrice(699.99);
        rollClassic2.setCategory(classicRolls);
        productRepository.save(rollClassic2);

        Product rollClassic3 = new Product();
        rollClassic3.setName("Авакадо ролл");
        rollClassic3.setDescription("Ролл Авокадо состоит из трех основных ингредиентов – это нори, рис и авокадо");
        rollClassic3.setPrice(499.99);
        rollClassic3.setCategory(classicRolls);
        productRepository.save(rollClassic3);

        //Товары для категория "Запеченные роллы"

        Product rollBaked1 = new Product();
        rollBaked1.setName("Запеченная Филадельфия");
        rollBaked1.setDescription("Вариация классического суши-ролла Филадельфия, который покрывается специальным соусом и запекается в духовке");
        rollBaked1.setPrice(589.99);
        rollBaked1.setCategory(bakedRolls);
        productRepository.save(rollBaked1);

        Product rollBaked2 = new Product();
        rollBaked2.setName("Запеченная Калифорния");
        rollBaked2.setDescription("Вариация классического суши-ролла Калифорния, который покрывается специальным соусом и запекается в духовке");
        rollBaked2.setPrice(659.99);
        rollBaked2.setCategory(bakedRolls);
        productRepository.save(rollBaked2);

        Product rollBaked3 = new Product();
        rollBaked3.setName("Запеченный динамит");
        rollBaked3.setDescription("Ролл — это сочное сочетание авокадо, креветок и спайси майонеза. Запекается в духовке до образования золотистой корочки");
        rollBaked3.setPrice(799.99);
        rollBaked3.setCategory(bakedRolls);
        productRepository.save(rollBaked3);

        //Товары для категория "Сладкие роллы"

        Product rollSweet1 = new Product();
        rollSweet1.setName("Фруктовый ролл ");
        rollSweet1.setDescription("Этот сладкий ролл обычно сделан с использованием фруктов, таких как манго, клубника, банан или киви");
        rollSweet1.setPrice(589.99);
        rollSweet1.setCategory(sweetRolls);
        productRepository.save(rollSweet1);

        Product rollSweet2 = new Product();
        rollSweet2.setName("Шоколадный ролл ");
        rollSweet2.setDescription("Этот ролл представляет собой комбинацию сладкого риса, клубничного джема, бананов или ягод, завернутых в слой нори и обсыпанных шоколадной крошкой");
        rollSweet2.setPrice(659.99);
        rollSweet2.setCategory(sweetRolls);
        productRepository.save(rollSweet2);

        Product rollSweet3 = new Product();
        rollSweet3.setName("Карамельный ролл");
        rollSweet3.setDescription("В этом сладком ролле используется карамель или карамельный соус в сочетании с бананами, орехами или клубникой");
        rollSweet3.setPrice(799.99);
        rollSweet3.setCategory(sweetRolls);
        productRepository.save(rollSweet3);

        //Товары для категория "Наборы"

        Product setRolls1 = new Product();
        setRolls1.setName("Сет Классический");
        setRolls1.setDescription("Этот набор роллов включает в себя основные классические варианты, такие как калифорния ролл, филадельфия ролл, спайси тунец ролл и огуречный ролл");
        setRolls1.setPrice(1589.99);
        setRolls1.setCategory(setRolls);
        productRepository.save(setRolls1);

        Product setRolls2 = new Product();
        setRolls2.setName("Сет Экзотика");
        setRolls2.setDescription("В него могут входить роллы с угрем, копченым лососем, манго или авокадо");
        setRolls2.setPrice(1659.99);
        setRolls2.setCategory(setRolls);
        productRepository.save(setRolls2);

        Product setRolls3 = new Product();
        setRolls3.setName("Сет Веганский");
        setRolls3.setDescription("В него могут входить роллы с овощами, авокадо, и нори");
        setRolls3.setPrice(1199.99);
        setRolls3.setCategory(setRolls);
        productRepository.save(setRolls3);

        //--- Товары для категория "Бургеры" ---

        //Товары для категория "Классические Бургеры"

        Product burgersClassic1 = new Product();
        burgersClassic1.setName("Гамбургер");
        burgersClassic1.setDescription("Классический бургер, состоящий из прожаренной котлеты из мяса, обжаренного лука, листьев салата, кетчупа и майонеза в мягкой булочке");
        burgersClassic1.setPrice(389.99);
        burgersClassic1.setCategory(classicBurgers);
        productRepository.save(burgersClassic1);

        Product burgersClassic2 = new Product();
        burgersClassic2.setName("Чизбургер");
        burgersClassic2.setDescription("Вариация гамбургера, в которой добавляется ломтик сыра на котлету перед подачей");
        burgersClassic2.setPrice(459.99);
        burgersClassic2.setCategory(classicBurgers);
        productRepository.save(burgersClassic2);

        Product burgersClassic3 = new Product();
        burgersClassic3.setName("Мексиканец");
        burgersClassic3.setDescription("Пряный и ароматный вариант классического бургера. Он часто содержит пряную котлету, сальсу, гуакамоле.");
        burgersClassic3.setPrice(529.99);
        burgersClassic3.setCategory(classicBurgers);
        productRepository.save(burgersClassic3);

        //Товары для категория "Острые Бургеры"

        Product burgersSpicy1 = new Product();
        burgersSpicy1.setName("Огненный Драконий Бургер");
        burgersSpicy1.setDescription("Этот бургер заполнен силой острых специй и соусов, включая соус срирача, карибский перец скотч боннет, ломтик кимчи и свежая зелень.");
        burgersSpicy1.setPrice(589.99);
        burgersSpicy1.setCategory(spicyBurgers);
        productRepository.save(burgersSpicy1);

        Product burgersSpicy2 = new Product();
        burgersSpicy2.setName("Дьявольский Чили Бургер");
        burgersSpicy2.setDescription("Этот бургер заправлен крепкими порциями острого чили-кетчупа, пикантным сыром чеддер, горячими пепперони и свежими ломтиками помидора. ");
        burgersSpicy2.setPrice(559.99);
        burgersSpicy2.setCategory(spicyBurgers);
        productRepository.save(burgersSpicy2);

        Product burgersSpicy3 = new Product();
        burgersSpicy3.setName("Пламенный Инферно Бургер");
        burgersSpicy3.setDescription("Состоит из острых чили-котлет, маринованных халапеньо перцев, кисло-сладкого соуса на основе табаско и пикантного горчично-майонезного соуса.");
        burgersSpicy3.setPrice(629.99);
        burgersSpicy3.setCategory(spicyBurgers);
        productRepository.save(burgersSpicy3);

        //--- Товары для категория "Напитки" ---

        //Товары для категория "Газированные напитки"

        Product drinksCarbonated1 = new Product();
        drinksCarbonated1.setName("Кола");
        drinksCarbonated1.setDescription("Классическая сладка газировка");
        drinksCarbonated1.setPrice(111.99);
        drinksCarbonated1.setCategory(carbonatedDrinks);
        productRepository.save(drinksCarbonated1);

        Product drinksCarbonated2 = new Product();
        drinksCarbonated2.setName("Кола-зеро");
        drinksCarbonated2.setDescription("Газировка без добавления сахара. Низкокаллорийная");
        drinksCarbonated2.setPrice(129.99);
        drinksCarbonated2.setCategory(carbonatedDrinks);
        productRepository.save(drinksCarbonated2);

        Product drinksCarbonated3 = new Product();
        drinksCarbonated3.setName("Кола-ванила");
        drinksCarbonated3.setDescription("Всеми любимая Кола, но со вкусом ванили");
        drinksCarbonated3.setPrice(139.99);
        drinksCarbonated3.setCategory(carbonatedDrinks);
        productRepository.save(drinksCarbonated3);

        //Товары для категория "Энергетические напитки"

        Product drinksEnergetic1 = new Product();
        drinksEnergetic1.setName("Flash");
        drinksEnergetic1.setDescription("Этот энергетический напиток обычно характеризуется своим ярким вкусом и высоким содержанием кофеина");
        drinksEnergetic1.setPrice(81.99);
        drinksEnergetic1.setCategory(energeticDrinks);
        productRepository.save(drinksEnergetic1);

        Product drinksEnergetic2 = new Product();
        drinksEnergetic2.setName("Burn");
        drinksEnergetic2.setDescription("Энергетик Burn обычно известен своим освежающим вкусом и содержанием таварина");
        drinksEnergetic2.setPrice(119.99);
        drinksEnergetic2.setCategory(energeticDrinks);
        productRepository.save(drinksEnergetic2);

        Product drinksEnergetic3 = new Product();
        drinksEnergetic3.setName("Redbull");
        drinksEnergetic3.setDescription("Он славится своим характерным вкусом, содержанием кофеина и таурина");
        drinksEnergetic3.setPrice(139.99);
        drinksEnergetic3.setCategory(energeticDrinks);
        productRepository.save(drinksEnergetic3);

        //Товары для категория "Соки"

        Product drinksJuice1 = new Product();
        drinksJuice1.setName("Любимый");
        drinksJuice1.setDescription("Сок Любимый обычно ассоциируется с натуральными вкусами фруктов и ягод.");
        drinksJuice1.setPrice(99.99);
        drinksJuice1.setCategory(juiceDrinks);
        productRepository.save(drinksJuice1);

        Product drinksJuice2 = new Product();
        drinksJuice2.setName("Добрый");
        drinksJuice2.setDescription("Сок Добрый известен своим свежим и приятным вкусом");
        drinksJuice2.setPrice(119.99);
        drinksJuice2.setCategory(juiceDrinks);
        productRepository.save(drinksJuice2);

        Product drinksJuice3 = new Product();
        drinksJuice3.setName("Рич");
        drinksJuice3.setDescription("Сок Рич обычно характеризуется высоким содержанием питательных веществ, таких как витамины С и Е, бета-каротин и другие.");
        drinksJuice3.setPrice(139.99);
        drinksJuice3.setCategory(juiceDrinks);
        productRepository.save(drinksJuice3);

        //Товары для категория "Другие"

        Product drinksOther1 = new Product();
        drinksOther1.setName("Вода негазированная");
        drinksOther1.setDescription("Простая вода, наполненная минералами");
        drinksOther1.setPrice(49.99);
        drinksOther1.setCategory(otherDrinks);
        productRepository.save(drinksOther1);

        Product drinksOther2 = new Product();
        drinksOther2.setName("Чай");
        drinksOther2.setDescription("Фруктовый чай, с Кавказских гор");
        drinksOther2.setPrice(89.99);
        drinksOther2.setCategory(otherDrinks);
        productRepository.save(drinksOther2);

        Product drinksOther3 = new Product();
        drinksOther3.setName("Кофе");
        drinksOther3.setDescription("Классическое эспрессо.");
        drinksOther3.setPrice(89.99);
        drinksOther3.setCategory(otherDrinks);
        productRepository.save(drinksOther3);

    }

    void createTwoClients() {
        Client client1 = new Client();
        client1.setAddress("Симферополь");
        client1.setExternalId(1L);
        client1.setPhoneNumber("89780010101");
        client1.setFullName("Клочков Богдан Константинович");
        clientRepository.save(client1);
        Client client2 = new Client();
        client2.setAddress("Севастополь");
        client2.setExternalId(2L);
        client2.setPhoneNumber("89780020202");
        client2.setFullName("Лапицкая Мария Романовна");
        clientRepository.save(client2);
    }

}
