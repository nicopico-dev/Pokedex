#!/usr/bin/env sh
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 TEMPLATE" >&2
    exit 1
fi
cd tools/cookiecutter
./run.sh $1
cd -