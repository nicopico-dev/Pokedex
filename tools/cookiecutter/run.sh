#!/usr/bin/env sh
if [[ $@ == *"gradle-plugin"* ]]; then
  output_folder="gradlePlugins"
else
  output_folder="modules"
fi

docker run -it --rm --name running-cookiecutter --volume `pwd`/templates:/root/.cookiecutters --volume `pwd`/../../$output_folder:/tmp/output cookiecutter "$@" -o /tmp/output
