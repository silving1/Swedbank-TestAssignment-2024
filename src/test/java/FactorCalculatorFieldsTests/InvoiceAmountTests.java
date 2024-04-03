import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class InvoiceAmountTests {

    SelenideElement errorMsg;
    FactorCalculatorTests FCTests = new FactorCalculatorTests();

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        //WebDriverRunner.getWebDriver().manage().window().maximize();
        if ($("button.ui-cookie-consent__accept-button").is(visible, Duration.ofSeconds(3))) {
            $(byText("Accept")).click();
        }

        errorMsg = $("ui-hint[type=error]");
    }

    //After each test, the website is closed and can start over with fresh test inputs
    @AfterEach
    public void tearDown() { closeWebDriver(); }

    //Different test scenarios, where user tries to input false values to Invoice Amount field
    @Test
    public void falseInvoiceAmountValues() {
        //Made a private helper method below to help me repeat each test.
        //Firstly inputs value the website doesn't like, then it should return error corresponding message
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
        //Firstly inputs value the website likes, there should be no error messages
        testValueInput("1", "");
        testValueInput("1000000000000000000000000000", "");
        testValueInput("0000000000000001", "");
        testValueInput("199.000000.000", "");
        testValueInput("199.9999999999999999", "");
    }

    //Test case, where user inputs true and false value after another
    @Test
    public void trueAndFalseInvoiceAmountValues() {
        //Tests, if the error message disappears after correct input and comes back, when inserted false input
        testValueInput("0", "Value must be greater than or equal 1.");
        testValueInput("1000", "");
        testValueInput("10+10", "Please enter a valid value.");
        testValueInput("199", "");
        testValueInput("1.001", "Step should be 0.01, nearest values are 1.00 and 1.01.");
    }

    // Test case, where user inputs false value and clicks Calculate button
    @Test
    public void falseInvoiceAmountValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();

        //Then adding user input the website doesn't like
        testValueInput("0", "Value must be greater than or equal 1.");

        //After false input, the result should show 0
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0"));
        $("#result").shouldHave(text("0"));
    }

    // Test case, where user inputs true value and clicks Calculate button
    @Test
    public void trueInvoiceAmountValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();

        //Then adding user input the website likes
        testValueInput("10000000000000000000000000000", "");

        //After correct input, the result should show the made calculation
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0.53"));
        $("#result").shouldHave(text("5.25e+25"));
    }

    // Helper method to test a value in the Invoice Amount field and validate the error message
    private void testValueInput(String value, String expectedErrorMessage) {
        //Inserts the user value to field
        $("#D5").val(value);

        //Checks, if the expected is empty
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
}