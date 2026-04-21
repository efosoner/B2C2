Feature: Contact Page Form and Structure
  As a potential institutional client
  I want to see a comprehensive contact form on the B2C2 website
  So that I can reach the appropriate B2C2 team

  Background:
    Given the user is on the B2C2 contact page
    And the user has dismissed the cookie consent

  @smoke
  Scenario: Contact page displays the correct heading and form instruction
    Then the page heading should display "Get in touch"
    And the form should display the instruction "Please complete the form to get in touch"

  @smoke
  Scenario: Contact form contains all required input fields and a message area
    Then the contact form should contain the following required fields:
      | fieldName              |
      | Name                   |
      | email                  |
      | Company                |
      | Position at company    |
    And the contact form should contain a message textarea

  @smoke
  Scenario: Team selection dropdown contains all department options
    Then the "team" dropdown should contain the following options:
      | option       |
      | Sales        |
      | General      |
      | Press        |
      | Legal        |
      | Onboarding   |

  @smoke
  Scenario: Organisation type dropdown contains all business categories
    Then the "organisation" dropdown should contain the following options:
      | option                      |
      | Exchange                    |
      | Fund                        |
      | OTC Desk / Broker           |
      | Proprietary Trading Firm    |
      | Other                       |

  @smoke
  Scenario: Trading volume dropdown contains all volume ranges
    Then the "trading volume" dropdown should contain the following options:
      | option       |
      | <$1m         |
      | $1m-$10m     |
      | $10m-$50m    |
      | $50m-$150m   |
      | $150m-$500m  |
      | $500m+       |
      | N/a          |

  @regression
  Scenario: Team dropdown opens on click, displays options visibly, and closes
    When the user clicks the "team" form dropdown
    Then the "team" form dropdown panel should be open
    And the visible "team" dropdown options should include "Sales"
    When the user clicks the "team" form dropdown again to close it
    Then the "team" form dropdown panel should be closed

  @smoke
  Scenario: Media enquiries section displays correct contact information
    Then the page should display media contact email "B2C2@eternapartners.com"

  # NOTE: We do not submit the form to avoid spamming the production mailbox.
  # These scenarios test client-side validation behaviour only.

  @regression
  Scenario: Contact form accepts valid input without submission
    When the user fills the contact form with valid test data
    Then the submit button should be present and enabled

  @regression
  Scenario: Contact form shows validation error for missing required field
    When the user fills every contact field except "email"
    And the user attempts to submit the contact form
    Then the "email" field should show a validation error

  @regression
  Scenario: Organisation type dropdown opens on click, displays options, and closes
    When the user clicks the "organisation" form dropdown
    Then the "organisation" form dropdown panel should be open
    And the visible "organisation" dropdown options should include "Exchange"
    When the user clicks the "organisation" form dropdown again to close it
    Then the "organisation" form dropdown panel should be closed

  @regression
  Scenario: Trading volume dropdown opens on click, displays options, and closes
    When the user clicks the "trading volume" form dropdown
    Then the "trading volume" form dropdown panel should be open
    And the visible "trading volume" dropdown options should include "<$1m"
    When the user clicks the "trading volume" form dropdown again to close it
    Then the "trading volume" form dropdown panel should be closed

  @smoke
  Scenario: Contact page has correct page title
    Then the contact page title should contain "B2C2"

  @smoke
  Scenario: Contact form includes reCAPTCHA protection
    Then the contact form should display a reCAPTCHA widget

  @regression
  Scenario: Success and error message containers are hidden by default
    Then the success message container should be present but not visible
    And the error message container should be present but not visible

  @regression
  Scenario: Form fields have placeholder text
    Then the "Name" field should have a placeholder
    And the "email" field should have a placeholder

  @regression
  Scenario: Contact form rejects malformed email address
    When the user fills the contact form with email "notanemail"
    And the user attempts to submit the contact form
    Then the "email" field should show a validation error

  @regression
  Scenario: Contact form rejects email without domain
    When the user fills the contact form with email "user@"
    And the user attempts to submit the contact form
    Then the "email" field should show a validation error

  @regression
  Scenario Outline: Contact form shows validation error when required field is empty
    When the user fills every contact field except "<field>"
    And the user attempts to submit the contact form
    Then the "<field>" field should show a validation error

    Examples:
      | field                |
      | Name                 |
      | email                |
      | Company              |
      | Position at company  |
