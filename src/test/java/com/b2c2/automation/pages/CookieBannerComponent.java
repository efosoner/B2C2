package com.b2c2.automation.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CookieBannerComponent extends BasePage {

    private static final Duration COOKIE_TIMEOUT = Duration.ofSeconds(8);

    public CookieBannerComponent(WebDriver driver) {
        super(driver);
    }

    /**
     * Dismiss the Termly cookie consent banner.
     * Uses JS only because Termly renders inside shadow DOM which Selenium cannot pierce.
     * Throws if the banner appeared but couldn't be dismissed.
     */
    public void dismissCookieConsent() {
        boolean bannerFound;
        try {
            WebDriverWait cookieWait = new WebDriverWait(driver, COOKIE_TIMEOUT);
            // Shadow DOM — must use JS to detect
            bannerFound = cookieWait.until(d -> {
                Object result = ((JavascriptExecutor) d).executeScript(
                        "return document.querySelector('#termly-code-snippet-support') !== null "
                        + "|| document.querySelector('[class*=\"termly\"]') !== null;");
                return Boolean.TRUE.equals(result);
            });
        } catch (Exception noBanner) {
            return;
        }

        if (!bannerFound) return;

        // Shadow DOM — must use JS to click inside shadow root
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Boolean clicked = (Boolean) js.executeScript(
                "var container = document.querySelector('#termly-code-snippet-support');"
                + "if (!container) return false;"
                + "var shadow = container.shadowRoot;"
                + "if (shadow) {"
                + "  var btn = shadow.querySelector('[class*=\"t-acceptAllButton\"], [class*=\"accept\"], button');"
                + "  if (btn) { btn.click(); return true; }"
                + "}"
                + "var btn = document.querySelector('[class*=\"t-acceptAllButton\"], [data-tid=\"banner-accept\"]');"
                + "if (btn) { btn.click(); return true; }"
                + "return false;");

        if (!Boolean.TRUE.equals(clicked)) {
            Boolean fallbackClicked = (Boolean) js.executeScript(
                    "var buttons = document.querySelectorAll('button');"
                    + "for (var b of buttons) {"
                    + "  if (b.textContent.trim().match(/accept/i)) { b.click(); return true; }"
                    + "}"
                    + "return false;");

            if (!Boolean.TRUE.equals(fallbackClicked)) {
                throw new IllegalStateException(
                        "Cookie consent banner appeared but the accept button could not be found or clicked");
            }
        }

        // Shadow DOM — must use JS to verify banner dismissed
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
                Object result = ((JavascriptExecutor) d).executeScript(
                        "var c = document.querySelector('#termly-code-snippet-support');"
                        + "if (!c) return true;"
                        + "var s = c.shadowRoot;"
                        + "if (!s) return true;"
                        + "var banner = s.querySelector('[class*=\"banner\"]');"
                        + "return !banner || banner.offsetHeight === 0;");
                return Boolean.TRUE.equals(result);
            });
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Cookie banner was clicked but did not dismiss within timeout", e);
        }
    }
}
