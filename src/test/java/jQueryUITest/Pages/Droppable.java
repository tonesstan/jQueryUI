package jQueryUITest.Pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static jQueryUITest.Utils.scrollToElement;

public enum Droppable {

    Draggable($("#draggable")),
    Draggable2($("#draggable2")),
    DraggableNonValid($("#draggable-nonvalid")),
    Droppable($("#droppable")),
    DroppableInner($("#droppable-inner")),
    Droppable2($("#droppable2")),
    Droppable2Inner($("#droppable2-inner")),

    Gallery($("#gallery")),
    Trash($("#trash"));

    private final SelenideElement element;

    Droppable (SelenideElement element) {this.element = element;}

    public String getStyle(String style) {return element.shouldBe(visible).getCssValue(style);}

    public void dragAndDropTo (Droppable droppable) {
        actions().clickAndHold(element.shouldBe(visible)).moveToElement(droppable.element.shouldBe(visible)).release().perform();
    }

    public static void waitForPageLoad () {$("h1.entry-title").shouldBe(visible).shouldHave(text("Droppable"));}

    public static void turnMode(String mode) {
        switchTo().defaultContent();
        scrollToElement($("iframe.demo-frame"));
        SelenideElement link = $x("//li[.//text()='" + mode + "']");
        link.click();
        link.shouldHave(cssClass("active"));
        switchTo().frame($("iframe.demo-frame"));
    }

    public static boolean dragAndDropToTest (Droppable draggable, Droppable droppable) {
        draggable.dragAndDropTo(droppable);
        try {
            droppable.element.shouldHave(exactText("Dropped!"));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static boolean preventPropagationCheck (Droppable draggable, Droppable droppable, Droppable innerDroppable) {
        draggable.dragAndDropTo(innerDroppable);
        innerDroppable.element.shouldHave(text("Dropped!"));
        try {
            droppable.element.$("p").shouldHave(text("Outer droppable"));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static boolean notPreventPropagationCheck(Droppable draggable, Droppable droppable, Droppable innerDroppable) {
        draggable.dragAndDropTo(innerDroppable);
        innerDroppable.element.shouldHave(text("Dropped!"));
        try {
            droppable.element.$("p").shouldHave(text("Dropped!"));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public boolean revertTest (Droppable droppable) {
        this.element.shouldBe(visible);
        droppable.element.shouldBe(visible);
        String x = this.getStyle("left").isEmpty() ? "0px" : this.getStyle("left");
        System.out.println(x);
        String y = this.getStyle("top").isEmpty() ? "0px" : this.getStyle("top");
        System.out.println(y);
        this.dragAndDropTo(droppable);
        try {
            this.element.shouldHave(cssValue("left", x));
            this.element.shouldHave(cssValue("top", y));
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public boolean dragAndNotDropToTest (Droppable droppable) {
        actions().clickAndHold(this.element.shouldBe(visible)).moveToElement(droppable.element.shouldBe(visible)).perform();
        try {
            droppable.element.shouldHave(attributeMatching("class", ".*ui-state.*"));
            return true;
        } catch (AssertionError e) {
            return false;
        } finally {
            actions().release().perform();
        }
    }

    public boolean clickAndLittleMoveTest (Droppable droppable) {
        actions().clickAndHold(this.element.shouldBe(visible)).moveByOffset(10, 10).perform();
        try {
            droppable.element.shouldHave(attributeMatching("class", ".*ui-state.*"));
            return true;
        } catch (AssertionError e) {
            return false;
        } finally {
            actions().release().perform();
        }
    }

    public static boolean checkImage (int number) {
        String Number = (number == 1) ? "" : String.valueOf(number);
        SelenideElement image = $("img[src='images/high_tatras" + Number + "_min.jpg']");
        try {
            image.shouldBe(visible);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    public static String imageDeleteAndRecycleTest (int number) {
        String Number = (number == 1) ? "" : String.valueOf(number);
        String xPath = ".//li[img[@src='images/high_tatras" + Number + "_min.jpg']]";
        SelenideElement image = $x(xPath);
        image.shouldBe(visible).$("a[title='Delete this image']").click();
        try {
            Trash.element.$x(xPath).shouldBe(visible);
        } catch (AssertionError e) {
            return "не удалось удалить картинку!";
        }
        image.shouldBe(visible).$("a[title='Recycle this image']").click();
        try {
            Gallery.element.$x(xPath).shouldBe(visible);
        } catch (AssertionError e) {
            return "не удалось восстановить картинку!";
        }
        return "Test passed!";
    }

    public static String imageOpenLargeAndCloseTest (int number) {
        String Number = (number == 1) ? "" : String.valueOf(number);
        SelenideElement image = $x("//li[img[@src='images/high_tatras" + Number + "_min.jpg']]");
        SelenideElement bigImage = $x("//div[img[@src='images/high_tatras" + Number + ".jpg']]");
        image.shouldBe(visible).$("a[title='View larger image']").click();
        try {
            bigImage.shouldBe(visible);
        } catch (AssertionError e) {
            return "не удалось развернуть большую картинку!";
        }
        bigImage.shouldBe(visible).$("button[title='Close']").click();
        try {
            bigImage.shouldNotBe(visible);
        } catch (AssertionError e) {
            return "не удалось закрыть большую картинку!";
        }
        return "Test passed!";
    }

}