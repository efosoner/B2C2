# Site Exploration Report: B2C2

> Generated on: 2026-04-21
> Explorer: web_fetch (static HTML analysis)

## Summary

- **Total pages explored**: 5 (Homepage, About Us, Contact, FAQs, Careers)
- **Platform**: Webflow (data-wf-domain="www.b2c2.com")
- **Navigation structure**: 4 top-level dropdown menus + Contact button + Subscribe popup
- **Cookie consent**: Yes — Termly (`app.termly.io`), must be dismissed before interacting with page content
- **Key observations**: JavaScript-heavy site with custom dropdown components (Finsweet), Flickity carousel, reCAPTCHA on forms

---

## Site Map

### Page: Homepage

- **URL**: https://www.b2c2.com/
- **Title**: Home | B2C2
- **Meta Description**: More than just a liquidity provider, B2C2 is a digital asset pioneer building the ecosystem of the future.

#### Key Elements

| Element | Selector | Type | Notes |
|---------|----------|------|-------|
| Logo | `a.brand img.logo` | link/image | Links to `/` |
| Solutions dropdown | `.w-dropdown` containing `a[href="/solutions"]` | dropdown | Hover-triggered (`data-hover="true"`) |
| About dropdown | `.w-dropdown` containing `a[href="#"]` (About text) | dropdown | Hover-triggered |
| News & Insights dropdown | `.w-dropdown` containing `a[href="/news-and-events"]` | dropdown | Hover-triggered |
| Join B2C2 dropdown | `.w-dropdown` containing `a[href="/join-b2c2"]` | dropdown | Hover-triggered |
| Contact button | `a[href="/contact-us"] .blue-button-text` | link/button | Text: "CONTACT" |
| Subscribe button | `.subscribe_btn .pink-button-text` | button/popup | Opens email subscription form |
| Subscribe email input | `form#wf-form-Email-Form input[name="Name"]` | email input | Placeholder: "Enter email here" |
| Subscribe submit | `form#wf-form-Email-Form input[type="submit"]` | submit | Value: "Subscribe →" |
| News slider | `.blog-slider` | carousel | Flickity-based, 3 slides visible |
| News slide links | `.link-block-4.slide-home` | link | Each links to a news post |
| Hero heading | `h1.heading-59` | text | "More than a liquidity provider..." |
| Hero subheading | `h1.heading-60` | text | Company description |
| Social icons (nav) | `.foo.social.nav a` | links | LinkedIn, Twitter, Medium, YouTube |
| Mobile menu button | `.menu-button-2` | button | 3-line hamburger, visible on medium screens |
| Cookie consent | Termly overlay | modal | Must dismiss first |

#### Navigation Links (from dropdowns)

**Solutions:**
- `/solutions/trading-overview` — Trading Overview
- `/solutions/product` — OTC Products
- `/solutions/market-making-liquidity-provision` — Market Making
- `/solutions/liquidity-partner` — Liquidity Partner
- `/penny` — Stablecoin Swaps

**About:**
- `/about/about-us` — About Us
- `/about/meet-the-team` — Meet the Team
- `/about/membership` — Memberships
- `/about/social-impact` — Social Impact
- `/about/in-the-community` — In the Community
- `/about/faqs` — FAQs

**News & Insights:**
- `/news-events/b2c2-in-the-news` — B2C2 in the News
- `/news-events/press-releases` — Press Releases
- `/news-events/events` — Events
- `/insights` — Insights

**Join B2C2:**
- `/join-b2c2/our-values` — Our Values
- `/join-b2c2/careers` — Careers

#### Dynamic Behavior

- Flickity carousel for news slides (auto-advances, responsive)
- Dropdown menus trigger on hover (desktop) / click (mobile)
- Subscribe popup animates open/close (Webflow interactions via `data-w-id`)

---

### Page: Contact Us

- **URL**: https://www.b2c2.com/contact-us
- **Title**: Contact | B2C2

#### Key Elements

| Element | Selector | Type | Notes |
|---------|----------|------|-------|
| Page heading | `h1.heading-1-2` | text | "Get in touch" |
| Form heading | `h2.para-24-3` | text | "Please complete the form to get in touch" |
| Team selector | `select#What-Best-Describes-Your-Organisation-4` | custom dropdown | Options: Sales, General, Press, Legal, Onboarding |
| Name input | `input#Name[name="Name"]` | text | Required, placeholder: "Enter your name*" |
| Email input | `input#email[name="email"]` | email | Required, placeholder: "Enter your email address*" |
| Organisation type | `select#What-Best-Describes-Your-Organisation-3` | custom dropdown | Options: Exchange, Fund, OTC Desk/Broker, Proprietary Trading Firm, Other |
| Company input | `input#Company` | text | Required |
| Position input | `input#Position-at-company-2` | text | Required |
| Trading volume | `select#What-Best-Describes-Your-Organisation-5` | custom dropdown | Options: <$1m, $1m-$10m, $10m-$50m, ... $500m+, N/a |
| Message textarea | `textarea#Message` | textarea | Optional, max 5000 chars |
| reCAPTCHA | `.g-recaptcha` | widget | Sitekey: `6Lc70xUqAAAAAGS5ZAH0NTTlfFxDRdg_2Cf0Jzq5` |
| Submit button | `input.form-submit-2[type="submit"]` | submit | Value: "Submit →" |
| Success message | `.success-message-5` | div | "Your enquiry has been successfully submitted." |
| Error message | `.error-message-6` | div | "Oops! Something went wrong..." |
| Media enquiries link | `a[href="mailto:B2C2@eternapartners.com"]` | email link | |
| Subscribe section | `.section.subscribe-section` | section | Bottom-of-page email subscribe form |
| Global locations | `.clock-widget` | content blocks | London (HQ), New Jersey, Tokyo, Luxembourg, Singapore, Poland |

#### Dynamic Behavior

- Custom select dropdowns (Finsweet `fs-selectcustom`) replace native `<select>` elements
- reCAPTCHA validation on submit
- Form success/error messages toggle visibility on submit

---

### Page: FAQs

- **URL**: https://www.b2c2.com/about/faqs
- **Title**: FAQs | B2C2 (inferred)

#### Key Elements

| Element | Selector | Type | Notes |
|---------|----------|------|-------|
| FAQ questions | Heading elements (h2) | accordion headers | 12+ FAQ entries |
| FAQ answers | Content below headings | expandable text | Detailed answers with links |

#### FAQ Topics Discovered

1. Who are B2C2?
2. What do you offer as a liquidity provider?
3. Can anyone trade with B2C2?
4. What is the overnight funding rate?
5. Do you have minimum/maximum trade sizes?
6. Equal Employment Opportunities policy
7. B2C2's funding solution
8. What is an OTC liquidity provider?
9. How are you different from other providers?
10. What are CFDs?
11. Margin requirements for CFDs
12. What are Crypto Options?
13. What is PENNY (stablecoin swap)?

---

## Third-Party Integrations

| Integration | Type | Notes |
|------------|------|-------|
| Termly | Cookie consent | `app.termly.io`, UUID: `e932fbfd-e3b1-4ab2-92c9-d1a8ca78ae1d` |
| Google Analytics | Analytics | UA-24539117-16 |
| Google Ads | Marketing | AW-10823134148 |
| Google reCAPTCHA | Form protection | v2, sitekey: `6Lc70xUqAAAAAGS5ZAH0NTTlfFxDRdg_2Cf0Jzq5` |
| Webflow | CMS/Hosting | Webflow-built site with custom CSS classes |
| Flickity | Carousel | News slider on homepage |
| Finsweet | Custom selects | `fs-selectcustom` for dropdown components |
| Google Fonts | Typography | Lato, Rubik, Rubik Mono One |

## Notes & Observations

- All navigation dropdowns use `data-hover="true"` — they open on hover (desktop), which requires mouse-over actions in Selenium
- Cookie consent (Termly) blocks interaction until dismissed — tests MUST handle this first
- reCAPTCHA on contact and subscribe forms prevents automated form submission testing
- Webflow generates class-heavy HTML — prefer `href` attribute selectors over class names for stability
- Mobile navigation uses a hamburger menu (`.menu-button-2`) visible at `medium` breakpoint and below
- Footer contains full sitemap replica with all page links — useful as alternative navigation selectors
