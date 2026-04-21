package com.b2c2.automation.stepdefs;

import com.b2c2.automation.hooks.WebDriverHooks;
import com.b2c2.automation.pages.AccessibilityHelper;
import com.deque.html.axecore.results.Rule;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccessibilitySteps {

    private AccessibilityHelper a11yHelper;

    private void initHelper() {
        if (a11yHelper == null) {
            WebDriver driver = WebDriverHooks.getDriver();
            a11yHelper = new AccessibilityHelper(driver);
        }
    }

    @Then("the page should have no critical or serious accessibility violations beyond known issues:")
    public void thePageShouldHaveNoViolationsBeyondKnownIssues(DataTable knownIssuesTable) {
        initHelper();
        Set<String> knownRuleIds = knownIssuesTable.asMaps().stream()
                .map(row -> row.get("ruleId"))
                .collect(Collectors.toSet());

        List<Rule> allViolations = a11yHelper.getCriticalAndSeriousViolations();

        // Log all found violations for reporting purposes
        if (!allViolations.isEmpty()) {
            String report = allViolations.stream()
                    .map(v -> v.getId() + " (" + v.getImpact() + "): " + v.getDescription()
                            + (knownRuleIds.contains(v.getId()) ? " [KNOWN ISSUE]" : " [NEW]"))
                    .collect(Collectors.joining("\n  - ", "\n  - ", ""));
            System.out.println("[A11Y REPORT] Violations found:" + report);
        }

        // Filter to only unexpected (new) violations
        List<Rule> newViolations = allViolations.stream()
                .filter(v -> !knownRuleIds.contains(v.getId()))
                .collect(Collectors.toList());

        String summary = newViolations.stream()
                .map(v -> v.getId() + " (" + v.getImpact() + "): " + v.getDescription())
                .collect(Collectors.joining("\n  - ", "\n  - ", ""));
        assertTrue(newViolations.isEmpty(),
                "Found " + newViolations.size() + " NEW critical/serious a11y violations "
                        + "(not in known issues list):" + summary);
    }
}
