---
description: "Scaffold a new Cucumber .feature file with scenarios for a given website functionality"
agent: "agent"
---

Create a new Cucumber `.feature` file for the following functionality of the B2C2 website.

## Instructions

1. Read the test strategy at `.github/specs/test-strategy.md` for context
2. Read the site exploration report at `.github/specs/site-exploration-report.md` for selectors
3. Create the feature file at `src/test/resources/features/<functionality-name>.feature`

## Output Requirements

- Use declarative Gherkin (describe user intent, not UI clicks)
- Include a `Feature:` description with As a / I want / So that
- Tag with `@smoke`
- Include at least one `Scenario` or `Scenario Outline` with `Examples`
- Keep steps reusable across features where possible
