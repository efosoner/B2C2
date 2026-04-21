package com.b2c2.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HeaderComponent extends BasePage {

    // Navigation dropdowns
    private static final By NAV_DROPDOWNS = By.cssSelector(".nav-menu_inner_wrap .w-dropdown");
    private static final By NAV_MENU_LINKS = By.cssSelector(".nav-menu_inner_wrap .w-dropdown .link-8");

    // Header logo
    private static final By LOGO_LINK = By.cssSelector("a.brand");
    private static final By LOGO_IMAGE = By.cssSelector("a.brand img.logo, a.brand img");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    /**
     * Returns the top-level navigation menu names (Solutions, About, etc.).
     * Deduplicates because desktop + mobile nav may both be present in the DOM.
     */
    public List<String> getNavigationMenuNames() {
        wait.until(ExpectedConditions.presenceOfElementLocated(NAV_MENU_LINKS));
        List<WebElement> links = driver.findElements(NAV_MENU_LINKS);
        return links.stream()
                .map(el -> el.getDomProperty("textContent").trim())
                .filter(t -> !t.isEmpty())
                .distinct()
                .toList();
    }

    /**
     * Returns the sub-item labels inside a specific navigation dropdown.
     */
    public List<String> getDropdownItems(String menuName) {
        WebElement wrapper = findDropdownWrapper(menuName);
        List<WebElement> subLinks = wrapper.findElements(By.cssSelector(".w-dropdown-link"));
        return subLinks.stream()
                .map(el -> el.getDomProperty("textContent").trim().replace('\u00A0', ' '))
                .filter(t -> !t.isEmpty())
                .toList();
    }

    /**
     * Hovers over a navigation dropdown to open it using real mouse interaction.
     * Falls back to synthetic pointer events if Actions API doesn't trigger Webflow in headless.
     */
    public void hoverOverNavDropdown(String menuName) {
        scrollToElement(NAV_MENU_LINKS);
        waitForWebflowReady();
        WebElement wrapper = findDropdownWrapper(menuName);
        WebElement toggle = wrapper.findElement(By.cssSelector(".w-dropdown-toggle"));

        if (wrapper.getSize().getWidth() > 0) {
            new Actions(driver)
                    .moveToElement(driver.findElement(By.tagName("body")), 0, 0)
                    .pause(Duration.ofMillis(100))
                    .moveToElement(toggle)
                    .pause(Duration.ofMillis(300))
                    .perform();
        }

        // Fallback: synthetic pointer events if Webflow didn't respond to the Actions hover
        if (!isDropdownOpen(wrapper)) {
            dispatchPointerEvents(wrapper, toggle, "pointerenter", "mouseenter", "mouseover");
        }

        waitForDropdownOpen(wrapper);
    }

    public void moveAwayFromNavDropdown(String menuName) {
        WebElement wrapper = findDropdownWrapper(menuName);
        WebElement toggle = wrapper.findElement(By.cssSelector(".w-dropdown-toggle"));

        new Actions(driver)
                .moveToElement(driver.findElement(By.tagName("body")), 10, 10)
                .perform();

        if (isDropdownOpen(wrapper)) {
            dispatchPointerEvents(wrapper, toggle, "pointerleave", "mouseleave", "mouseout");
        }
        waitForDropdownClosed(wrapper);
    }

    public boolean isNavDropdownPanelVisible(String menuName) {
        WebElement wrapper = findDropdownWrapper(menuName);
        return isDropdownOpen(wrapper);
    }

    /**
     * Returns dropdown item texts that are currently visible in the opened dropdown.
     */
    public List<String> getVisibleDropdownItems(String menuName) {
        WebElement wrapper = findDropdownWrapper(menuName);
        List<WebElement> links = wrapper.findElements(By.cssSelector(".w-dropdown-link"));
        return links.stream()
                .filter(WebElement::isDisplayed)
                .map(el -> el.getDomProperty("textContent").trim().replace('\u00A0', ' '))
                .filter(t -> !t.isEmpty())
                .toList();
    }

    /**
     * Clicks a navigation button by its visible text and waits for navigation to complete.
     */
    public void clickNavButton(String buttonText) {
        String originalUrl = driver.getCurrentUrl();
        By buttonLocator = By.xpath(
                "//div[contains(@class,'nav-menu_inner_wrap')]//a[normalize-space()='" + buttonText + "']");
        WebElement button = wait.until(ExpectedConditions.presenceOfElementLocated(buttonLocator));
        new Actions(driver).moveToElement(button).click().perform();
        wait.until(d -> !d.getCurrentUrl().equals(originalUrl));
    }

    public String getLogoHref() {
        wait.until(ExpectedConditions.presenceOfElementLocated(LOGO_LINK));
        List<WebElement> logos = driver.findElements(LOGO_LINK);
        // Prefer the visible brand link (Webflow has mobile + desktop variants)
        for (WebElement logo : logos) {
            if (logo.isDisplayed()) {
                String href = logo.getDomProperty("href");
                return href != null ? href : "";
            }
        }
        // Fallback: return first one's href
        String href = logos.get(0).getDomProperty("href");
        return href != null ? href : "";
    }

    /**
     * Checks whether at least one logo image inside a.brand is visible.
     * Webflow renders two brand links (mobile + desktop) and hides one via CSS;
     * we check all matches and return true if any is displayed or has loaded content.
     */
    public boolean isLogoImageVisible() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(LOGO_IMAGE));
            List<WebElement> images = driver.findElements(LOGO_IMAGE);
            for (WebElement img : images) {
                if (img.isDisplayed()) return true;
            }
            // Fallback: check if any logo image has loaded (naturalWidth > 0)
            for (WebElement img : images) {
                Object loaded = ((JavascriptExecutor) driver).executeScript(
                        "return arguments[0].naturalWidth > 0;", img);
                if (Boolean.TRUE.equals(loaded)) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Private helpers ---

    /**
     * Finds the Webflow .w-dropdown wrapper for a given menu name.
     * Prefers visible (non-zero-size) wrappers to avoid matching hidden mobile nav duplicates.
     */
    private WebElement findDropdownWrapper(String menuName) {
        List<WebElement> dropdowns = driver.findElements(NAV_DROPDOWNS);
        WebElement fallback = null;
        for (WebElement dd : dropdowns) {
            WebElement link = dd.findElement(By.cssSelector(".link-8"));
            String text = link.getDomProperty("textContent").trim();
            if (text.equals(menuName)) {
                if (dd.getSize().getWidth() > 0 && dd.getSize().getHeight() > 0) {
                    return dd;
                }
                if (fallback == null) {
                    fallback = dd;
                }
            }
        }
        if (fallback != null) return fallback;
        throw new IllegalArgumentException("Navigation dropdown not found: " + menuName);
    }

    /** Waits for Webflow's JS framework to initialize (no Selenium equivalent). */
    private void waitForWebflowReady() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .withMessage("Webflow JS did not initialize")
                .until(d -> Boolean.TRUE.equals(
                        ((JavascriptExecutor) d).executeScript(
                                "return typeof window.Webflow !== 'undefined';")));
    }

    private boolean isDropdownOpen(WebElement wrapper) {
        try {
            WebElement list = wrapper.findElement(By.cssSelector(".w-dropdown-list"));
            return list.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForDropdownOpen(WebElement wrapper) {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .withMessage("Dropdown did not open after hover")
                .until(d -> isDropdownOpen(wrapper));
    }

    private void waitForDropdownClosed(WebElement wrapper) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .withMessage("Dropdown did not close after moving away")
                .until(d -> !isDropdownOpen(wrapper));
    }

    /** Dispatches synthetic pointer events — fallback when Actions API doesn't trigger Webflow. */
    private void dispatchPointerEvents(WebElement wrapper, WebElement toggle, String... events) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (String evt : events) {
            js.executeScript(
                    "arguments[0].dispatchEvent(new PointerEvent(arguments[2], {bubbles:true, cancelable:true}));"
                    + "arguments[1].dispatchEvent(new PointerEvent(arguments[2], {bubbles:true, cancelable:true}));",
                    wrapper, toggle, evt);
        }
    }
}
