import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorCalculatorTests {

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        WebDriverRunner.getWebDriver().manage().window().maximize();
        if($("button.ui-cookie-consent__accept-button").is(visible)){
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
        assertEquals(url, "https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
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
    public void isFactoringCalculatorFieldsExists() {
        //Each field is searched after its id, verifies that its on the website and is visible
        //for the user
        $("#D5").should(exist); $("#D5").shouldBe(visible);
        $("#D7").should(exist); $("#D7").shouldBe(visible);
        $("#D9").should(exist); $("#D9").shouldBe(visible);

        //Same for dropdown menu items
        $("#D6").should(exist); $("#D6").shouldBe(visible);
        $("#D8").should(exist); $("#D8").shouldBe(visible);
    }

    //Test case to verify if the user can interact with the calculators fields
    @Test
    public void canAddValuesToFactoringCalculatorFields() {
        //Each fields value is inserted after its id, the added values are all acceptable
        $("#D5").val("9000");
        $("#D7").val("15");
        $("#D9").val("1");

        $("#D6").selectOption("80");
        $("#D8").selectOption("60");
    }

    //Test case to verify if Factoring Calculator has calculation button and shows results
    @Test
    public void isFactoringCalculatorCalculateButtonExists() {
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
    public void doesFactoringCalculatorCalculateButtonCalculate() {
        //Calculate button is clicked by its id, results are shown by their ids
        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0.53"));
        $("#result").shouldHave(text("52.50"));
    }
}