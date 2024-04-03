import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactorCalculatorTests {

    @Test
    public void isFactoringCalculatorUrlExists() {
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        String url = WebDriverRunner.url();
        assertEquals(url, "https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
    }
    @Test
    public void isFactoringCalculatorExists() {
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        $("h1").shouldHave(text("Factoring"));
        $("ui-calculator").shouldBe(visible);
    }
    @Test
    public void isFactoringCalculatorFieldsExists() {
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        $(byText("Accept")).click();

        $("#D5").should(exist); $("#D5").shouldBe(visible);
        $("#D7").should(exist); $("#D7").shouldBe(visible);
        $("#D9").should(exist); $("#D9").shouldBe(visible);

        $("#D6").should(exist); $("#D6").shouldBe(visible);
        $("#D8").should(exist); $("#D8").shouldBe(visible);
    }
    @Test
    public void canAddValuesToFactoringCalculatorFields() {
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        $(byText("Accept")).click();

        $("#D5").val("9000");
        $("#D7").val("15");
        $("#D9").val("1");

        $("#D6").selectOption("80");
        $("#D8").selectOption("60");
    }
    @Test
    public void isFactoringCalculatorCalculateButtonExists() {
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        $(byText("Accept")).click();

        $("#calculate-factoring").should(exist); $("#calculate-factoring").shouldBe(visible);
        $("#result_perc").should(exist); $("#result_perc").shouldBe(visible);
        $("#result_perc").shouldHave(text("0"));
        $("#result").should(exist); $("#result").shouldBe(visible);
        $("#result").shouldHave(text("0.00"));
    }
    @Test
    public void doesFactoringCalculatorCalculateButtonCalculate() {
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        $(byText("Accept")).click();

        $(By.id("calculate-factoring")).click();
        $("#result_perc").shouldHave(text("0.53"));
        $("#result").shouldHave(text("52.50"));
    }
}