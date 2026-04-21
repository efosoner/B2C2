# B2C2 Website Test Automation

Cucumber BDD test automation suite for the [B2C2 website](https://www.b2c2.com/) using Java, Selenium WebDriver, and the Page Object Model.

## Prerequisites

- **Java 17** or higher
- **Browser** — at least one of: Google Chrome (default), Microsoft Edge, or Mozilla Firefox

> Maven is bundled via the [Maven Wrapper](https://maven.apache.org/wrapper/) — no Maven installation required.
> Browser drivers are managed automatically by Selenium Manager (built into Selenium 4.6+) — no manual driver installation required.

## Project Structure

```
src/test/
├── java/com/b2c2/automation/
│   ├── hooks/             # Cucumber @Before/@After hooks (WebDriver lifecycle)
│   ├── pages/             # Page Object classes (BasePage, HomePage, ContactPage)
│   ├── runners/           # Cucumber test runner (JUnit Platform Suite)
│   ├── stepdefs/          # Step definition classes (HomepageSteps, ContactFormSteps, AccessibilitySteps)
│   └── testdata/          # Test data factories for form input generation
└── resources/
    ├── features/          # Gherkin .feature files
    └── cucumber.properties
```

## Quick Start

The fastest way to run the tests — auto-detects your browser and Java:

```bash
# Windows (double-click or run from terminal)
run.bat

# macOS / Linux
chmod +x run.sh && ./run.sh

# Headed mode — opens the browser so you can watch
run.bat headed          # Windows
./run.sh headed         # macOS / Linux

# Demo mode — extra slow for presentations
run.bat demo
./run.sh demo
```

## Build & Run

```bash
# Compile the project
./mvnw clean compile

# Run all tests (headless, fast — default for CI)
./mvnw clean test

# Run a specific feature file
./mvnw test -Dcucumber.features="src/test/resources/features/homepage_structure.feature"

# Run scenarios by tag
./mvnw test -Dcucumber.filter.tags="@smoke"

# Headed mode — opens a visible browser window
./mvnw test -Dheadless=false

# Slow-motion mode — pauses between steps so you can follow along
./mvnw test -Dheadless=false -Dslow=1500

# Extra slow for demos/presentations
./mvnw test -Dheadless=false -Dslow=3000

# Run on a different browser
./mvnw test -Dbrowser=firefox
./mvnw test -Dbrowser=edge
./mvnw test -Dbrowser=firefox -Dheadless=false -Dslow=1500
```

> **Windows note:** Use `mvnw.cmd` instead of `./mvnw` if not using Git Bash.

### Run Mode Options

| Property | Default | Description |
|----------|---------|-------------|
| `-Dbrowser` | `chrome` | Browser to use: `chrome`, `edge`, or `firefox` |
| `-Dheadless` | `true` | Set to `false` to open a visible browser window |
| `-Dslow` | `0` | Milliseconds to pause after each Cucumber step (0 = no delay) |

## Test Reports

After running tests, the HTML report is available at:

```
target/cucumber-reports/report.html
```

## Test Scenarios

### 1. Homepage Content & Structure (`homepage_structure.feature`)

Validates the homepage comprehensively across 14 scenarios (8 `@smoke` + 6 `@regression`):

| Scenario | Tag | What it verifies |
|----------|-----|-----------------|
| Hero section | @smoke | Brand tagline ("liquidity provider") and partner description are displayed |
| Navigation menus | @smoke | All 4 top-level dropdowns present (Solutions, About, News & Insights, Join B2C2) |
| Solutions dropdown items | @smoke | All 5 product sub-links (Trading Overview, OTC Products, Market Making, Liquidity Partner, Stablecoin Swaps) |
| About dropdown items | @smoke | All 6 company pages (About Us, Meet the Team, Memberships, Social Impact, In the Community, FAQs) |
| News & Insights dropdown items | @smoke | All 4 content pages (B2C2 in the News, Press Releases, Events, Insights) |
| Join B2C2 dropdown items | @smoke | All 2 recruitment pages (Our Values, Careers) |
| Page title & meta description | @smoke | Page title contains "B2C2"; meta description mentions "liquidity provider" |
| Header logo | @smoke | Logo links to homepage and is visible |
| Solutions dropdown interaction | @regression | Dropdown opens on hover, items are visible, closes on mouse-away |
| News slider | @regression | At least 3 press release articles with titles and dates |
| Global offices | @regression | All 7 office locations in the footer (London HQ, New Jersey, Tokyo, Luxembourg, Singapore, Poland, France) |
| Social media links | @regression | LinkedIn, Twitter, Medium, YouTube links present and open in new tabs (`target="_blank"`) |
| Cross-page navigation | @regression | Clicking CONTACT button navigates to `/contact-us` with correct heading |
| Footer sitemap links | @regression | Footer contains sitemap links for Trading Overview, About Us, Careers, FAQs |

### 2. Contact Page Form & Structure (`contact_form.feature`)

Validates the contact form deeply across 18 scenarios (8 `@smoke` + 10 `@regression`):

| Scenario | Tag | What it verifies |
|----------|-----|-----------------|
| Page heading & instruction | @smoke | "Get in touch" heading, form instruction text |
| Required input fields | @smoke | Name, email, company, position fields are `required`; message textarea is present |
| Team dropdown | @smoke | 5 department options (Sales, General, Press, Legal, Onboarding) |
| Organisation dropdown | @smoke | 5 business categories (Exchange, Fund, OTC Desk/Broker, Proprietary Trading Firm, Other) |
| Trading volume dropdown | @smoke | 7 volume ranges from <$1m to $500m+ plus N/a |
| Media enquiries | @smoke | Correct contact email (B2C2@eternapartners.com) is displayed |
| Page title | @smoke | Contact page title contains "B2C2" |
| reCAPTCHA protection | @smoke | Contact form displays a reCAPTCHA widget |
| Team dropdown interaction | @regression | Dropdown opens on click, shows options visibly, closes on re-click |
| Organisation dropdown interaction | @regression | Dropdown opens on click, shows options visibly, closes on re-click |
| Trading volume dropdown interaction | @regression | Dropdown opens on click, shows options visibly, closes on re-click |
| Valid input acceptance | @regression | Form accepts valid test data; submit button is present and enabled |
| Required field validation | @regression | Leaving a required field empty triggers a browser validation error |
| Success/error containers | @regression | Success and error message containers are present but hidden by default |
| Placeholder text | @regression | Name and email fields have placeholder text |
| Malformed email rejection | @regression | Form rejects email without valid format (e.g. "notanemail") |
| Email without domain | @regression | Form rejects email missing domain (e.g. "user@") |
| Required field validation (Outline) | @regression | Scenario Outline — validates each required field (Name, email, Company, Position) individually |

### 3. Accessibility Compliance (`accessibility.feature`)

Runs axe-core WCAG accessibility scans across 2 scenarios (both `@regression`):

| Scenario | Tag | What it verifies |
|----------|-----|-----------------|
| Homepage accessibility | @regression | No new critical/serious axe-core violations beyond documented known issues |
| Contact page accessibility | @regression | No new critical/serious axe-core violations beyond documented known issues |

> **Known issues:** The B2C2 website currently has several serious accessibility violations (`html-has-lang`, `link-name`, `nested-interactive`, `aria-hidden-focus`) that appear to be Webflow platform-level issues. These are documented in the feature file and excluded from failure criteria so that tests remain green while still catching any new regressions.

### Tags

| Tag | Purpose |
|-----|---------|
| `@smoke` | Core structure and content checks — fast, safe to run frequently |
| `@regression` | Deeper interactive tests, validation flows, and accessibility audits |

## Continuous Integration

Tests run automatically via **GitHub Actions** on every push and pull request to `main`. The CI matrix tests against both Chrome and Firefox in headless mode. Test reports and failure screenshots are uploaded as workflow artifacts.

## Design Decisions

- **Page Object Model** — all browser interactions are encapsulated in page object classes (`pages/`); step definitions delegate to them and never call WebDriver directly
- **Selenium Manager** — Selenium 4.25's built-in driver management replaces the need for WebDriverManager, reducing dependencies
- **Cookie consent handling** — the Termly cookie banner uses shadow DOM and is dismissed via JavaScript, with a graceful timeout if the banner doesn't appear
- **JavaScript-based element queries** — Webflow renders some elements as hidden in headless mode; critical selectors use `document.querySelector` via `JavascriptExecutor` for reliability
- **Data Tables for verification** — Cucumber DataTables drive parameterised assertions (dropdown options, menu items, office locations) making tests data-driven and easy to maintain
- **Chrome → Edge fallback** — if Chrome isn't available, tests automatically fall back to Edge
- **Cross-browser support** — tests verified on Chrome, Edge, and Firefox via `-Dbrowser` property
- **Accessibility testing with axe-core** — the `accessibility.feature` uses the axe-core accessibility engine (via `com.deque.html.axe-core:selenium`) to scan pages for WCAG violations, catching critical and serious issues automatically
- **No form submission test** — the contact form submits to a production endpoint; automating submission would spam B2C2's real mailbox. Tests verify form structure, required fields, and dropdown options without submitting.

