# Security Policy

## Supported Versions

This is a personal portfolio project. Only the latest commit on `main` is actively maintained.

| Version | Supported |
|---------|-----------|
| latest (main) | ✅ |
| older commits | ❌ |

## Reporting a Vulnerability

**Please do not open a public issue for security vulnerabilities.**

If you discover a security issue in UniBank, please report it privately:

1. Go to the [Security tab](https://github.com/j0hn-char/UniBank/security) of this repository.
2. Click **"Report a vulnerability"** to open a private advisory.
3. Describe the vulnerability, steps to reproduce, and potential impact.

I'll do my best to respond within a few days and will keep you updated on the fix.

## Security Notes

This project uses:
- **JWT** for stateless authentication (jjwt 0.12.5)
- **Spring Security** for endpoint protection and password encoding
- **Input validation** via Spring Validation

> ⚠️ This project is intended for educational/portfolio purposes. Do **not** deploy it in production without a thorough security review.
