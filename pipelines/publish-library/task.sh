#!/usr/bin/env bash

[ -n "$DEBUG" ] && set -x
set -e
set -o pipefail

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR="$( cd "$SCRIPT_DIR/../../.." && pwd )"

ls
pwd

export VERSION="$( cat ../version/version )"

echo "VERSION: $VERSION"

cd "$PROJECT_DIR"
cd source

ls
pwd

mkdir -p ~/.lein

./go library:publish:release
