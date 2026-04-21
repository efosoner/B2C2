package com.b2c2.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class FooterComponent extends BasePage {

    private static final Logger LOG = Logger.getLogger(FooterComponent.class.getName());

    // Footer office locations — fallback chain for Webflow class renames
    private static final By FOOTER_OFFICE_NAMES = By.cssSelector(
            ".clock-widget h1.cityname-2, footer [class*='city'], .clock-widget h1");

    // Social media links — fallback chain for Webflow wrapper class renames
    private static final By SOCIAL_LINKS = By.cssSelector(
            ".div-block-35 a[target='_blank'], a.social-icon[target='_blank'], a.social-icon-2[target='_blank']");

    // Footer sitemap links
    private static final By FOOTER_SITEMAP_LINKS = By.cssSelector(
            ".footer.bottom a[href], footer .footer-links a[href], footer a.footer-link");

    public FooterComponent(WebDriver driver) {
        super(driver);
    }

    public List<String> getFooterOfficeLocations() {
        scrollToElement(FOOTER_OFFICE_NAMES);
        wait.until(ExpectedConditions.presenceOfElementLocated(FOOTER_OFFICE_NAMES));
        return collectNonEmptyText(driver.findElements(FOOTER_OFFICE_NAMES));
    }

    /**
     * Returns a map of social platform name → href from the social links section.
     */
    public Map<String, String> getSocialMediaLinks() {
        wait.until(ExpectedConditions.presenceOfElementLocated(SOCIAL_LINKS));
        List<WebElement> links = driver.findElements(SOCIAL_LINKS);
        Map<String, String> result = new LinkedHashMap<>();
        for (WebElement link : links) {
            String href = link.getDomProperty("href");
            if (href != null && !href.isEmpty()) {
                result.put(deriveSocialPlatformName(href), href);
            }
        }
        return result;
    }

    public boolean allSocialLinksOpenInNewTab() {
        List<WebElement> links = driver.findElements(SOCIAL_LINKS);
        if (links.isEmpty()) return false;
        return links.stream()
                .allMatch(link -> "_blank".equals(link.getDomAttribute("target")));
    }

    /**
     * Returns a map of link text → href for footer sitemap links.
     * Scoped to the footer bottom section to avoid duplicating nav links.
     */
    public Map<String, String> getFooterSitemapLinks() {
        // Try multiple footer selectors since Webflow class names can vary
        List<WebElement> links = driver.findElements(FOOTER_SITEMAP_LINKS);
        if (links.isEmpty()) {
            // Fallback: try all links inside any footer element
            links = driver.findElements(By.cssSelector("footer a[href^='/']"));
        }
        Map<String, String> result = new LinkedHashMap<>();
        for (WebElement link : links) {
            String text = link.getDomProperty("textContent").trim();
            String href = link.getDomProperty("href");
            if (!text.isEmpty() && href != null && !href.isEmpty()) {
                // Normalize href to relative path
                String path = href.replaceFirst("https?://[^/]+", "");
                result.put(text, path);
            }
        }
        return result;
    }

    private String deriveSocialPlatformName(String href) {
        if (href.contains("linkedin")) return "LinkedIn";
        if (href.contains("twitter") || href.contains("x.com")) return "Twitter";
        if (href.contains("medium.com")) return "Medium";
        if (href.contains("youtube")) return "YouTube";
        LOG.warning("Unrecognised social media link — update deriveSocialPlatformName: " + href);
        return href;
    }

    private List<String> collectNonEmptyText(List<WebElement> elements) {
        List<String> result = new ArrayList<>();
        for (WebElement el : elements) {
            String text = el.getText().trim();
            if (!text.isEmpty()) {
                result.add(text);
            }
        }
        return result;
    }
}
