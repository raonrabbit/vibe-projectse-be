# ship skill

Create a branch, commit changes, push, and open a PR for review — never push directly to main.

## Git Convention

**Commit format:** `<type>(<scope>): <subject>`

**Branch format:** `<type>/<short-kebab-description>`

**Types:**
| Type | When to use |
|------|-------------|
| `feat` | New feature or behavior |
| `fix` | Bug fix |
| `chore` | Build, config, tooling, dependency updates |
| `docs` | Documentation only |
| `style` | Formatting, no logic change |
| `refactor` | Restructure without behavior change |
| `test` | Add or update tests |
| `ci` | CI/CD pipeline changes |

**Scopes:**
| Scope | Path |
|-------|------|
| `api` | `devnews/api/` |
| `domain` | `devnews/domain/` |
| `collector` | `devnews/collector/` |
| `config` | root config files (`.claude/`, `build.gradle.kts`, `gradle/`, `docker-compose.yml`, etc.) |
| `deps` | dependency changes (`gradle/libs.versions.toml`) |
| `docs` | `devnews/docs/`, `docs/` |

**Subject rules:**

- Lowercase, imperative mood ("add" not "added")
- No trailing period, max 72 characters

**Examples:**

```
feat(api): add news article list endpoint          → branch: feat/news-article-list
fix(domain): correct article publish date mapping  → branch: fix/article-publish-date
chore(config): add springdoc openapi dependency    → branch: chore/springdoc-setup
```

## Execution Steps

1. **Understand the changes:** run `git status` and `git diff`.

2. **Fix — run before anything else.**

    Run `/fix` to verify the build compiles and tests pass.

    **Do not proceed to step 3 until `/fix` completes cleanly** (or the user has explicitly approved skipping).

3. **Group by scope.** Analyze all modified/untracked files and group them into logical commit units. Each group gets:
    - A proposed type + scope (e.g., `chore(config)`)
    - A proposed commit message
    - The list of files it contains

4. **Present the groups and ask the user to select.**
   Show a numbered list like:

    ```
    변경사항을 다음 그룹으로 나눴습니다:

    [1] chore(config): add springdoc and .claude harness
        gradle/libs.versions.toml, devnews/api/build.gradle.kts, .claude/

    [2] feat(api): add news article CRUD endpoints
        devnews/api/src/

    [3] feat(domain): add article entity and repository
        devnews/domain/src/

    전부 포함할까요, 아니면 포함할 번호를 알려주세요. (예: 1 3)
    ```

    Wait for the user's answer before proceeding.

5. **Determine PR grouping.** For the selected groups, decide how many PRs to create:
    - Groups with the **same type + scope** → can share one branch and one PR (multiple commits).
    - Groups with **different type or scope** → each gets its own branch and PR.
    - State the plan explicitly before proceeding, e.g.:
        ```
        PR을 2개로 나눠서 올릴게요:
        PR A — chore/springdoc-setup: [1]
        PR B — feat/article-crud: [2] [3]  ← (같은 feat, 관련 범위)
        ```

6. **For each PR group (repeat steps 6a–6d):**

    6a. **Branch handling:** Check out `main` first, then `git checkout -b <branch-name>`. Exception: if already on a non-main branch that matches this PR group's work, stay on it.

    6b. **Stage and commit each file group (in order):** `git add <specific files>` — never `git add .` or `git add -A`. Commit with the convention format + footer:
    ```
    Co-Authored-By: Claude Sonnet 4.6 <noreply@anthropic.com>
    ```

    6c. **Push the branch:** `git push -u origin <branch-name>`

    6d. **Open a PR** with `gh pr create`: Title = commit message of the primary commit. Body = bullet-point summary of what changed and why. Base branch = `main`.

7. **Return all PR URLs** at the end so the user can review and merge each one.

8. **Return to main:** Run `git checkout main` after all PRs are created.

## Safety Rules

- **Never push to main directly.**
- Never force push.
- Never skip hooks (`--no-verify`).
- Do not stage `.env`, secret files, or unrelated build artifacts.
