package jQueryUITest.Tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.logging.Level;

import static com.codeborne.selenide.Selenide.*;
import static jQueryUITest.Pages.Dialog.*;
import static java.util.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.*;

public class DialogTest {

    private static Level previousLevel;

    @BeforeAll
    public static void setUp() {
        previousLevel = getLogger("").getLevel();
        getLogger("").setLevel(Level.WARNING);
        Configuration.baseUrl = "https://demoqa.com/automation-practice-form";
        Configuration.browser = "firefox";
        Configuration.browserCapabilities = new FirefoxOptions().setPageLoadStrategy(PageLoadStrategy.EAGER)
                .addArguments("--headless", "--window-size=1920,1080", "--disable-notifications", "--disable-gpu", "--disable-dev-tools", "--fastSetValue");
        open("https://jqueryui.com/dialog/");
        waitForPageLoad();
    }

    @Test
    public void defaultFunctionalityTest() {
        System.out.println("\nТестируем основную функциональность модального окна.");
        turnMode("Default functionality");
        try {assertTrue(dialogMoveTest(), "\nОшибка: модальное окно не перемещается!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        Close.click();
        try {assertTrue(dialogDisappearTest(), "\nОшибка: модальное окно не перемещается!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void animationTest() {
        System.out.println("\nТестируем появление и исчезновение модального окна.");
        turnMode("Animation");
        OpenDialog.click();
        try {assertTrue(dialogAppearTest(), "\nОшибка: модальное окно не появилось!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        Close.click();
        try {assertTrue(dialogDisappearTest(), "\nОшибка: модальное окно не исчезло!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void deleteAllItemsTest() {
        System.out.println("\nТестируем кнопку подтверждения действия модального окна.");
        turnMode("Modal confirmation");
        DeleteAllItems.click();
        try {assertTrue(dialogDisappearTest(), "\nОшибка: модальное окно не исчезло!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!\n");
    }

    @Test
    public void cancelDeleteTest() {
        System.out.println("Тестируем кнопку отмены модального окна.");
        turnMode("Modal confirmation");
        Cancel.click();
        try {assertTrue(dialogDisappearTest(), "\nОшибка: модальное окно не исчезло!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void modalFormTest() {
        System.out.println("\nТестируем форму заполнения модального окна.");
        turnMode("Modal form");
        try {
            String result = createNewUserTest();
            assertEquals("Test passed!", result, "\nОшибка: " + result + "\nТест провален!\n");
        }
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void modalMessageTest() {
        System.out.println("\nТестируем кнопку подтверждения сообщения модального окна.");
        turnMode("Modal message");
        Ok.click();
        try {assertTrue(dialogDisappearTest(), "\nОшибка: модальное окно не исчезло!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @AfterAll
    public static void tearDown() {
        closeWebDriver();
        getLogger("").setLevel(previousLevel);
    }

}