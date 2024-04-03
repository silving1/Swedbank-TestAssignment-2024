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

public class InterestRateTests {
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

    //Different test scenarios, where user tries to input false values to Interest Rate field
    @Test
    public void falseInterestRateValues() {
        //Made a private helper method below to help me repeat each test.
        //Firstly inputs value the website doesn't like, then it should return error corresponding message
        testValueInput("-5000", "Value must be greater than or equal 0.");
        testValueInput("abc", "Please fill out this field.");
        testValueInput("-.", "Please enter a valid value.");
        testValueInput(" ", "Please fill out this field.");
        testValueInput("19.99999", "Step should be 0.01, nearest values are 19.99 and 20.00.");
        testValueInput("20.0000000000000001", "Value must be less than or equal 20.");
    }

    //Different test scenarios, where user tries to input unusual, but true values to Interest Rate field
    @Test
    public void trueInterestRateValues() {
        //Firstly inputs value the website likes, there should be no error messages
        testValueInput("0", "");
        testValueInput("0000000000000001", "");
        testValueInput("19.0000.000", "");
        testValueInput("20.0000000000000000001", "");
    }

    //Test case, where user inputs true and false value after another
    @Test
    public void trueAndFalseInterestRateValues() {
        //Tests, if the error message disappears after correct input and comes back, when inserted false input
        testValueInput("-1", "Value must be greater than or equal 0.");
        testValueInput("10", "");
        testValueInput("10+10", "Please enter a valid value.");
        testValueInput("20", "");
        testValueInput("1.001", "Step should be 0.01, nearest values are 1.00 and 1.01.");
    }

    // Test case, where user inputs false value and clicks Calculate button
    @Test
    public void falseInterestRateValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();

        //Then adding user input the website doesn't like
        testValueInput("-10", "Value must be greater than or equal 0.");

        //After false input, the result should show 0
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0"));
        $("#result").shouldHave(text("0"));
    }

    // Test case, where user inputs true value and clicks Calculate button
    @Test
    public void trueInterestRateValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();

        //Then adding user input the website likes
        testValueInput("0", "");

        //After correct input, the result should show the made calculation
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0.30"));
        $("#result").shouldHave(text("30.00"));
    }

    // Helper method to test a value in the Interest Rate field and validate the error message
    private void testValueInput(String value, String expectedErrorMessage) {
        //Inserts the user value to field
        $("#D7").val(value);

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
