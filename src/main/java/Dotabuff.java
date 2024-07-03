import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Dotabuff {
    public static void main(String[] args) {

        String filePath = args[0];
        int n = Integer.parseInt(args[1]);

        Map<Integer, Map<Integer, Integer>> heroStats = new HashMap<>();
        Map<Integer, Integer> totalHeroGames = new HashMap<>();

        try {
            Scanner scanner = new Scanner(new File(filePath));

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (line.startsWith("user_id,hero_id,num_games")){
                    continue;
                }

                String[] info = line.split(",");
                int userId = Integer.parseInt(info[0]);
                int heroId = Integer.parseInt(info[1]);
                int numGames = Integer.parseInt(info[2]);

                heroStats.computeIfAbsent(heroId, k -> new HashMap<>()) // Проверка на существование записи с heroId
                        .merge(userId, numGames, Integer::sum);

                totalHeroGames.put(heroId, totalHeroGames.getOrDefault(heroId, 0) + numGames);
            }
            List<Map.Entry<Integer, Integer>> popularHeroes = new ArrayList<>(totalHeroGames.entrySet());
            popularHeroes.sort(
                    (entry1, entry2) -> {
                        Integer gamesA = entry1.getValue();
                        Integer gamesB = entry2.getValue();
                        return gamesB.compareTo(gamesA);
                    }
            );
            System.out.println("| hero_id | num_all_games | user_id |");
            System.out.println("|_________|_______________|_________|");

            int count = 0;
            for (Map.Entry<Integer, Integer> entry : popularHeroes) {
                if (count >= n) {
                    break;
                }

                int heroId = entry.getKey();
                int totalGames = entry.getValue();

                Map<Integer, Integer> heroUsers = heroStats.get(heroId);

                // Находим пользователя с наибольшим количеством игр
                Integer mostPlayedUserId = heroUsers.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(-1); // Если пользователь не найден, возвращаем -1
                System.out.printf("| %5d   | %9d     | %6d  |%n", heroId, totalGames, mostPlayedUserId);
                count++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        }
    }
}