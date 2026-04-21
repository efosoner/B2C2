package com.b2c2.automation.stepdefs;

import com.b2c2.automation.hooks.WebDriverHooks;
import com.b2c2.automation.pages.ContactPage;
import com.b2c2.automation.testdata.TestDataFactory;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ContactFormSteps {

    private WebDriver driver;
    private ContactPage contactPage;

    private void initDriver() {
        if (driver == null) {
            driver = WebDriverHooks.getDriver();
            contactPage = new ContactPage(driver);
        }
    }

    @Given("the user is on the B2C2 contact page")
    public void theUserIsOnTheContactPage() {
        initDriver();
        contactPage.openContactPage();
    }

    @Then("the page heading should display {string}")
    public void thePageHeadingShouldDisplay(String expectedText) {
        initDriver();
        String actual = contactPage.getPageHeadingText();
        assertEquals(expectedText, actual.trim(),
                "Page heading text mismatch");
    }

    @Then("the form should display the instruction {string}")
    public void theFormShouldDisplayInstruction(String expectedText) {
        String actual = contactPage.getFormInstruction();
        assertTrue(actual.toLowerCase().contains(expectedText.toLowerCase()),
                "Expected form instruction to contain '" + expectedText + "' but was: " + actual);
    }

    @Then("the contact form should contain the following required fields:")
    public void theContactFormShouldContainRequiredFields(DataTable dataTable) {
        List<String> expectedFields = dataTable.asList().stream()
                .filter(s -> !s.equals("fieldName"))
                .map(String::toLowerCase)
                .sorted()
                .toList();
        List<String> actualFields = contactPage.getRequiredFieldNames().stream()
                .map(String::toLowerCase)
                .sorted()
                .toList();

        assertEquals(expectedFields, actualFields,
                "Required fields mismatch — expected exact set");
    }

    @Then("the contact form should contain a message textarea")
    public void theContactFormShouldContainAMessageTextarea() {
        assertTrue(contactPage.hasMessageTextarea(),
                "Message textarea not found on the contact form");
    }

    @Then("the {string} dropdown should contain the following options:")
    public void theDropdownShouldContainOptions(String dropdownName, DataTable dataTable) {
        List<String> expectedOptions = dataTable.asList().stream()
                .filter(s -> !s.equals("option"))
                .sorted()
                .toList();

        List<String> actualOptions = switch (dropdownName.toLowerCase()) {
            case "team" -> contactPage.getTeamDropdownOptions();
            case "organisation" -> contactPage.getOrganisationDropdownOptions();
            case "trading volume" -> contactPage.getTradingVolumeDropdownOptions();
            default -> throw new IllegalArgumentException("Unknown dropdown: " + dropdownName);
        };
        actualOptions = actualOptions.stream().sorted().toList();

        assertEquals(expectedOptions, actualOptions,
                "Dropdown options mismatch for '" + dropdownName + "' — expected exact set");
    }

    @Then("the page should display media contact email {string}")
    public void thePageShouldDisplayMediaContactEmail(String expectedEmail) {
        String actual = contactPage.getMediaContactEmail();
        assertEquals(expectedEmail, actual.trim(),
                "Media contact email mismatch");
    }

    // --- Interactive Finsweet dropdown steps ---

    @When("the user clicks the {string} form dropdown")
    public void theUserClicksTheFormDropdown(String dropdownName) {
        contactPage.clickFormDropdown(dropdownName);
    }

    @Then("the {string} form dropdown panel should be open")
    public void theFormDropdownPanelShouldBeOpen(String dropdownName) {
        assertTrue(contactPage.isFormDropdownOpen(dropdownName),
                "Expected '" + dropdownName + "' form dropdown to be open after click");
    }

    @Then("the visible {string} dropdown options should include {string}")
    public void theVisibleDropdownOptionsShouldInclude(String dropdownName, String expectedOption) {
        List<String> options = contactPage.getVisibleFormDropdownOptions(dropdownName);
        assertTrue(options.stream().anyMatch(o -> o.equals(expectedOption)),
                "Expected visible option '" + expectedOption + "' not found in '" + dropdownName
                + "'. Visible: " + options);
    }

    @When("the user clicks the {string} form dropdown again to close it")
    public void theUserClicksTheFormDropdownAgainToCloseIt(String dropdownName) {
        contactPage.closeFormDropdown(dropdownName);
    }

    @Then("the {string} form dropdown panel should be closed")
    public void theFormDropdownPanelShouldBeClosed(String dropdownName) {
        assertFalse(contactPage.isFormDropdownOpen(dropdownName),
                "Expected '" + dropdownName + "' form dropdown to be closed");
    }

    // --- Form filling and validation steps ---

    @When("the user fills the contact form with valid test data")
    public void theUserFillsTheContactFormWithValidTestData() {
        initDriver();
        Map<String, String> data = TestDataFactory.validContactFormData();
        contactPage.fillFormWithValidData(
                data.get("name"), data.get("email"), data.get("company"),
                data.get("position"), data.get("message"));
    }

    @Then("the submit button should be present and enabled")
    public void theSubmitButtonShouldBePresentAndEnabled() {
        assertTrue(contactPage.isSubmitButtonEnabled(),
                "Expected the submit button to be present and enabled");
    }

    @When("the user fills the contact form with email {string}")
    public void theUserFillsTheContactFormWithEmail(String email) {
        initDriver();
        Map<String, String> data = TestDataFactory.validContactFormData();
        contactPage.fillFormWithSpecificEmail(
                data.get("name"), email, data.get("company"),
                data.get("position"), data.get("message"));
    }

    @When("the user fills every contact field except {string}")
    public void theUserFillsEveryContactFieldExcept(String fieldName) {
        initDriver();
        Map<String, String> data = TestDataFactory.validContactFormData();
        contactPage.fillFormExcept(fieldName,
                data.get("name"), data.get("email"), data.get("company"),
                data.get("position"));
    }

    @When("the user attempts to submit the contact form")
    public void theUserAttemptsToSubmitTheContactForm() {
        contactPage.clickSubmitButton();
    }

    @Then("the {string} field should show a validation error")
    public void theFieldShouldShowAValidationError(String fieldName) {
        assertTrue(contactPage.isFieldShowingValidationError(fieldName),
                "Expected field '" + fieldName + "' to show a validation error");
    }

    // --- Page title step ---

    @Then("the contact page title should contain {string}")
    public void theContactPageTitleShouldContain(String expectedText) {
        initDriver();
        String actual = contactPage.getPageTitle();
        assertTrue(actual.contains(expectedText),
                "Expected contact page title to contain '" + expectedText + "' but was: " + actual);
    }

    // --- reCAPTCHA steps ---

    @Then("the contact form should display a reCAPTCHA widget")
    public void theContactFormShouldDisplayARecaptchaWidget() {
        assertTrue(contactPage.isRecaptchaPresent(),
                "reCAPTCHA widget not found on the contact form");
    }

    // --- Success/Error message steps ---

    @Then("the success message container should be present but not visible")
    public void theSuccessMessageContainerShouldBePresentButNotVisible() {
        assertTrue(contactPage.isSuccessMessageInDom(),
                "Success message container not found in DOM");
        assertFalse(contactPage.isSuccessMessageVisible(),
                "Success message should be hidden by default");
    }

    @Then("the error message container should be present but not visible")
    public void theErrorMessageContainerShouldBePresentButNotVisible() {
        assertTrue(contactPage.isErrorMessageInDom(),
                "Error message container not found in DOM");
        assertFalse(contactPage.isErrorMessageVisible(),
                "Error message should be hidden by default");
    }

    // --- Placeholder steps ---

    @Then("the {string} field should have a placeholder")
    public void theFieldShouldHaveAPlaceholder(String fieldName) {
        initDriver();
        String actual = contactPage.getFieldPlaceholder(fieldName);
        assertNotNull(actual, "Placeholder not found for field: " + fieldName);
        assertFalse(actual.isBlank(), "Placeholder is blank for field: " + fieldName);
    }

    @Then("the {string} field should have placeholder text {string}")
    public void theFieldShouldHavePlaceholderText(String fieldName, String expectedPlaceholder) {
        initDriver();
        String actual = contactPage.getFieldPlaceholder(fieldName);
        assertNotNull(actual, "Placeholder not found for field: " + fieldName);
        assertEquals(expectedPlaceholder, actual,
                "Placeholder mismatch for field '" + fieldName + "'");
    }
}
