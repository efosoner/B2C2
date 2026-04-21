package com.b2c2.automation.stepdefs;

import com.b2c2.automation.hooks.WebDriverHooks;
import com.b2c2.automation.pages.HomePage;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class HomepageSteps {
    private WebDriver driver;
    private HomePage homePage;

    private void initDriver() {
        if (driver == null) {
            driver = WebDriverHooks.getDriver();
            homePage = new HomePage(driver);
        }
    }

    @Given("the user is on the B2C2 homepage")
    public void theUserIsOnTheHomepage() {
        initDriver();
        homePage.openHomePage();
    }

    @And("the user has dismissed the cookie consent")
    public void theUserHasDismissedTheCookieConsent() {
        initDriver();
        homePage.dismissCookieConsent();
    }

    @Then("the hero section should display the company tagline")
    public void theHeroSectionShouldDisplayTheCompanyTagline() {
        String tagline = homePage.getHeroTagline();
        assertNotNull(tagline, "Hero tagline element not found");
        assertFalse(tagline.isBlank(), "Hero tagline is blank");
        assertTrue(tagline.toLowerCase().matches(".*\\bliquidity provider\\b.*"),
                "Expected hero tagline to contain the phrase 'liquidity provider' but was: " + tagline);
    }

    @Then("the hero section should display the partner description")
    public void theHeroSectionShouldDisplayThePartnerDescription() {
        String description = homePage.getHeroDescription();
        assertNotNull(description, "Hero description element not found");
        assertFalse(description.isBlank(), "Hero description is blank");
        assertTrue(description.toLowerCase().matches(".*\\bpartner\\b.*"),
                "Expected hero description to mention 'partner' as a whole word but was: " + description);
    }

    @Then("the navigation bar should contain the following menus:")
    public void theNavigationBarShouldContainMenus(DataTable dataTable) {
        List<String> expectedMenus = dataTable.asList().stream()
                .filter(s -> !s.equals("menuName"))
                .sorted()
                .toList();
        List<String> actualMenus = homePage.getNavigationMenuNames().stream()
                .sorted()
                .toList();

        assertEquals(expectedMenus, actualMenus,
                "Navigation menus mismatch — expected exact set");
    }

    @Then("the {string} dropdown should contain the following items:")
    public void theDropdownShouldContainItems(String menuName, DataTable dataTable) {
        List<String> expectedItems = dataTable.asList().stream()
                .filter(s -> !s.equals("itemName"))
                .sorted()
                .toList();
        List<String> actualItems = homePage.getDropdownItems(menuName).stream()
                .sorted()
                .toList();

        assertEquals(expectedItems, actualItems,
                "Dropdown items mismatch for '" + menuName + "' — expected exact set");
    }

    @Then("the news slider should display at least {int} articles")
    public void theNewsSliderShouldDisplayAtLeastArticles(int minCount) {
        int count = homePage.getNewsSliderArticleCount();
        assertTrue(count >= minCount,
                "Expected at least " + minCount + " news slider articles but found " + count);
    }

    @Then("each news slider article should have a title and date")
    public void eachNewsSliderArticleShouldHaveATitleAndDate() {
        List<String> titles = homePage.getNewsSliderTitles();
        List<String> dates = homePage.getNewsSliderDates();

        assertFalse(titles.isEmpty(), "No news slider titles found");
        assertFalse(dates.isEmpty(), "No news slider dates found");

        for (String title : titles) {
            assertFalse(title.isBlank(), "Found a blank news slider title");
            assertTrue(title.length() >= 5,
                    "News slider title suspiciously short: '" + title + "'");
        }
        // Validate dates match a recognizable pattern (e.g., "April 15, 2026" or "15 Apr 2026")
        for (String date : dates) {
            assertFalse(date.isBlank(), "Found a blank news slider date");
            assertTrue(date.matches(".*\\b(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|" +
                            "January|February|March|April|May|June|July|August|September|October|November|December)" +
                            "\\b.*\\d{1,2}.*\\d{4}.*"),
                    "News slider date doesn't match expected format (month + day + year): '" + date + "'");
        }
    }

    @Then("the footer should display the following office locations:")
    public void theFooterShouldDisplayOfficeLocations(DataTable dataTable) {
        List<String> expectedLocations = dataTable.asList().stream()
                .filter(s -> !s.equals("location"))
                .sorted()
                .toList();
        List<String> actualLocations = homePage.getFooterOfficeLocations().stream()
                .sorted()
                .toList();

        assertEquals(expectedLocations, actualLocations,
                "Office locations mismatch — expected exact set");
    }

    @Then("the footer should contain social media links for:")
    public void theFooterShouldContainSocialMediaLinksFor(DataTable dataTable) {
        List<String> expectedPlatforms = dataTable.asList().stream()
                .filter(s -> !s.equals("platform"))
                .sorted()
                .toList();
        Map<String, String> socialLinks = homePage.getSocialMediaLinks();
        List<String> actualPlatforms = socialLinks.keySet().stream()
                .sorted()
                .toList();

        assertEquals(expectedPlatforms, actualPlatforms,
                "Social media links mismatch — expected exact set");
    }

    @Then("all social media links should open in new tabs")
    public void allSocialMediaLinksShouldOpenInNewTabs() {
        assertTrue(homePage.allSocialLinksOpenInNewTab(),
                "Not all social media links have target='_blank'");
    }

    // --- Interactive dropdown steps ---

    @When("the user hovers over the {string} navigation dropdown")
    public void theUserHoversOverNavDropdown(String menuName) {
        homePage.hoverOverNavDropdown(menuName);
    }

    @Then("the {string} dropdown panel should be visible")
    public void theDropdownPanelShouldBeVisible(String menuName) {
        assertTrue(homePage.isNavDropdownPanelVisible(menuName),
                "Expected '" + menuName + "' dropdown panel to be visible after hover");
    }

    @Then("the visible dropdown items should include {string}")
    public void theVisibleDropdownItemsShouldInclude(String expectedItem) {
        // Get items from the currently open dropdown (Solutions is the only one tested)
        List<String> items = homePage.getVisibleDropdownItems("Solutions");
        assertTrue(items.stream().anyMatch(i -> i.equals(expectedItem)),
                "Expected visible item '" + expectedItem + "' not found. Visible items: " + items);
    }

    @When("the user moves away from the {string} navigation dropdown")
    public void theUserMovesAwayFromNavDropdown(String menuName) {
        homePage.moveAwayFromNavDropdown(menuName);
    }

    @Then("the {string} dropdown panel should not be visible")
    public void theDropdownPanelShouldNotBeVisible(String menuName) {
        assertFalse(homePage.isNavDropdownPanelVisible(menuName),
                "Expected '" + menuName + "' dropdown panel to be hidden after moving away");
    }

    // --- Cross-page navigation steps ---

    @When("the user clicks the {string} button in the navigation bar")
    public void theUserClicksTheButtonInTheNavigationBar(String buttonText) {
        homePage.clickNavButton(buttonText);
    }

    @Then("the page URL should contain {string}")
    public void thePageUrlShouldContain(String expectedFragment) {
        String url = homePage.getCurrentUrl();
        assertTrue(url.contains(expectedFragment),
                "Expected URL to contain '" + expectedFragment + "' but was: " + url);
    }

    // --- Page metadata steps ---

    @Then("the page title should contain {string}")
    public void thePageTitleShouldContain(String expectedText) {
        String actual = homePage.getPageTitle();
        assertTrue(actual.contains(expectedText),
                "Expected page title to contain '" + expectedText + "' but was: " + actual);
    }

    @Then("the meta description should contain {string}")
    public void theMetaDescriptionShouldContain(String expectedText) {
        String metaDesc = homePage.getMetaContent("description");
        assertNotNull(metaDesc, "Meta description not found");
        assertTrue(metaDesc.toLowerCase().contains(expectedText.toLowerCase()),
                "Expected meta description to contain '" + expectedText + "' but was: " + metaDesc);
    }

    // --- Logo steps ---

    @Then("the header logo should link to the homepage")
    public void theHeaderLogoShouldLinkToTheHomepage() {
        String href = homePage.getLogoHref();
        assertNotNull(href, "Logo href is null");
        assertFalse(href.isEmpty(), "Logo href is empty");
        try {
            java.net.URI uri = java.net.URI.create(href);
            String host = uri.getHost();
            assertNotNull(host, "Logo href has no host: " + href);
            assertTrue(host.equals("www.b2c2.com") || host.equals("b2c2.com"),
                    "Expected logo to link to b2c2.com but host was: " + host);
            String path = uri.getPath();
            assertTrue(path == null || path.equals("/") || path.isEmpty(),
                    "Expected logo to link to homepage root but path was: " + path);
        } catch (IllegalArgumentException e) {
            fail("Logo href is not a valid URL: " + href);
        }
    }

    @Then("the header logo image should be visible")
    public void theHeaderLogoImageShouldBeVisible() {
        assertTrue(homePage.isLogoImageVisible(), "Header logo image is not visible");
    }

    // --- Footer sitemap steps ---

    @Then("the footer should contain a sitemap link for {string} pointing to {string}")
    public void theFooterShouldContainSitemapLink(String linkText, String expectedPath) {
        Map<String, String> links = homePage.getFooterSitemapLinks();
        String actualPath = links.get(linkText);
        assertNotNull(actualPath, "Footer sitemap link '" + linkText + "' not found. Available: " + links.keySet());
        assertTrue(actualPath.contains(expectedPath),
                "Expected footer link '" + linkText + "' to point to '" + expectedPath + "' but was: " + actualPath);
    }
}
