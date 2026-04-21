@regression
Feature: Accessibility Compliance
  As a user with accessibility needs
  I want the B2C2 website to meet WCAG standards
  So that I can navigate and use the site effectively

  # NOTE: The B2C2 website currently has known accessibility violations documented below.
  # These tests verify that no NEW critical/serious violations are introduced beyond the
  # known issues. If a known issue is fixed upstream, the test will flag it for removal
  # from the known list — keeping this documentation accurate.
  #
  # Known issues (as of 2026-04-24):
  #   - html-has-lang: HTML document missing lang attribute (both pages)
  #   - link-name: Links without discernible text (both pages)
  #   - nested-interactive: Nested interactive controls (both pages)
  #   - aria-hidden-focus: Focusable elements inside aria-hidden containers (homepage only)

  @regression
  Scenario: Homepage has no unexpected accessibility violations
    Given the user is on the B2C2 homepage
    And the user has dismissed the cookie consent
    Then the page should have no critical or serious accessibility violations beyond known issues:
      | ruleId                | description                                                  |
      | aria-hidden-focus     | Focusable element inside aria-hidden container               |
      | html-has-lang         | HTML document missing lang attribute                         |
      | link-name             | Links missing discernible text                               |
      | nested-interactive    | Nested interactive controls                                  |

  @regression
  Scenario: Contact page has no unexpected accessibility violations
    Given the user is on the B2C2 contact page
    And the user has dismissed the cookie consent
    Then the page should have no critical or serious accessibility violations beyond known issues:
      | ruleId                | description                                                  |
      | html-has-lang         | HTML document missing lang attribute                         |
      | link-name             | Links missing discernible text                               |
      | nested-interactive    | Nested interactive controls                                  |
