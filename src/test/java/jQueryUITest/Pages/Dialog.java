package jQueryUITest.Pages;

import com.codeborne.selenide.SelenideElement;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static jQueryUITest.Utils.*;

public enum Dialog {

    Dialog($("div[role='dialog']")),
    TitleBar($("#ui-id-1")),
    ContentArea($("#dialog")),
    UsersTable($("#users")),

    OpenDialog($("#opener")),
    DeleteAllItems($x("//button[text()='Delete all items']")),
    CreateNewUser($("#create-user")),
    Name($("#name")),
    Email($("#email")),
    Password($("#password")),
    CreateAnAccount($x("//button[text()='Create an account']")),
    Cancel($x("//button[text()='Cancel']")),
    Ok($x("//button[text()='Ok']")),
    Close($("button[title='Close']"));

    private final SelenideElement element;

    Dialog(SelenideElement element) {this.element = element;}

    public void click() {element.shouldBe(visible).click();}

    public void input(String text) {element.shouldBe(visible).setValue(text);}

    public static void waitForPageLoad () {$("h1.entry-title").shouldBe(visible).shouldHave(text("Dialog"));}

    public static void turnMode(String mode) {
        switchTo().defaultContent();
        scrollToElement($("iframe.demo-frame"));
        SelenideElement link = $x("//li[.//text()='" + mode + "']");
        link.click();
        link.shouldHave(cssClass("active"));
        switchTo().frame($("iframe.demo-frame"));
    }

    public static boolean dialogAppearTest() {
        try {
            ContentArea.element.should(appear);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static boolean dialogDisappearTest() {
        try {
            ContentArea.element.should(disappear);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static void moveDialog(int x, int y) {
        actions().clickAndHold(TitleBar.element.shouldBe(visible)).moveByOffset(x, y).release().perform();
    }

    public static boolean dialogMoveTest() {
        double top, left;
        top = Double.parseDouble(Dialog.element.shouldBe(visible).getCssValue("top").replace("px", ""));
        left = Double.parseDouble(Dialog.element.shouldBe(visible).getCssValue("left").replace("px", ""));
        System.out.println("top: " + top + "px, " + "left: " + left + "px.");
        moveDialog(100, 100);
        double newTop, newLeft;
        newTop = Double.parseDouble(Dialog.element.shouldBe(visible).getCssValue("top").replace("px", ""));
        newLeft = Double.parseDouble(Dialog.element.shouldBe(visible).getCssValue("left").replace("px", ""));
        System.out.println("top: " + newTop + "px, " + "left: " + newLeft + "px.");
        System.out.println("Выполненное движение по вертикали: " + (newTop - top) + "px, " + "по горизонтали: " + (newLeft - left) + "px.");
        return top != newTop && left != newLeft;
    }

    public static void createNewUser (String name, String email, String password) {
        CreateNewUser.click();
        Name.input(name);
        Email.input(email);
        Password.input(password);
        CreateAnAccount.click();
    }

    public static String createNewUserTest () throws IOException {
        String name = getRandomLineFromFile("FirstNames.MD") + " " + getRandomLineFromFile("LastNames.MD");
        System.out.println("Имя нового пользователя: " + name);
        String email = generateRandomEmail();
        System.out.println("Email нового пользователя: " + email);
        String password = generateRandomPassword();
        System.out.println("Пароль нового пользователя: " + password);
        try {createNewUser(name, email, password);}
        catch (AssertionError e) {return "не удалось создать нового пользователя!";}
        try {UsersTable.element.shouldBe(visible).shouldHave(text(name), text(email));}
        catch (AssertionError e) {return "новый пользователь не был добавлен в таблицу!";}
        return "Test passed!";
    }

}