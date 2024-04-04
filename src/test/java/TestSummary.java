import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.HashMap;
import java.util.Map;

//Im aware that I can find the test summary under the test folder in my files, but I am not sure
//thats what is needed when the task says "Create test summary". So I made this.
public class TestSummary implements TestWatcher {
    private Map<String, Boolean> testResults = new HashMap<>();

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        testResults.put(context.getDisplayName(), false);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        testResults.put(context.getDisplayName(), true);
    }

    public void printTestSummary() {
        int totalTests = testResults.size();
        int passedTests = 0;
        int failedTests = 0;

        System.out.println("Test Summary:");
        for (Map.Entry<String, Boolean> entry : testResults.entrySet()) {
            String testName = entry.getKey();
            boolean testResult = entry.getValue();
            if (testResult) {
                passedTests++;
                System.out.println("  - " + testName + ": Passed");
            } else {
                failedTests++;
                System.out.println("  - " + testName + ": Failed");
            }
        }

        System.out.println("\nTotal Tests: " + totalTests);
        System.out.println("Passed Tests: " + passedTests);
        System.out.println("Failed Tests: " + failedTests);
    }
}
