# Exploration Checklist

Use this checklist when exploring the B2C2 website. Check each item for every page visited.

## Per-Page Checks

- [ ] Record URL, page title, and meta description
- [ ] Screenshot at desktop width (1920px)
- [ ] Screenshot at mobile width (375px)
- [ ] List all navigation links (header and footer)
- [ ] Identify interactive elements: buttons, forms, dropdowns, accordions, tabs
- [ ] Capture CSS selectors for each key element
- [ ] Check for `data-testid` or `data-qa` attributes
- [ ] Note any dynamic content (lazy loading, AJAX, animations, carousels)
- [ ] Note any modals, popups, or overlays triggered on load or scroll
- [ ] Check for cookie consent banner and how to dismiss it

## Site-Wide Checks

- [ ] Map full navigation tree (which pages link to which)
- [ ] Identify external links vs internal links
- [ ] Note third-party integrations (analytics scripts, chat widgets, embedded iframes)
- [ ] Check HTTP response codes for all discovered URLs
- [ ] Note any pages behind authentication or gating
- [ ] Test basic keyboard navigation (tab order, focus visibility)

## Test Candidate Evaluation

For each page/feature, assess:
- **Stability**: Is this content likely to change frequently? (Avoid flaky test targets)
- **Testability**: Are there clear success/failure indicators?
- **Value**: Does testing this catch meaningful regressions?
- **Complexity**: How many interactions are needed to test it?

Rate each candidate as: High / Medium / Low suitability for automation.
