import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceAmountTests {

    SelenideElement errorMsg;
    FactorCalculatorTests FCTests = new FactorCalculatorTests();

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        //WebDriverRunner.getWebDriver().manage().window().maximize();
        if ($("button.ui-cookie-consent__accept-button").is(visible)) {
            $(byText("Accept")).click();
        }

        errorMsg = $("ui-hint[type=error]");
    }

    //After each test, the website is closed and can start over with fresh test inputs
    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    //Different test scenarios, where user tries to input false values to Invoice Amount field
    @Test
    public void falseInvoiceAmountValues() {
        testValueInput("0", "Value must be greater than or equal 1.");
        testValueInput("-5000", "Value must be greater than or equal 1.");
        testValueInput("abc", "Please fill out this field.");
        testValueInput("-.", "Please enter a valid value.");
        testValueInput(" ", "Please fill out this field.");
        testValueInput("199.114", "Step should be 0.01, nearest values are 199.11 and 199.12.");
        testValueInput("199.999999999", "Step should be 0.01, nearest values are 199.99 and 200.00.");
    }

    //Different test scenarios, where user tries to input unusual, but true values to Invoice Amount field
    @Test
    public void trueInvoiceAmountValues() {
        testValueInput("1", "");
        testValueInput("1000000000000000000000000000", "");
        testValueInput("0000000000000001", "");
        testValueInput("199.000000.000", "");
        testValueInput("199.9999999999999999", "");
    }
    @Test
    public void trueAndFalseInvoiceAmountValues() {
        testValueInput("0", "Value must be greater than or equal 1.");
        testValueInput("1000", "");
        testValueInput("10+10", "Please enter a valid value.");
        testValueInput("199", "");
        testValueInput("1.001", "Step should be 0.01, nearest values are 1.00 and 1.01.");
    }

    // Test case, where user inputs false value and clicks Calculate button
    @Test
    public void falseInvoiceAmountValueAndCalculate() {
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();
        testValueInput("0", "Value must be greater than or equal 1.");

        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0"));
        $("#result").shouldHave(text("0"));
    }

    // Method to test a value in the Invoice Amount field and validate the error message
    private void testValueInput(String value, String expectedErrorMessage) {
        $("#D5").val(value);
        if (!expectedErrorMessage.isEmpty()) {
            errorMsg.should(exist);
            errorMsg.shouldBe(visible);
            errorMsg.shouldHave(text(expectedErrorMessage));
        } else {
            errorMsg.shouldNot(exist);
        }
    }
}