package com.b2c2.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Map;

public class HomePage extends BasePage {

    private static final String URL = "https://www.b2c2.com/";

    // Hero section — fallback chain for Webflow auto-generated classes
    private static final By HERO_TAGLINE = By.cssSelector(
            "section.hero-section h1, .section-hero h1, h1.heading-59, main h1:first-of-type");
    private static final By HERO_DESCRIPTION = By.cssSelector(
            "section.hero-section h1 ~ h1, .section-hero h1 ~ h1, h1.heading-60");

    private final CookieBannerComponent cookieBanner;
    private final HeaderComponent header;
    private final FooterComponent footer;
    private final NewsSliderComponent newsSlider;

    public HomePage(WebDriver driver) {
        super(driver);
        this.cookieBanner = new CookieBannerComponent(driver);
        this.header = new HeaderComponent(driver);
        this.footer = new FooterComponent(driver);
        this.newsSlider = new NewsSliderComponent(driver);
    }

    public void openHomePage() {
        open(URL);
    }

    // --- General page methods ---

    public void waitForUrlContaining(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    // --- Hero section (HomePage-specific) ---

    public String getHeroTagline() {
        scrollToElement(HERO_TAGLINE);
        return getText(HERO_TAGLINE);
    }

    public String getHeroDescription() {
        scrollToElement(HERO_DESCRIPTION);
        return getText(HERO_DESCRIPTION);
    }

    // --- Cookie banner delegation ---

    public void dismissCookieConsent() {
        cookieBanner.dismissCookieConsent();
    }

    // --- Header / navigation delegation ---

    public List<String> getNavigationMenuNames() {
        return header.getNavigationMenuNames();
    }

    public List<String> getDropdownItems(String menuName) {
        return header.getDropdownItems(menuName);
    }

    public void hoverOverNavDropdown(String menuName) {
        header.hoverOverNavDropdown(menuName);
    }

    public void moveAwayFromNavDropdown(String menuName) {
        header.moveAwayFromNavDropdown(menuName);
    }

    public boolean isNavDropdownPanelVisible(String menuName) {
        return header.isNavDropdownPanelVisible(menuName);
    }

    public List<String> getVisibleDropdownItems(String menuName) {
        return header.getVisibleDropdownItems(menuName);
    }

    public void clickNavButton(String buttonText) {
        header.clickNavButton(buttonText);
    }

    public String getLogoHref() {
        return header.getLogoHref();
    }

    public boolean isLogoImageVisible() {
        return header.isLogoImageVisible();
    }

    // --- Footer delegation ---

    public List<String> getFooterOfficeLocations() {
        return footer.getFooterOfficeLocations();
    }

    public Map<String, String> getSocialMediaLinks() {
        return footer.getSocialMediaLinks();
    }

    public boolean allSocialLinksOpenInNewTab() {
        return footer.allSocialLinksOpenInNewTab();
    }

    public Map<String, String> getFooterSitemapLinks() {
        return footer.getFooterSitemapLinks();
    }

    // --- News slider delegation ---

    public int getNewsSliderArticleCount() {
        return newsSlider.getNewsSliderArticleCount();
    }

    public List<String> getNewsSliderTitles() {
        return newsSlider.getNewsSliderTitles();
    }

    public List<String> getNewsSliderDates() {
        return newsSlider.getNewsSliderDates();
    }
}
