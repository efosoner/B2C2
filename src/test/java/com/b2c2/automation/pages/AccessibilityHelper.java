package com.b2c2.automation.pages;

import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;

public class AccessibilityHelper {

    private final WebDriver driver;

    public AccessibilityHelper(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Runs axe-core accessibility scan and returns violations at critical and serious impact levels.
     */
    public List<Rule> getCriticalAndSeriousViolations() {
        Results results = new AxeBuilder().analyze(driver);
        return results.getViolations().stream()
                .filter(v -> "critical".equals(v.getImpact()) || "serious".equals(v.getImpact()))
                .collect(Collectors.toList());
    }

    /**
     * Runs axe-core and returns all violations.
     */
    public List<Rule> getAllViolations() {
        Results results = new AxeBuilder().analyze(driver);
        return results.getViolations();
    }
}
