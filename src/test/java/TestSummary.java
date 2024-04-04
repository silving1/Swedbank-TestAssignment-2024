import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.HashMap;
import java.util.Map;

//I'm aware that I can find the test summary under the test folder in my files, but I am not sure
//that's' what is needed when the task says "Create test summary". So I made this.
public class TestSummary implements TestWatcher {

    //Save class name for better understanding
    private String testClassName;
    //Binds together test name and its result, true for passed, false for failed
    private final Map<String, Boolean> testResults = new HashMap<>();

    //Overrides TestWatcher, helps test summaries simple to make
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        testResults.put(context.getDisplayName(), false);
    }
    @Override
    public void testSuccessful(ExtensionContext context) {
        testResults.put(context.getDisplayName(), true);
    }

    //Saves class name as string
    public void setTestClassName(String testClassName) { this.testClassName = testClassName; }

    //Callable method, that creates a summary for tester
    public void printTestSummary() {
        //Finds total, calculates passed and failed tests
        int totalTests = testResults.size();
        int passedTests = (int) testResults.values().stream().filter(Boolean::booleanValue).count();
        int failedTests = totalTests - passedTests;

        //Prints out each test unit
        System.out.println(testClassName + " Test Summary:");
        testResults.forEach((testName, testResult) -> {
            if (testResult) System.out.println("  - " + testName + ": Passed");
            else System.out.println("  - " + testName + ": Failed");
        });

        //Prints out total test summary
        System.out.println("\nTotal Tests: " + totalTests);
        System.out.println("Passed Tests: " + passedTests);
        System.out.println("Failed Tests: " + failedTests+"\n");

        testResults.clear();
    }
}
