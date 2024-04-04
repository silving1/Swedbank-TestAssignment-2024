import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;

public class CommissionFeeTests extends BaseFieldsTest {

    //Different test scenarios, where user tries to input false values to Commission Fee field
    @Test
    public void falseCommissionFeeValues() {
        //User inputs value the website doesn't like, should return corresponding error message
        //Inputs are sent to helper method in BaseFieldsTest class
        testValueInput("D9", "abc", "Please fill out this field.");
        testValueInput("D9","-.", "Please enter a valid value.");
        testValueInput("D9"," ", "Please fill out this field.");
        testValueInput("D9","199.114", "Step should be 0.01, nearest values are 199.11 and 199.12.");
        testValueInput("D9","199.999999999", "Step should be 0.01, nearest values are 199.99 and 200.00.");
    }

    //Different test scenarios, where user tries to input unusual, but true values to Commission Fee field
    @Test
    public void trueCommissionFeeValues() {
        //User inputs value the website likes, there should be no error messages
        testValueInput("D9","0", "");
        testValueInput("D9","-100", "");
        testValueInput("D9","1000000000000000000000000000", "");
        testValueInput("D9","0000000000000001", "");
        testValueInput("D9","199.000000.000", "");
        testValueInput("D9","199.9999999999999999", "");
    }

    //Test case, where user inputs true and false value after another
    @Test
    public void trueAndFalseCommissionFeeValues() {
        //Tests, if the error message disappears after correct input and comes back, when inserted false input
        testValueInput("D9","aaa", "Please fill out this field.");
        testValueInput("D9","1000", "");
        testValueInput("D9","10+10", "Please enter a valid value.");
        testValueInput("D9","-199", "");
        testValueInput("D9","1.001", "Step should be 0.01, nearest values are 1.00 and 1.01.");
    }

    // Test case, where user inputs false value and clicks Calculate button
    @Test
    public void falseCommissionFeeValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        doesCalculateButtonCalculate();

        //Then adding input the website doesn't like
        testValueInput("D9","aaa", "Please fill out this field.");

        //After false input, the result should show 0
        //Expected values are sent to helper method in BaseFieldsTest class
        testCalculation("0","0");
    }

    // Test case, where user inputs correct value and clicks Calculate button
    @Test
    public void trueCommissionFeeValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        doesCalculateButtonCalculate();

        //Then adding input the website likes
        testValueInput("D9","-8888", "");

        //After false input, the result should show 0
        //Expected values are sent to helper method in BaseFieldsTest class
        testCalculation("-8887.77","-888777.50");
    }
}
