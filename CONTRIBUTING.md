# Contributing to UniBank

Thanks for your interest! UniBank is a personal portfolio project, so contributions are welcome in the form of bug reports, suggestions, and small fixes.

## How to Contribute

### Reporting Bugs

Please [open an issue](https://github.com/j0hn-char/UniBank/issues/new?template=bug_report.md) and include:
- A clear description of the problem
- Steps to reproduce
- Expected vs. actual behavior
- Your Java and MySQL version

### Suggesting Features

[Open a feature request](https://github.com/j0hn-char/UniBank/issues/new?template=feature_request.md) describing what you'd like to see and why it would be useful.

### Submitting Pull Requests

1. Fork the repository and create a branch from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. Make your changes, keeping them focused and minimal.

3. Ensure the project builds and tests pass:
   ```bash
   ./mvnw clean verify
   ```

4. Open a pull request against `main` with a clear description of what you changed and why.

## Code Style

- Follow standard Java conventions
- Use Lombok annotations where appropriate to reduce boilerplate
- Keep controllers thin — business logic belongs in the service layer
- Write meaningful commit messages

## Questions?

Feel free to open an issue if you have any questions about the codebase.
