# Test Strategy: B2C2 Website Automation

> Based on site exploration performed 2026-04-21
> Reference: `.github/specs/site-exploration-report.md`

## Test Candidate Assessment

| Functionality | Page | Stability | Testability | Value | Complexity | Rating |
|--------------|------|-----------|-------------|-------|------------|--------|
| Page navigation (header links) | All | High | High | High | Low | **High** |
| Page title & meta verification | All | High | High | Medium | Low | **High** |
| FAQ accordion expand/collapse | FAQs | High | High | Medium | Low | **High** |
| Contact form field validation | Contact | High | Medium | High | Medium | Medium |
| Subscribe email form | Homepage | Medium | Low | Medium | Medium | Low |
| News carousel interaction | Homepage | Low | Low | Low | High | Low |
| Navigation dropdown completeness | All | High | High | High | Low | **High** |
| Page metadata (title/meta) | All | High | High | Medium | Low | **High** |
| Footer sitemap links | Homepage | High | High | Medium | Low | **High** |
| reCAPTCHA presence | Contact | High | Medium | Medium | Low | Medium |
| Form feedback messages | Contact | Medium | Medium | Medium | Low | Medium |
| Mobile hamburger menu | All | Medium | Medium | Medium | High | Low |

### Why these ratings?

- **Navigation**: Highly stable (URLs/structure rarely change), easy to verify (page title + URL), catches broken links.
- **FAQ accordion**: Content-rich, verifiable text, no third-party blockers.
- **Contact form validation**: Testable for field-level validation but reCAPTCHA blocks actual submission — can only test partial flow.
- **Subscribe/Carousel/Mobile**: Either have reCAPTCHA, dynamic third-party dependencies, or require viewport resizing — fragile for automation.

---

## Implemented Test Scenarios

> **Note:** The original strategy recommended Homepage Navigation and FAQ Page tests.
> During implementation, the final scope was refined to **Homepage Content & Structure** and **Contact Page Form & Structure** — these provide broader coverage of the site's most important user-facing content and interactive elements.

### Scenario 1: Homepage Content & Structure (`homepage_structure.feature`)

**What**: Validates the homepage comprehensively — hero section messaging, navigation menus, dropdown interactions, news slider, global office locations, social media links, and cross-page navigation to the contact page.

**Why**:
- Covers the most critical first impression and navigation entry points
- Tests both static content and interactive behaviour (dropdown hover/close)
- 14 focused scenarios exercise different page sections independently
- Stable selectors (href-based links, structural elements)

**Scenarios implemented**:

| # | Scenario | Key verification |
|---|----------|-----------------|
| 1 | Hero section | Brand tagline and partner description visible |
| 2 | Navigation menus | All 4 top-level dropdowns present |
| 3 | Solutions dropdown items | All 5 product sub-links listed |
| 4 | Solutions dropdown interaction | Opens on hover, items visible, closes on mouse-away |
| 5 | News slider | ≥3 articles with titles and dates |
| 6 | Global offices | All 7 office locations in footer |
| 7 | Social media links | LinkedIn, Twitter, Medium, YouTube with `target="_blank"` |
| 8 | Cross-page navigation | CONTACT button → `/contact-us` with "Get in touch" heading |

> **Expansion (Phase 2):** Following additional site exploration via Playwright MCP, 12 more scenarios were added to deepen coverage of homepage navigation and contact form structure.

| 9 | About dropdown items | All 6 sub-links: About Us, Meet the Team, Memberships, Social Impact, In the Community, FAQs |
| 10 | News & Insights dropdown items | All 4 sub-links: B2C2 in the News, Press Releases, Events, Insights |
| 11 | Join B2C2 dropdown items | All 2 sub-links: Our Values, Careers |
| 12 | Page title and meta description | Title "Home \| B2C2", meta contains "liquidity provider" |
| 13 | Header logo | Logo links to "/", logo image visible |
| 14 | Footer sitemap links | Validates key footer links for Solutions, About, News, Careers sections |

---

### Scenario 2: Contact Page Form & Structure (`contact_form.feature`)

**What**: Validates the contact form structure, all required fields, three custom dropdown menus (team, organisation type, trading volume), dropdown interaction behaviour, media enquiry email, and client-side form validation.

**Why**:
- Tests the primary conversion page — most business-critical interactive element
- Exercises custom Finsweet dropdown components (non-trivial to automate)
- Verifies client-side validation without submitting to avoid spamming production
- 18 scenarios (8 `@smoke` + 10 `@regression`) cover structure and behaviour

**Scenarios implemented**:

| # | Scenario | Tag | Key verification |
|---|----------|-----|-----------------|
| 1 | Page heading & instruction | @smoke | "Get in touch" heading, form instruction text |
| 2 | Required input fields | @smoke | Name, email, company, position required; textarea present |
| 3 | Team dropdown | @smoke | 5 department options |
| 4 | Organisation dropdown | @smoke | 5 business categories |
| 5 | Trading volume dropdown | @smoke | 7 volume ranges |
| 6 | Team dropdown interaction | @regression | Opens on click, options visible, closes on re-click |
| 7 | Media enquiries | @smoke | Correct email (B2C2@eternapartners.com) |
| 8 | Valid input acceptance | @regression | Form accepts valid data; submit button enabled |
| 9 | Required field validation | @regression | Missing required field triggers validation error |
| 10 | Organisation dropdown interaction | @regression | Opens on click, "Exchange" visible, closes on re-click |
| 11 | Trading volume dropdown interaction | @regression | Opens on click, "<$1m" visible, closes on re-click |
| 12 | Contact page title | @smoke | Page title "Contact \| B2C2" |
| 13 | reCAPTCHA widget presence | @smoke | `.g-recaptcha` element present on form |
| 14 | Success/error messages hidden by default | @regression | Both containers exist in DOM but are not visible |
| 15 | Form field placeholders | @regression | Name, email, company, position fields show correct hint text |
| 16 | Malformed email rejection | @regression | Form rejects email without valid format (e.g. "notanemail") |
| 17 | Email without domain | @regression | Form rejects email missing domain (e.g. "user@") |
| 18 | Required field validation (Outline) | @regression | Scenario Outline — validates each required field individually (4 examples) |

---

### Scenario 3: Accessibility Compliance (`accessibility.feature`)

**What**: Runs axe-core accessibility scans on key pages and reports WCAG violations at critical and serious severity levels.

**Why**:
- Automated accessibility scanning catches common WCAG violations early
- Documents known third-party/platform issues that are outside our control
- Alerts on any new regressions introduced beyond the known baseline

**Known violations found** (as of 2026-04-24):
- `html-has-lang` (serious): HTML document missing `lang` attribute — both pages
- `link-name` (serious): Links without discernible text — both pages
- `nested-interactive` (serious): Nested interactive controls — both pages
- `aria-hidden-focus` (serious): Focusable elements inside `aria-hidden` containers — homepage only

These are Webflow platform-level issues that B2C2 would need to address in their CMS configuration. Tests document and exclude them to maintain a green build while still catching new regressions.

**Scenarios implemented**:

| # | Scenario | Tag | Key verification |
|---|----------|-----|-----------------|
| 1 | Homepage accessibility | @regression | No new critical/serious violations beyond known issues |
| 2 | Contact page accessibility | @regression | No new critical/serious violations beyond known issues |

---

## Cross-Cutting Test Concerns

### Cookie Consent Handling

Every test must dismiss the Termly cookie consent banner before interacting with the page.

**Strategy**: Create a shared Cucumber step (`@Given the user has dismissed the cookie consent`) and implement it in a reusable method on `BasePage`:
- Wait for the Termly consent banner to appear
- Click the "Accept All" or dismiss button
- Wait for the banner to disappear
- If no banner appears within timeout, continue (banner may be cached)

### WebDriver Configuration

- **Browser**: Chrome (headless by default)
- **Window size**: 1920x1080 (desktop default)
- **Timeouts**: 10s implicit wait, 15s explicit wait for dynamic elements
- **Page load strategy**: `normal` (wait for full load including Webflow scripts)

### Selector Strategy (Priority Order)

1. `href` attribute selectors (`a[href="/about/faqs"]`) — most stable
2. `id` selectors (`#email`, `#Company`) — stable but not always available
3. CSS class selectors (`.heading-1-2`) — use sparingly, Webflow classes can change
4. XPath — last resort for complex hierarchical queries

### Footer Links as Fallback

The footer (`div.footer.bottom`) contains a complete sitemap with all page links. If header dropdown navigation proves flaky (hover timing issues), use footer links instead — they are always visible and don't require hover interactions.
