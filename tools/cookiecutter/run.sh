#!/usr/bin/env sh
if [[ $@ == *"gradle-plugin"* ]]; then
  output_folder="gradlePlugins"
else
  printf "Where should this module go?\nPROJECT_DIR/modules/"
  read where
  output_folder="modules/$where"
fi

docker run -it --rm --name running-cookiecutter --volume `pwd`/templates:/root/.cookiecutters --volume `pwd`/../../$output_folder:/tmp/output cookiecutter "$@" -o /tmp/output module_path=$output_folder
