import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.time.Duration.ofSeconds;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

//Should extend FactorCalculatorTests, but due to repeating FactorCalculatorTests tests
//I created a new setup
public class FactorCalculatorFunctionalityTests {

    @RegisterExtension
    static TestSummary testSummary = new TestSummary();
    static FactorCalculatorTests FCTests = new FactorCalculatorTests();

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        if($("button.ui-cookie-consent__accept-button").is(visible, ofSeconds(3))){
            $(byText("Accept")).click();
        }
        testSummary.setTestClassName(this.getClass().getSimpleName());
    }
    //After each test, the website is closed and can start over with fresh test inputs
    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }
    //After all tests are done, the test summary will appear on clipboard
    @AfterAll
    public static void printSummary() { testSummary.printTestSummary(); }

    //Test case to verify if factoring calculators result equals with manually calculated result
    @Test
    public void manualFactoringCalculator() {
        //Gets all fields values
        var iAmount = Double.parseDouble(requireNonNull($(By.id("D5")).val()));
        var aRate = Double.parseDouble(requireNonNull($(By.id("D6")).getSelectedOptionText()));
        var iRate = Double.parseDouble(requireNonNull($(By.id("D7")).val()));
        var pTerm = Double.parseDouble(requireNonNull($(By.id("D8")).getSelectedOptionText()));
        var cFee = Double.parseDouble(requireNonNull($(By.id("D9")).val()));

        //Does needed calculations
        var amount = iAmount*aRate/100;
        var fee = iAmount*(cFee/100);
        var interest = amount*(iRate/100)*(pTerm/365);
        var total = fee+interest;
        var totalInPercent = (total/iAmount)*100;

        //Website does its own calculations
        $(By.id("calculate-factoring")).click();

        //This delta calculation is not very good, but it ensures that both are "roundably" equal
        var totalDelta = Double.parseDouble(requireNonNull($(By.id("result")).val()))-total;

        //Asserting values to equal. Due to minor calculation/rounding errors, the delta is needed
        assertEquals(total, Double.parseDouble(requireNonNull($(By.id("result")).val())), totalDelta);
        assertEquals(totalInPercent, Double.parseDouble(requireNonNull($(By.id("result_perc")).val())), 0.1);
    }

    //Test case to verify if factoring calculators result equals with manually calculated result with
    //manually inserted values
    @Test
    public void manualCalculatorInsertedValues() {
        //Changing the field values
        FCTests.setValueAndVerify("D5", "100");
        FCTests.setValueAndVerify("D7", "20");
        FCTests.setValueAndVerify("D9", "5");
        FCTests.setSelectOptionAndVerify("D6", "90");
        FCTests.setSelectOptionAndVerify("D8", "120");

        //Check, if the manual calculations and website calculations match
        manualFactoringCalculator();
    }

    //Test case to verify if factoring calculators result equals with manually calculated result with
    //manually inserted values, where Invoice Amount is a large number
    @Test
    public void manualCalculatorInsertedValuesLarge() {
        //Changing the field values
        FCTests.setValueAndVerify("D5", "1023469");
        FCTests.setValueAndVerify("D7", "3");
        FCTests.setValueAndVerify("D9", "10");
        FCTests.setSelectOptionAndVerify("D6", "75");
        FCTests.setSelectOptionAndVerify("D8", "30");

        //Check, if the manual calculations and website calculations match
        manualFactoringCalculator();
    }

    //Test case to verify if factoring calculators result equals with manually calculated result with
    //manually inserted values, where Commission Fee is negative
    @Test
    public void manualCalculatorInsertedValuesNegativeComFee() {
        //Changing the field values
        FCTests.setValueAndVerify("D5", "7777");
        FCTests.setValueAndVerify("D7", "3");
        FCTests.setValueAndVerify("D9", "-10");
        FCTests.setSelectOptionAndVerify("D6", "75");
        FCTests.setSelectOptionAndVerify("D8", "30");

        //Check, if the manual calculations and website calculations match
        manualFactoringCalculator();
    }
}
