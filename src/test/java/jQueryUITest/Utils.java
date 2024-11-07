package jQueryUITest;

import com.codeborne.selenide.SelenideElement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class Utils {

    //получаем по заданной ссылке API-ответ в виде одной единственной строки из JSON-ответа
    private static String getResponseAsString(String url) throws Exception {
        URL urlObj = new URI(url).toURL();
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = in.readLine();
        in.close();
        return response;
    }

    //получаем случайное английское слово заданной длины с random-word-api.herokuapp.com
    private static String randomWord(int min, int max) {
        String url;
        try {
	    int length = randomNumber(min, max);
            url = "https://random-word-api.herokuapp.com/word?length=" + length;
            return getResponseAsString(url).substring(2, length + 2);
        } catch (Exception e) {
            String word;
            url = "https://random-word-form.herokuapp.com/random/noun";
            do { try {
                    word = getResponseAsString(url);
                    word = word.substring(2, word.length() - 2);
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
                return null;
            }} while (word.length() < min || word.length() > max);
            return word;
        }
    }

    //скролим к элементу, чтобы он был в центре экрана
    public static void scrollToElement(SelenideElement element){executeJavaScript("arguments[0].scrollIntoView({block: 'center'});", element);}

    //извлекаем случайную строчку из текстового файла
    public static String getRandomLineFromFile(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(lines.size());
        return lines.get(index);
    }

    //генерируем случайную строку с заданными допустимыми длиной и символами
    public static String generateRandomString(int minLength, int maxLength, String allowedChars) {
        SecureRandom random = new SecureRandom();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            sb.append(allowedChars.charAt(index));
        }
        return sb.toString();
    }

    //генерируем случайное целое число в заданном диапазоне
    public static int randomNumber(int min, int max) {return new SecureRandom().nextInt(max - min + 1) + min;}

    //генерируем случайный пароль, соответствующий допустимым значениям и требованиям
    public static String generateRandomPassword() {
        String password;
        do {
            password = generateRandomString(5, 16, "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        } while (!(password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*[0-9].*")));
        return password;
    }

    //генерируем случайный ненастоящий типичный адрес электронной почты
    public static String generateRandomEmail() {
        return randomWord(5, 15) + randomNumber(10, 999) + "@" + randomWord(4, 8) + ".com";
    }

    //создаём поток целых чисел от 1 до N
    public static Stream<Integer> numbers(int n) {return Stream.iterate(1, i -> i + 1).limit(n);}

}