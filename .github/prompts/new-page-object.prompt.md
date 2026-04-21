---
description: "Scaffold a new Page Object class for a page on the B2C2 website"
agent: "agent"
---

Create a new Page Object class for the specified page of the B2C2 website.

## Instructions

1. Read the site exploration report at `.github/specs/site-exploration-report.md` for element selectors
2. Create the class at `src/test/java/com/b2c2/automation/pages/<PageName>Page.java`
3. Extend `BasePage`

## Output Requirements

- Declare element locators as `private final By` fields at the top of the class
- Use descriptive locator names (e.g., `navigationMenu`, `contactButton`)
- Prefer CSS selectors; use XPath only when CSS cannot express the query
- Include action methods that return the next page object for navigation flows
- Include verification methods (e.g., `isLoaded()`) using explicit waits
- Do not include any test assertions — page objects describe capabilities, not expectations
