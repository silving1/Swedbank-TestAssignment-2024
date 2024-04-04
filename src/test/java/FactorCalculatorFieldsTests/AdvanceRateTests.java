import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class AdvanceRateTests extends BaseFieldsTest {

    SelenideElement id = $(By.id("D6"));

    //Test case to verify if correct values are selectable from the dropdown menu
    @Test
    public void isCorrectAdvanceRateValues() {
        id.selectOption("75"); id.shouldHave(text("75"));
        id.selectOption("80"); id.shouldHave(text("80"));
        id.selectOption("85"); id.shouldHave(text("85"));
        id.selectOption("90"); id.shouldHave(text("90"));
    }

    //Test case to verify if a random value is selectable from the dropdown menu
    @Test
    public void isFalseAdvanceRateValues() {
        //List together all elements
        ElementsCollection elementList = id.getOptions();

        //Check, if a false value is in list using helper method in BaseFieldsTest
        assertFalse(doesValueExist(elementList, "100"));
        assertFalse(doesValueExist(elementList, "0"));
        assertFalse(doesValueExist(elementList, ""));
        assertFalse(doesValueExist(elementList, "abc"));
    }

    //Test case to verify if the dropdown menu count is true
    @Test
    public void isCorrectAdvanceRateCount() {
        //List together all elements
        ElementsCollection elementList = id.getOptions();

        //Since there is only 4 options to select, it must return 4 elements
        assertEquals(4, elementList.size());

        //Checks, if there are more or less than firstly expected elements
        assertNotEquals(5, elementList.size());
        assertNotEquals(3, elementList.size());
    }
}
