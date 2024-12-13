# Contribution Guide

Welcome to the QUALI-DEV-ORDER-FLOW project! This document outlines the rules and best practices to follow for effective collaboration on this project.

## General Rules

1. **Work with GitFlow**:
   - All new features must be developed in a **feature** branch based on `develop`.
   - Critical fixes should be made in a **hotfix** branch based on `main`.

2. **Respect the existing codebase**:
   - Follow the coding standards and style conventions used in the project.
   - Perform unit and integration tests before submitting changes.

## Collaboration Process

1. **Branch creation**:
   - Branch names should follow the format: `feature/feature-name` or `hotfix/fix-description`.
   - Example: `feature/product-management`.

2. **Commit messages**:
   - Use a clear format for commit messages:
     ```
     [TYPE] Concise description
     ```
     - **TYPE**: `feat` (new feature), `fix` (bug fix), `docs` (documentation), `chore` (technical task).
     - Example: `[feat] Add an endpoint to retrieve products.`

3. **Code review**:
   - All changes must go through a pull request.
   - Assign a reviewer and provide context in the pull request description.
   - A pull request must have at least 3 reviewers' approvals to be merged.
   - Example: `This pull request implements the product management service.`

4. **Mandatory tests**:
   - Each contribution must include tests (unit, integration).
   - Run the tests before submitting your pull request:
     ```bash
     ./gradlew test
     ```

5. **Documentation**:
   - If you add a new feature, update the corresponding documentation in the `doc/` folder.

## Deployment Process

- All changes must be merged into `develop` and validated before being included in `main`.