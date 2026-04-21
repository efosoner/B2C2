---
name: site-exploration
description: "Explore the B2C2 website using Playwright MCP. Use for mapping pages, discovering testable functionalities, capturing selectors, and building site exploration reports."
---

# Site Exploration Skill

Systematically explore https://www.b2c2.com/ and produce a structured exploration report.

## When to Use

- Before writing any tests, to discover what the site contains
- When adding tests for new pages or features
- When selectors may have changed and need updating

## Procedure

1. Launch browser via Playwright MCP: navigate to `https://www.b2c2.com/`
2. Handle cookie consent — accept or dismiss any banner
3. Follow the [exploration checklist](./references/exploration-checklist.md)
4. For each page discovered, fill in the [site map template](./assets/site-map-template.md)
5. Save the completed report to `.github/specs/site-exploration-report.md`

## Tools Required

- Playwright MCP (`playwright/*`) for browser automation
- `read` and `edit` for saving the report

## Output

A markdown file at `.github/specs/site-exploration-report.md` following the site map template format.
