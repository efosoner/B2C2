# Project Guidelines

## Overview

This is a Java test automation project for the [B2C2 website](https://www.b2c2.com/). It uses Cucumber (BDD) with Selenium WebDriver to verify key website functionalities. See `.github/specs/overview.md` for the full exercise requirements.

## Architecture

- **Cucumber BDD**: Feature files in Gherkin syntax define test scenarios; step definitions in Java implement them.
- **Page Object Model**: Each page/component of the B2C2 site should have a corresponding page object class that encapsulates element locators and interactions.
- **WebDriver management**: Selenium 4.6+ includes Selenium Manager for automatic browser driver setup. No external driver management library needed. Support headless execution by default.

### Recommended project layout

```
src/
  test/
    java/
      stepdefs/         # Cucumber step definition classes
      pages/            # Page Object classes
      runners/          # Cucumber test runner(s)
      hooks/            # Cucumber @Before/@After hooks (WebDriver lifecycle)
    resources/
      features/         # .feature files (Gherkin)
```

## Build & Test

This project uses Maven. Key commands:

```bash
# Run all tests
mvn clean test

# Run a specific feature file
mvn test -Dcucumber.features="src/test/resources/features/homepage_structure.feature"

# Run scenarios by tag
mvn test -Dcucumber.filter.tags="@smoke"
```

## Conventions

- **One feature file per area of functionality** — keep scenarios focused and independent.
- **Step definitions should delegate to page objects** — steps call page object methods, never interact with WebDriver directly.
- **Use Cucumber tags** (`@smoke`, `@regression`) to categorize scenarios.
- Feature files use declarative language describing user intent, not imperative UI actions (prefer "When the user navigates to the About page" over "When the user clicks the About link in the nav bar").
- Dependencies are managed in `pom.xml` — use `cucumber-java`, `cucumber-junit-platform-engine` (or `cucumber-junit`), `selenium-java` (4.6+ includes Selenium Manager for automatic browser driver management).

## Copilot Customization Map

Reference docs for creating AI agent configurations live in `.github/context/`. Use them as blueprints when creating new files.

### File types and where to create them

| What | Blueprint | Create files at | Format |
|------|-----------|-----------------|--------|
| **Project-wide instructions** | `context/agent-instructions.md` | `.github/copilot-instructions.md` (this file) or `AGENTS.md` at root — pick one, not both | Markdown |
| **File-specific instructions** | `context/instructions.md` | `.github/instructions/*.instructions.md` | Markdown with YAML frontmatter (`description`, optional `applyTo` glob) |
| **Reusable prompt templates** | `context/prompts.md` | `.github/prompts/*.prompt.md` | Markdown with YAML frontmatter (`description`, optional `agent`, `model`, `tools`) |
| **Custom agents** | `context/agents.md` | `.github/agents/*.agent.md` | Markdown with YAML frontmatter (`description`, `tools`, optional `model`, `agents`, `handoffs`) |
| **Agent skills** | `context/skills.md` | `.github/skills/<name>/SKILL.md` + `scripts/`, `references/`, `assets/` subdirs | Markdown with YAML frontmatter (`name`, `description`); folder name must match `name` field |
| **Lifecycle hooks** | `context/hooks.md` | `.github/hooks/*.json` | JSON with `hooks` object keyed by event (`SessionStart`, `PreToolUse`, `PostToolUse`, etc.) |

### Quick-start patterns

**File-specific instruction** (auto-attaches to matching files):
```yaml
# .github/instructions/cucumber-features.instructions.md
---
description: "Use when writing or editing Cucumber .feature files. Covers Gherkin style and tagging."
applyTo: "**/*.feature"
---
Write scenarios in declarative style. Tag with @smoke or @regression. One Feature per file.
```

**Custom agent** (specialist persona with restricted tools):
```yaml
# .github/agents/test-writer.agent.md
---
description: "Use when writing Cucumber scenarios and step definitions for B2C2 website tests."
tools: [read, edit, search, execute]
---
You are a test automation specialist. Write Cucumber BDD tests following the Page Object Model.
```

**Prompt template** (reusable one-shot task):
```yaml
# .github/prompts/new-feature-file.prompt.md
---
description: "Scaffold a new Cucumber feature file with scenario outline"
agent: "agent"
---
Create a new .feature file for the given functionality. Include a Scenario Outline with Examples table.
```

**Skill** (multi-step workflow with bundled scripts):
```
.github/skills/browser-test/
├── SKILL.md              # name: browser-test, description: "Run Selenium tests..."
├── scripts/run-tests.sh  # mvn clean test wrapper
└── references/selectors.md
```

**Hook** (deterministic enforcement):
```json
// .github/hooks/pre-test-lint.json
{
  "hooks": {
    "PreToolUse": [{
      "type": "command",
      "command": "mvn validate",
      "timeout": 30
    }]
  }
}
```

### Key rules

- **Instructions**: keyword-rich `description` enables on-demand discovery; `applyTo` auto-attaches on file create/edit.
- **Agents**: minimal `tools` list — only what the role needs. Use `user-invocable: false` for subagent-only agents.
- **Skills**: folder name must match the `name` field in SKILL.md. Keep SKILL.md under 500 lines; use `references/` for detail.
- **Prompts vs Skills**: prompts are single focused tasks; skills are multi-step workflows with bundled assets.
- **Hooks**: use for deterministic enforcement (blocking dangerous commands, forcing validation), not for guidance — that's what instructions are for.
