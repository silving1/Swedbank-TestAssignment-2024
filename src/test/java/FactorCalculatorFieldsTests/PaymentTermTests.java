import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PaymentTermTests extends BaseFieldsTest {

    SelenideElement id = $(By.id("D8"));

    //Test case to verify if correct values are selectable from the dropdown menu
    @Test
    public void isCorrectPaymentTermValues() {
        id.selectOption("30"); id.shouldHave(text("30"));
        id.selectOption("60"); id.shouldHave(text("60"));
        id.selectOption("90"); id.shouldHave(text("90"));
        id.selectOption("120"); id.shouldHave(text("120"));
    }

    //Test case to verify if a random value is selectable from the dropdown menu
    @Test
    public void isFalsPaymentTermValues() {
        //List together all elements
        ElementsCollection elementList = id.getSelectedOptions();

        //Check, if a false value is in list using helper method in BaseFieldsTest
        assertFalse(doesValueExist(elementList, "100"));
        assertFalse(doesValueExist(elementList, "0"));
        assertFalse(doesValueExist(elementList, ""));
        assertFalse(doesValueExist(elementList, "abc"));
    }
}
