---
description: "Use when writing or editing Cucumber .feature files. Covers Gherkin style, tagging, and declarative scenario conventions for B2C2 tests."
applyTo: "**/*.feature"
---

# Cucumber Feature File Conventions

- Write scenarios in **declarative style** — describe user intent, not UI mechanics
  - ✅ `When the user navigates to the About page`
  - ❌ `When the user clicks the "About" link in the navigation bar`
- One `Feature` per file, named after the functionality being tested
- Tag every scenario with at least `@smoke` or `@regression`
- Use `Scenario Outline` with `Examples` table when testing multiple data variations
- Steps should be reusable — avoid feature-specific wording when a generic step exists
- Reference `.github/specs/test-strategy.md` for the agreed test scenarios and scope
