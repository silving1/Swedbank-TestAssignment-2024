import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
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
        //$(byText("Accept")).click();
        $("ui-calculator").shouldBe(visible);
        $("Factoring").shouldBe(visible);
    }
}