# Branching Strategy
This document will describe the branching strategy that we will be using throughout this group project.

## Branch Types

- **Protected main branch**: Only after multiple people review a merge request can anything be pushed to `main`.
- **Development branch**: All other contributors work on feature branches. After doubling checking with the team, they can push into this development branch. Ideally, after finishing a feature.
- **Work branches**: Clear and proper labelling for the branch, this is where majority of a team members work will be, having frequent commits. 


## Git & Version Control Standards

In general, a new branch should be created for each developer task. Multiple developer tasks can be included in one branch if they are closely linked.

Branches should have appropriate naming, e.g:
- feat-sql
- dev-rename file
- bug-not fully deleting

## Commit Messages

Commit messages must be clear, concise, and descriptive.

Examples:
- Created Sql interface, so that other members can start working, will do full implementation now.
- Refactored codebase to have lowercase package names to adhere to grading remarks.
- Corrected bug when renaming category, category is renamed, but not expenses categories.

