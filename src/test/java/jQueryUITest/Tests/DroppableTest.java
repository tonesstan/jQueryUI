package jQueryUITest.Tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.logging.Level;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static jQueryUITest.Pages.Droppable.*;
import static jQueryUITest.Utils.numbers;
import static java.util.logging.Logger.getLogger;
import static org.junit.jupiter.api.Assertions.*;

public class DroppableTest {

    private static Level previousLevel;

    @BeforeAll
    public static void setUp() {
        previousLevel = getLogger("").getLevel();
        getLogger("").setLevel(Level.WARNING);
        Configuration.baseUrl = "https://demoqa.com/automation-practice-form";
        Configuration.browser = "firefox";
        Configuration.browserCapabilities = new FirefoxOptions().setPageLoadStrategy(PageLoadStrategy.EAGER)
                .addArguments("--headless", "--window-size=1920,1080", "--disable-notifications", "--disable-gpu", "--disable-dev-tools", "--fastSetValue");
        open("https://jqueryui.com/droppable/");
        waitForPageLoad();
    }

    @Test
    public void defaultFunctionalityTest() {
        System.out.println("\nТестируем основную функциональность перетаскиваемых элементов.");
        turnMode("Default functionality");
        $x("//p[text()='Drop here']").shouldBe(visible);
        $x("//h3[text()='Default functionality:']").shouldNotBe(visible);
        try {assertTrue(dragAndDropToTest(Draggable, Droppable), "\nОшибка: элемент не был успешно принят!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void draggableAcceptTest() {
        System.out.println("\nТестируем возможность принимать валидный перетаскиваемый элемент.");
        turnMode("Accept");
        $x("//p[text()=\"I'm draggable but can't be dropped\"]").shouldBe(visible);
        try {assertTrue(dragAndDropToTest(Draggable, Droppable), "\nОшибка: валидный элемент не был успешно принят!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void draggableRejectTest() {
        System.out.println("\nТестируем возможность отклонять невалидный перетаскиваемый элемент.");
        turnMode("Accept");
        $x("//p[text()=\"I'm draggable but can't be dropped\"]").shouldBe(visible);
        try {assertFalse(dragAndDropToTest(DraggableNonValid, Droppable), "\nОшибка: невалидный элемент был успешно принят!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void preventPropagationTest() {
        System.out.println("\nТестируем возможность перехватывать всплытие событий в принимающем элементе.");
        turnMode("Prevent propagation");
        $x("//p[text()='Outer droppable']").shouldBe(visible);
        try {assertTrue(preventPropagationCheck(Draggable, Droppable2, Droppable2Inner), "\nОшибка: элемент не был успешно перехвачен!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void notPreventPropagationTest() {
        System.out.println("\nТестируем возможность не перехватывать всплытие событий в принимающем элементе.");
        turnMode("Prevent propagation");
        $x("//p[text()='Outer droppable']").shouldBe(visible);
        try {assertTrue(notPreventPropagationCheck(Draggable, Droppable, DroppableInner), "\nОшибка: элемент был успешно перехвачен!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void revertDraggableTest() {
        System.out.println("\nТестируем возможность возврата перетаскиваемого элемента.");
        turnMode("Revert draggable position");
        $x("//p[text()=\"I revert when I'm dropped\"]").shouldBe(visible);
        try {
            assertTrue(Draggable.revertTest(Droppable), "\nОшибка: элемент не был успешно возвращён от принимающего элемента!\nТест провален!\n");
            assertFalse(Draggable.revertTest(Draggable2), "\nОшибка: элемент был возвращён после обычного перемещения!\nТест провален!\n");
        }
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void revertDraggable2Test() {
        System.out.println("\nТестируем возможность возврата перетаскиваемого элемента.");
        turnMode("Revert draggable position");
        $x("//p[text()=\"I revert when I'm not dropped\"]").shouldBe(visible);
        try {
            assertFalse(Draggable2.revertTest(Droppable), "\nОшибка: элемент был возвращён от принимающего элемента!\nТест провален!\n");
            assertTrue(Draggable2.revertTest(Draggable), "\nОшибка: элемент не был возвращён после обычного перемещения!\nТест провален!\n");
        }
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void visualFeedbackTest() {
        System.out.println("\nТестируем визуальное отображение действий пользователя на принимающем элементе.");
        turnMode("Visual feedback");
        $x("//h3[text()='Feedback on hover:']").shouldBe(visible);
        try {assertTrue(Draggable.dragAndNotDropToTest(Droppable), "\nОшибка: действия пользователя не были отображены на принимающем элементе!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @Test
    public void VisualFeedback2Test() {
        System.out.println("\nТестируем визуальное отображение действий пользователя на принимающем элементе.");
        turnMode("Visual feedback");
        $x("//h3[text()='Feedback on activating draggable:']").shouldBe(visible);
        try {assertTrue(Draggable2.clickAndLittleMoveTest(Droppable2), "\nОшибка: действия пользователя не были отображены на принимающем элементе!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @ParameterizedTest(name = "Тест удаления и восстановления картинки {0}")
    @MethodSource("paramSource")
    public void deleteAndRecycleImageTest(int i) {
        System.out.println("\nТестируем удаление и восстановление картинки " + i + ".");
        turnMode("Simple photo manager");
        $x("//h4[text()=' Trash']").shouldBe(visible);
        try {assertTrue(checkImage(i), "\nОшибка: картинка " + i + " не найдена!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем в процессе поиска картинки!\nТест провален! Подробности:\n" + e.getMessage());}
        try {
            String result = imageDeleteAndRecycleTest(i);
            assertEquals("Test passed!", result, "\nОшибка: " + result + "\nТест провален!\n");
        }
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    @ParameterizedTest(name = "Тест открытия и закрытия большой картинки {0}")
    @MethodSource("paramSource")
    public void openAndCloseBigImageTest(int i) {
        System.out.println("\nТестируем открытие и закрытия большой картинки " + i + ".");
        turnMode("Simple photo manager");
        $x("//h4[text()=' Trash']").shouldBe(visible);
        try {assertTrue(checkImage(i), "\nОшибка: картинка " + i + " не найдена!\nТест провален!\n");}
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем в процессе поиска картинки!\nТест провален! Подробности:\n" + e.getMessage());}
        try {
            String result = imageOpenLargeAndCloseTest(i);
            assertEquals("Test passed!", result, "\nОшибка: " + result + "\nТест провален!\n");
        }
        catch (Exception e) {fail("\nОшибка: тест не был успешно завершён из-за непредвиденных проблем!\nТест провален! Подробности:\n" + e.getMessage());}
        System.out.println("\nТест прошёл успешно!");
    }

    static Stream<Integer> paramSource() {return numbers(4);}

    @AfterAll
    public static void tearDown() {
        closeWebDriver();
        getLogger("").setLevel(previousLevel);
    }

}