import org.junit.jupiter.api.Test;
import static com.codeborne.selenide.Condition.*;

public class InvoiceAmountTests extends BaseFieldsTest {

    FactorCalculatorTests FCTests = new FactorCalculatorTests();

    //Different test scenarios, where user tries to input false values to Invoice Amount field
    @Test
    public void falseInvoiceAmountValues() {
        //User inputs value the website doesn't like, should return corresponding error message
        //Inputs are sent to helper method in BaseFieldsTest class
        testValueInput("D5","0", "Value must be greater than or equal 1.");
        testValueInput("D5","-5000", "Value must be greater than or equal 1.");
        testValueInput("D5","abc", "Please fill out this field.");
        testValueInput("D5","-.", "Please enter a valid value.");
        testValueInput("D5"," ", "Please fill out this field.");
        testValueInput("D5","199.114", "Step should be 0.01, nearest values are 199.11 and 199.12.");
        testValueInput("D5","199.999999999", "Step should be 0.01, nearest values are 199.99 and 200.00.");
    }

    //Different test scenarios, where user tries to input unusual, but true values to Invoice Amount field
    @Test
    public void trueInvoiceAmountValues() {
        //User inputs value the website likes, there should be no error messages
        //Inputs are sent to helper method in BaseFieldsTest class
        testValueInput("D5","1", "");
        testValueInput("D5","1000000000000000000000000000", "");
        testValueInput("D5","0000000000000001", "");
        testValueInput("D5","199.000000.000", "");
        testValueInput("D5","199.9999999999999999", "");
    }

    //Test case, where user inputs true and false value after another
    @Test
    public void trueAndFalseInvoiceAmountValues() {
        //Tests, if the error message disappears after correct input and comes back, when inserted false input
        testValueInput("D5","0", "Value must be greater than or equal 1.");
        testValueInput("D5","1000", "");
        testValueInput("D5","10+10", "Please enter a valid value.");
        testValueInput("D5","199", "");
        testValueInput("D5","1.001", "Step should be 0.01, nearest values are 1.00 and 1.01.");
    }

    // Test case, where user inputs false value and clicks Calculate button
    @Test
    public void falseInvoiceAmountValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();

        //Then adding user input the website doesn't like
        testValueInput("D5","0", "Value must be greater than or equal 1.");

        //After false input, the result should show 0
        //Expected values are sent to helper method in BaseFieldsTest class
        testCalculation("0","0");
    }

    // Test case, where user inputs correct value and clicks Calculate button
    @Test
    public void trueInvoiceAmountValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesFactoringCalculatorCalculateButtonCalculate();

        //Then adding user input the website likes
        testValueInput("D5","10000000000000000000000000000", "");

        //After correct input, the result should show the made calculation
        //Expected values are sent to helper method in BaseFieldsTest class
        testCalculation("0.53","5.25e+25");
    }
}