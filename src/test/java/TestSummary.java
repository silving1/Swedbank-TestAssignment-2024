import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.HashMap;
import java.util.Map;

//Im aware that I can find the test summary under the test folder in my files, but I am not sure
//thats what is needed when the task says "Create test summary". So I made this.
public class TestSummary {
    private Map<String, String> testResults = new HashMap<>();

    protected void testFailed(ExtensionContext context, Throwable cause) {
        String testName = context.getDisplayName();
        testResults.put(testName, "Failed");
    }

    protected void testSuccessful(ExtensionContext context) {
        String testName = context.getDisplayName();
        testResults.put(testName, "Passed");
    }
}
