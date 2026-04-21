# Main Specification

## Objective

Build a Java + Cucumber BDD test automation project that runs two end-to-end tests against **https://www.b2c2.com/**.

## Deliverables

### 1. Two Cucumber automation tests

- **Language**: Java
- **Framework**: Cucumber (Gherkin `.feature` files + Java step definitions)
- **Target**: https://www.b2c2.com/
- **Scope**: Pick two distinct functionalities of the website to verify
- **Quality bar**: Robust, well-structured, follows best practices (Page Object Model, proper waits, clean separation of concerns)

### 2. Documentation

- A `README.md` at the project root with:
  - Prerequisites (Java version, Maven, browser)
  - How to build the project
  - How to run the full test suite
  - How to run a single test

## Constraints

- The solution must be publishable to a git repository as-is
- Tests must be runnable by a reviewer with only the README instructions
- No manual setup steps beyond what is documented

## Acceptance Criteria

| # | Criterion | Verified by |
|---|-----------|-------------|
| 1 | Project compiles with `mvn clean test` | Build succeeds with zero errors |
| 2 | Two `.feature` files exist, each testing a distinct functionality | File count and content review |
| 3 | All scenarios pass against the live site | `mvn clean test` exits 0, Cucumber report shows green |
| 4 | Page Object Model is used | Step definitions delegate to page object classes |
| 5 | README contains build and run instructions | Reviewer can execute tests following only the README |

## Implementation Notes

- Selenium 4.6+ includes Selenium Manager for automatic browser driver setup — no manual chromedriver downloads or external driver management libraries needed
- Default to headless browser execution so tests can run in CI without a display
- Use explicit waits (`WebDriverWait`) instead of `Thread.sleep`
- Keep feature files declarative (describe user intent, not UI mechanics)
- Tag scenarios with `@smoke` for easy selective execution
