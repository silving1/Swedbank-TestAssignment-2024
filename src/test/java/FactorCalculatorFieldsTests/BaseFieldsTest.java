import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

//Theoretically, this class should extend FactorCalculatorTests, because before every fields test,
//the base calculator tests must be passed. For example, cannot continue with field test if field
//doesn't exist. But due to excessive testing, this can't be done.
@ExtendWith(TestSummary.class)
public abstract class BaseFieldsTest{

    @RegisterExtension
    static TestSummary testSummary = new TestSummary();
    FactorCalculatorTests FCTests = new FactorCalculatorTests();
    protected SelenideElement errorMsg;

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        if($("button.ui-cookie-consent__accept-button").is(visible, Duration.ofSeconds(3))){
            $(byText("Accept")).click();
        }
        errorMsg = $("ui-hint[type=error]");
    }
    //After each test, the website is closed and can start over with fresh test inputs
    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
    //After all tests are done, the test summary will appear on clipboard
    @AfterAll
    public static void printSummary() { testSummary.printTestSummary(); }

    // Helper method to test a value in the corresponding field and validate the error message
    protected void testValueInput(String fieldId, String value, String expectedErrorMessage) {
        //Inserts the user value to field
        $(By.id(fieldId)).val(value);

        //Checks, if the expected error message is empty
        if (!expectedErrorMessage.isEmpty()) {
            //The expected error message is not empty. The system confirms, if the expectation is correct
            errorMsg.should(exist);
            errorMsg.shouldBe(visible);
            errorMsg.shouldHave(text(expectedErrorMessage));
        } else {
            //The expected error message is empty. The system confirms, if the expectation is correct
            errorMsg.shouldNot(exist);
        }
    }

    //Helper method, which shows the made calculation
    protected void testCalculation(String expectedResultPerc, String expectedResult) {
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text(expectedResultPerc));
        $("#result").shouldHave(text(expectedResult));
    }

    //Helper method to loop each checkable value through the list
    protected boolean doesValueExist(ElementsCollection elementList, String valueToCheck) {
        for (SelenideElement element : elementList) {
            if (element.equals(valueToCheck)) return true;
        }
        return false;
    }
}