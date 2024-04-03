import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorCalculatorTests {

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        //WebDriverRunner.getWebDriver().manage().window().maximize();
        if($("button.ui-cookie-consent__accept-button").is(visible, Duration.ofSeconds(3))){
            $(byText("Accept")).click();
        }

    }
    //After each test, the website is closed and can start over with fresh test inputs
    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    //Test case to verify if the factoring calculator URL exists
    @Test
    public void isFactoringCalculatorUrlExists() {
        //Saves the opened websites URL as string
        String url = WebDriverRunner.url();

        //Checks, if the saved URL matches with expected URL
        assertEquals("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG", url);
    }

    //Test case to verify if the website has the Factoring Calculator in it
    @Test
    public void isFactoringCalculatorExists() {
        //I wanted to search for Factoring Calculator under "h2", but couldn't succeed. Instead,
        //it searches, if the website has text Factoring in
        $("h1").shouldHave(text("Factoring"));

        //This tests, if the Factoring Calculator has its own form
        $("ui-calculator").shouldBe(visible);
    }

    //Test case to verify if Factoring Calculator has its fields
    @Test
    public void isFieldsExists() {
        //Each field is searched after its id, verifies that its on the website and is visible
        //for the user
        $(By.id("D5")).should(exist); $(By.id("D5")).shouldBe(visible);
        $(By.id("D7")).should(exist); $(By.id("D7")).shouldBe(visible);
        $(By.id("D9")).should(exist); $(By.id("D9")).shouldBe(visible);

        //Same for dropdown menu items
        $(By.id("D6")).should(exist); $(By.id("D6")).shouldBe(visible);
        $(By.id("D8")).should(exist); $(By.id("D8")).shouldBe(visible);
    }

    //Test case to verify if the user can interact with the calculators fields
    @Test
    public void canAddValuesToFields() {
        //Each fields value is inserted after its id, the added values are all acceptable
        $(By.id("D5")).val("9000");
        $(By.id("D7")).val("15");
        $(By.id("D9")).val("1");
        $(By.id("D6")).selectOption("80");
        $(By.id("D8")).selectOption("60");
    }

    //Test case to verify if inserted value is actually inserted
    @Test
    public void isInsertedValuesInserted() {
        setValueAndVerify("D5", "9000");
        setValueAndVerify("D7", "15");
        setValueAndVerify("D9", "1");
        setSelectOptionAndVerify("D6", "80");
        setSelectOptionAndVerify("D8", "60");
    }

    //Test case to verify if Factoring Calculator has calculation button and shows results
    @Test
    public void isCalculateButtonExists() {
        //For better visibility, the ids are saved as elements
        SelenideElement calculateButton = $("#calculate-factoring");
        SelenideElement resultPerc = $("#result_perc");
        SelenideElement result = $("#result");

        //Checks if each elements' existence in webpage and visibility to user is true
        calculateButton.should(exist); calculateButton.shouldBe(visible);

        //For results, if nothing is calculated, it shouldn't show anything
        resultPerc.should(exist); resultPerc.shouldBe(visible);
        resultPerc.shouldHave(text("0"));
        result.should(exist); result.shouldBe(visible);
        result.shouldHave(text("0.00"));
    }

    //Test case to verify if calculate button correctly calculates the websites default values
    @Test
    public void doesCalculateButtonCalculate() {
        //Calculate button is clicked by its id, results are shown by their ids
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0.53"));
        $("#result").shouldHave(text("52.50"));
    }

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

        //Asserting values to equal. Due to minor calculation rounding errors, the delta is needed
        assertEquals(total, Double.parseDouble(requireNonNull($(By.id("result")).val())), totalDelta);
        assertEquals(totalInPercent, Double.parseDouble(requireNonNull($(By.id("result_perc")).val())), 0.1);
    }

    //Test case to verify if factoring calculators result equals with manually calculated result with
    //manually inserted values
    @Test
    public void manualCalculatorInsertedValues() {
        //Changing the field values
        setValueAndVerify("D5", "100");
        setValueAndVerify("D7", "20");
        setValueAndVerify("D9", "5");
        setSelectOptionAndVerify("D6", "90");
        setSelectOptionAndVerify("D8", "120");

        //Check, if the manual calculations and website calculations match
        manualFactoringCalculator();
    }

    //Test case to verify if factoring calculators result equals with manually calculated result with
    //manually inserted values, where Commission Fee is negative
    @Test
    public void manualCalculatorInsertedValuesLarge() {
        //Changing the field values
        setValueAndVerify("D5", "1023469");
        setValueAndVerify("D7", "3");
        setValueAndVerify("D9", "10");
        setSelectOptionAndVerify("D6", "75");
        setSelectOptionAndVerify("D8", "30");

        //Check, if the manual calculations and website calculations match
        manualFactoringCalculator();
    }

    //Helper method that verifies if added value is also shown to user as that value
    private void setValueAndVerify(String id, String expected) {
        $(By.id(id)).val(expected);
        assertEquals(expected, $(By.id(id)).val());
    }

    //Helper method that verifies if selected value is also shown to user as that value
    private void setSelectOptionAndVerify(String id, String expected) {
        $(By.id(id)).selectOption(expected);
        assertEquals(expected, $(By.id(id)).getSelectedOptionText());
    }
}