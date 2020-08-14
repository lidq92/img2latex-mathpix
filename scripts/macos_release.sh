#!/usr/bin/env bash

APP_NAME=Image2LaTeX.app

APP_PATH=./Image2LaTeX.app/Contents

if [[ ! -f build.gradle ]]; then
  cd ../releases || exit
else
  cd ./releases || exit
fi

mkdir -p $APP_PATH $APP_PATH/MacOS $APP_PATH/Resources

cp ../macos/Info.plist $APP_PATH/Info.plist

cp ../macos/Assets.car $APP_PATH/Resources/Assets.car

cp ../src/main/resources/icons/macos.icns $APP_PATH/Resources/AppIcon.icns

FILENAME="$(echo ./*macos.zip)"

unzip -q "$FILENAME"

rm -f "$FILENAME"

mv ./Image2LaTeX-macos/* $APP_PATH/MacOS

rm -rf ./Image2LaTeX-macos

VERSION="$(echo "$FILENAME" | cut -d'-' -f2)"

if [[ "$OSTYPE" == "darwin"* ]]; then
  sed -i "" "s/0.0.0/$VERSION/g" ./$APP_NAME/Contents/Info.plist
else
  sed -i "s/0.0.0/$VERSION/g" ./$APP_NAME/Contents/Info.plist
fi

touch Image2LaTeX.app

zip -r -q "$FILENAME" ./$APP_NAME

rm -rf ./"$APP_NAME"
