---
description: "Use when writing or editing Java test automation code — step definitions, page objects, hooks, and runner classes for B2C2 Cucumber tests."
applyTo: "src/test/java/**/*.java"
---

# Java Test Code Conventions

## Page Objects (`pages/`)
- Extend `BasePage`
- Declare locators as `private final By` fields
- Provide action methods that return the next page object (fluent navigation)
- Include `isLoaded()` method using explicit waits
- No assertions in page objects

## Step Definitions (`stepdefs/`)
- Delegate all browser interaction to page object methods — never call `driver.findElement()` directly
- Keep steps thin: one page object call per step where possible
- Use dependency injection or shared state for passing page objects between steps

## General Rules
- Use `WebDriverWait` with explicit conditions — never `Thread.sleep`
- WebDriver lifecycle is managed in `hooks/WebDriverHooks.java` — do not create or quit drivers elsewhere
- Target Java 17+ language features
