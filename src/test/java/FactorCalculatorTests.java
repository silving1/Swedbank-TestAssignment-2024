import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Test;

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
}