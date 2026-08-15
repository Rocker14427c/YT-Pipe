#!/usr/bin/env bash
set -euo pipefail

# The fork is self-contained; the client and extractor are tracked directories.
# Release maintainers can still edit the localized changelog before committing.
if [[ -n "${1:-}" ]]; then
    changelog="fastlane/metadata/android/en-US/changelogs/$1.txt"
    if [[ -f "$changelog" && -t 0 ]]; then
        ${EDITOR:-vi} "$changelog"
    fi
fi

git add .
git commit -m "Prepare PipePipe release ${1:-}" || true
git push origin HEAD
