import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvoiceAmountTests {

    SelenideElement errorMsg;

    //For each test, the factoring calculator website is opened and cookies accepted for visual purposes
    @BeforeEach
    public void setUp() {
        Configuration.browser = "chrome";
        open("https://www.swedbank.lt/business/finance/trade/factoring?language=ENG");
        //WebDriverRunner.getWebDriver().manage().window().maximize();
        if($("button.ui-cookie-consent__accept-button").is(visible)){
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
        $("ui-hint[type=error]").shouldNot(exist);

        $("#D5").val("0");
        $("ui-hint[type=error]").should(exist); $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Value must be greater than or equal 1."));

        $("#D5").val("-5000");
        $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Value must be greater than or equal 1."));

        $("#D5").val("abc");
        $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Please fill out this field."));

        $("#D5").val("-.");
        $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Please enter a valid value."));

        $("#D5").val(" ");
        $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Please fill out this field."));

        $("#D5").val("199.114");
        $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Step should be 0.01, nearest values are 199.11 and 199.12."));

        $("#D5").val("199.999999999");
        $("ui-hint[type=error]").shouldBe(visible);
        $("ui-hint[type=error]").shouldHave(text("Step should be 0.01, nearest values are 199.99 and 200.00."));
    }

}
