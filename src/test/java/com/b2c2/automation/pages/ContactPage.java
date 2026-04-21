package com.b2c2.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ContactPage extends BasePage {

    private static final String URL = "https://www.b2c2.com/contact-us";

    private static final By PAGE_HEADING = By.cssSelector(
            "h1.heading-1-2, .contact-section h1, main h1:first-of-type");
    private static final By FORM_INSTRUCTION = By.cssSelector(
            "h2.para-24-3, .contact-section h2, form ~ h2, main h2:first-of-type");
    private static final By CONTACT_FORM = By.id("wf-form-Email-Form-2-Contact-Page");
    private static final By REQUIRED_INPUTS = By.cssSelector(
            "#wf-form-Email-Form-2-Contact-Page input.contatc_field[required]");
    private static final By MESSAGE_TEXTAREA = By.id("Message");

    // Finsweet custom select dropdowns — the underlying <select> elements
    private static final By TEAM_SELECT = By.id("What-Best-Describes-Your-Organisation-4");
    private static final By ORGANISATION_SELECT = By.id("What-Best-Describes-Your-Organisation-3");
    private static final By TRADING_VOLUME_SELECT = By.id("What-Best-Describes-Your-Organisation-5");

    // Media enquiries link
    private static final By MEDIA_EMAIL_LINK = By.cssSelector("a[href^='mailto:B2C2@eternapartners']");

    // reCAPTCHA
    private static final By RECAPTCHA_WIDGET = By.cssSelector(".g-recaptcha, [data-sitekey]");

    // Success / error message containers
    private static final By SUCCESS_MESSAGE = By.cssSelector(".success-message-5, .w-form-done");
    private static final By ERROR_MESSAGE = By.cssSelector(".error-message-6, .w-form-fail");

    // Form fields — scoped to contact form to avoid matching subscribe form duplicates
    private static final By SUBMIT_BUTTON = By.cssSelector(
            "#wf-form-Email-Form-2-Contact-Page input[type='submit']");
    private static final By NAME_INPUT = By.cssSelector(
            "#wf-form-Email-Form-2-Contact-Page input[data-name='Name']");
    private static final By EMAIL_INPUT = By.cssSelector(
            "#wf-form-Email-Form-2-Contact-Page #email");
    private static final By COMPANY_INPUT = By.cssSelector(
            "#wf-form-Email-Form-2-Contact-Page #Company");
    private static final By POSITION_INPUT = By.cssSelector(
            "#wf-form-Email-Form-2-Contact-Page input[data-name='Position at company']");

    public ContactPage(WebDriver driver) {
        super(driver);
    }

    public void openContactPage() {
        open(URL);
    }

    public String getPageHeadingText() {
        scrollToElement(PAGE_HEADING);
        return getText(PAGE_HEADING);
    }

    public String getFormInstruction() {
        scrollToElement(FORM_INSTRUCTION);
        return getText(FORM_INSTRUCTION);
    }

    /**
     * Returns the name/data-name attributes of all required input fields in the contact form.
     */
    public List<String> getRequiredFieldNames() {
        scrollToElement(CONTACT_FORM);
        wait.until(ExpectedConditions.presenceOfElementLocated(CONTACT_FORM));
        List<WebElement> inputs = driver.findElements(REQUIRED_INPUTS);
        List<String> names = new ArrayList<>();
        for (WebElement input : inputs) {
            String dataName = input.getDomAttribute("data-name");
            if (dataName != null && !dataName.isEmpty()) {
                names.add(dataName);
            } else {
                names.add(input.getDomAttribute("name"));
            }
        }
        return names;
    }

    public boolean hasMessageTextarea() {
        if (!isElementDisplayed(MESSAGE_TEXTAREA)) return false;
        WebElement textarea = driver.findElement(MESSAGE_TEXTAREA);
        return textarea.isEnabled() && !Boolean.parseBoolean(textarea.getDomAttribute("readonly"));
    }

    /**
     * Reads option values from a {@code <select>} element using Selenium's Select class.
     * Skips the first option (placeholder).
     */
    private List<String> getSelectOptions(By selectLocator) {
        wait.until(ExpectedConditions.presenceOfElementLocated(selectLocator));
        Select select = new Select(driver.findElement(selectLocator));
        List<WebElement> options = select.getOptions();
        List<String> result = new ArrayList<>();
        for (int i = 1; i < options.size(); i++) {
            String text = options.get(i).getDomProperty("textContent").trim();
            if (!text.isEmpty()) {
                result.add(text);
            }
        }
        return result;
    }

    public List<String> getTeamDropdownOptions() {
        return getSelectOptions(TEAM_SELECT);
    }

    public List<String> getOrganisationDropdownOptions() {
        return getSelectOptions(ORGANISATION_SELECT);
    }

    public List<String> getTradingVolumeDropdownOptions() {
        return getSelectOptions(TRADING_VOLUME_SELECT);
    }

    public String getMediaContactEmail() {
        scrollToElement(MEDIA_EMAIL_LINK);
        WebElement link = waitForElement(MEDIA_EMAIL_LINK);
        return link.getText().trim();
    }

    // --- Finsweet custom dropdown interaction ---

    private By getDropdownSelectLocator(String dropdownName) {
        return switch (dropdownName.toLowerCase()) {
            case "team" -> TEAM_SELECT;
            case "organisation" -> ORGANISATION_SELECT;
            case "trading volume" -> TRADING_VOLUME_SELECT;
            default -> throw new IllegalArgumentException("Unknown dropdown: " + dropdownName);
        };
    }

    /**
     * Finds the .w-dropdown wrapper that contains the given select element
     * by traversing up the DOM via XPath ancestor axis.
     */
    private WebElement findDropdownToggle(String dropdownName) {
        WebElement select = driver.findElement(getDropdownSelectLocator(dropdownName));
        WebElement wrapper = select.findElement(By.xpath("./ancestor::div[contains(@class,'w-dropdown')][1]"));
        return wrapper.findElement(By.cssSelector(".w-dropdown-toggle"));
    }

    /**
     * Clicks the Finsweet custom dropdown toggle to open it using a real Selenium click.
     */
    public void clickFormDropdown(String dropdownName) {
        WebElement toggle = findDropdownToggle(dropdownName);
        scrollToElement(toggle);
        wait.until(ExpectedConditions.elementToBeClickable(toggle)).click();
        waitForFormDropdownState(dropdownName, true);
    }

    /**
     * Clicks the toggle again to close the dropdown using a real Selenium click.
     */
    public void closeFormDropdown(String dropdownName) {
        WebElement toggle = findDropdownToggle(dropdownName);
        wait.until(ExpectedConditions.elementToBeClickable(toggle)).click();
        waitForFormDropdownState(dropdownName, false);
    }

    private void waitForFormDropdownState(String dropdownName, boolean expectOpen) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .withMessage("Dropdown '" + dropdownName + "' did not " + (expectOpen ? "open" : "close"))
                .until(d -> isFormDropdownOpen(dropdownName) == expectOpen);
    }

    /**
     * Checks whether the Finsweet dropdown panel is currently open
     * using native Selenium isDisplayed().
     */
    public boolean isFormDropdownOpen(String dropdownName) {
        WebElement select = driver.findElement(getDropdownSelectLocator(dropdownName));
        WebElement wrapper = select.findElement(By.xpath("./ancestor::div[contains(@class,'w-dropdown')][1]"));
        try {
            WebElement list = wrapper.findElement(By.cssSelector(".w-dropdown-list"));
            return list.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the option texts currently visible in the open Finsweet dropdown panel.
     * Reads from the rendered .w-dropdown-list links, not the hidden &lt;select&gt; element.
     */
    public List<String> getVisibleFormDropdownOptions(String dropdownName) {
        WebElement select = driver.findElement(getDropdownSelectLocator(dropdownName));
        WebElement wrapper = select.findElement(By.xpath("./ancestor::div[contains(@class,'w-dropdown')][1]"));
        List<WebElement> links = wrapper.findElements(By.cssSelector(".w-dropdown-list .w-dropdown-link"));
        List<String> visibleOptions = new ArrayList<>();
        for (WebElement link : links) {
            if (link.isDisplayed()) {
                String text = link.getText().trim();
                if (!text.isEmpty()) {
                    visibleOptions.add(text);
                }
            }
        }
        return visibleOptions;
    }

    // --- Form filling and validation ---

    /**
     * Fills the contact form with valid test data (does not submit).
     */
    public void fillFormWithValidData(String name, String email, String company, String position, String message) {
        scrollToElement(CONTACT_FORM);
        wait.until(ExpectedConditions.presenceOfElementLocated(CONTACT_FORM));
        typeInField(NAME_INPUT, name);
        typeInField(EMAIL_INPUT, email);
        typeInField(COMPANY_INPUT, company);
        typeInField(POSITION_INPUT, position);
        typeInField(MESSAGE_TEXTAREA, message);
    }

    /**
     * Fills all fields with valid data but uses the provided email string.
     */
    public void fillFormWithSpecificEmail(String name, String email, String company, String position, String message) {
        scrollToElement(CONTACT_FORM);
        wait.until(ExpectedConditions.presenceOfElementLocated(CONTACT_FORM));
        typeInField(NAME_INPUT, name);
        typeInField(EMAIL_INPUT, email);
        typeInField(COMPANY_INPUT, company);
        typeInField(POSITION_INPUT, position);
        typeInField(MESSAGE_TEXTAREA, message);
    }

    /**
     * Fills all required fields except the one matching {@code skipFieldName}.
     */
    public void fillFormExcept(String skipFieldName, String name, String email, String company, String position) {
        scrollToElement(CONTACT_FORM);
        wait.until(ExpectedConditions.presenceOfElementLocated(CONTACT_FORM));
        if (!"name".equalsIgnoreCase(skipFieldName)) typeInField(NAME_INPUT, name);
        if (!"email".equalsIgnoreCase(skipFieldName)) typeInField(EMAIL_INPUT, email);
        if (!"company".equalsIgnoreCase(skipFieldName)) typeInField(COMPANY_INPUT, company);
        if (!"position at company".equalsIgnoreCase(skipFieldName)) typeInField(POSITION_INPUT, position);
    }

    private void typeInField(By locator, String text) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(locator));
        field.clear();
        field.sendKeys(text);
    }

    public boolean isSubmitButtonEnabled() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(SUBMIT_BUTTON));
        return btn.isDisplayed() && btn.isEnabled();
    }

    public void clickSubmitButton() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_BUTTON));
        btn.click();
    }

    /**
     * Checks whether a field shows a browser-native validation error.
     * Uses JS only because there is no Selenium API for the HTML5 ValidityState interface.
     */
    public boolean isFieldShowingValidationError(String fieldName) {
        By fieldLocator = switch (fieldName.toLowerCase()) {
            case "name" -> NAME_INPUT;
            case "email" -> EMAIL_INPUT;
            case "company" -> COMPANY_INPUT;
            case "position at company" -> POSITION_INPUT;
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
        WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(fieldLocator));
        // No Selenium equivalent for ValidityState — JS required
        return Boolean.TRUE.equals(
                ((JavascriptExecutor) driver).executeScript("return !arguments[0].validity.valid;", field));
    }

    /** Scrolls a specific element into view (overload for WebElement). */
    private void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});", element);
    }

    // --- reCAPTCHA ---

    public boolean isRecaptchaPresent() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(RECAPTCHA_WIDGET));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // --- Success / Error messages ---

    public boolean isSuccessMessageInDom() {
        return !driver.findElements(SUCCESS_MESSAGE).isEmpty();
    }

    public boolean isSuccessMessageVisible() {
        return isElementDisplayed(SUCCESS_MESSAGE);
    }

    public boolean isErrorMessageInDom() {
        return !driver.findElements(ERROR_MESSAGE).isEmpty();
    }

    public boolean isErrorMessageVisible() {
        return isElementDisplayed(ERROR_MESSAGE);
    }

    // --- Field placeholders ---

    public String getFieldPlaceholder(String fieldName) {
        By fieldLocator = switch (fieldName.toLowerCase()) {
            case "name" -> NAME_INPUT;
            case "email" -> EMAIL_INPUT;
            case "company" -> COMPANY_INPUT;
            case "position at company" -> POSITION_INPUT;
            case "message" -> MESSAGE_TEXTAREA;
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        };
        WebElement field = wait.until(ExpectedConditions.presenceOfElementLocated(fieldLocator));
        return field.getDomAttribute("placeholder");
    }
}
