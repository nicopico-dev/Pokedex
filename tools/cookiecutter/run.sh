#!/usr/bin/env sh
docker run -it --rm --name running-cookiecutter --volume `pwd`/templates:/root/.cookiecutters --volume `pwd`/../../modules:/tmp/output cookiecutter "$@" -o /tmp/output