package com.b2c2.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(3);

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public void open(String url) {
        driver.get(url);
    }

    /**
     * Scrolls the page so the given element is centered in the viewport.
     * Uses element presence (not visibility) to support hidden elements like custom selects.
     */
    protected void scrollToElement(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((org.openqa.selenium.JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", el);
    }

    public WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public String getText(By locator) {
        return waitForElement(locator).getText();
    }

    public boolean isElementDisplayed(By locator) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, SHORT_TIMEOUT);
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getMetaContent(String nameOrProperty) {
        By selector = By.cssSelector(
                "meta[name='" + nameOrProperty + "'], meta[property='" + nameOrProperty + "']");
        wait.until(ExpectedConditions.presenceOfElementLocated(selector));
        return driver.findElement(selector).getDomAttribute("content");
    }
}
