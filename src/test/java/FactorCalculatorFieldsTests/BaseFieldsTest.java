import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public abstract class BaseFieldsTest {
    protected SelenideElement errorMsg;

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        if ($("button.ui-cookie-consent__accept-button").is(visible, Duration.ofSeconds(3))) {
            $(byText("Accept")).click();
        }
        errorMsg = $("ui-hint[type=error]");
    }

    //After each test, the website is closed and can start over with fresh test inputs
    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    // Helper method to test a value in the corresponding field and validate the error message
    protected void testValueInput(String fieldId, String value, String expectedErrorMessage) {
        $(By.id(fieldId)).val(value);
        if (!expectedErrorMessage.isEmpty()) {
            errorMsg.should(exist);
            errorMsg.shouldBe(visible);
            errorMsg.shouldHave(text(expectedErrorMessage));
        } else {
            errorMsg.shouldNot(exist);
        }
    }

    protected void testCalculation(String expectedResultPerc, String expectedResult) {
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text(expectedResultPerc));
        $("#result").shouldHave(text(expectedResult));
    }
}