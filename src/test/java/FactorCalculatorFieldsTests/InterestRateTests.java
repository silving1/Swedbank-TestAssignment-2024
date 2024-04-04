import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;

public class InterestRateTests extends BaseFieldsTest {

    //Different test scenarios, where user tries to input false values to Interest Rate field
    @Test
    public void falseInterestRateValues() {
        //User inputs value the website doesn't like, should return corresponding error message
        //Inputs are sent to helper method in BaseFieldsTest class
        testValueInput("D7","-5000", "Value must be greater than or equal 0.");
        testValueInput("D7","abc", "Please fill out this field.");
        testValueInput("D7","-.", "Please enter a valid value.");
        testValueInput("D7"," ", "Please fill out this field.");
        testValueInput("D7","19.99999", "Step should be 0.01, nearest values are 19.99 and 20.00.");
        testValueInput("D7","20.0000000000000001", "Value must be less than or equal 20.");
    }

    //Different test scenarios, where user tries to input unusual, but true values to Interest Rate field
    @Test
    public void trueInterestRateValues() {
        //User inputs value the website likes, there should be no error message
        //Inputs are sent to helper method in BaseFieldsTest class
        testValueInput("D7","0", "");
        testValueInput("D7","0000000000000001", "");
        testValueInput("D7","19.0000.000", "");
        testValueInput("D7","20.0000000000000000001", "");
    }

    //Test case, where user inputs true and false value after another
    @Test
    public void trueAndFalseInterestRateValues() {
        //Checks, if the error message disappears after correct input and comes back, when inserted false input
        testValueInput("D7","-1", "Value must be greater than or equal 0.");
        testValueInput("D7","10", "");
        testValueInput("D7","10+10", "Please enter a valid value.");
        testValueInput("D7","20", "");
        testValueInput("D7","1.001", "Step should be 0.01, nearest values are 1.00 and 1.01.");
    }

    // Test case, where user inputs false value and clicks Calculate button
    @Test
    public void falseInterestRateValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesCalculateButtonCalculate();

        //Then adding input the website doesn't like
        testValueInput("D7","-10", "Value must be greater than or equal 0.");

        //After false input, the result should show 0
        //Expected values are sent to helper method in BaseFieldsTest class
        testCalculation("0", "0");
    }

    // Test case, where user inputs correct value and clicks Calculate button
    @Test
    public void trueInterestRateValueAndCalculate() {
        //Firstly checks, if initial calculation is done
        errorMsg.shouldNot(exist);
        FCTests.doesCalculateButtonCalculate();

        //Then adding input the website likes
        testValueInput("D7","0", "");

        //After correct input, the result should show the made calculation
        //Expected values are sent to helper method in BaseFieldsTest class
        testCalculation("0.30", "30.00");
    }
}
