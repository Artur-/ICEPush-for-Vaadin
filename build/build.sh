#!/bin/bash

rm zip-staging/icepush-0.5.5.jar
rm icepush-0.5.6.jar
pushd ../src
jar mcvf ../WebContent/META-INF/MANIFEST.MF ../build/icepush-0.5.6.jar org
jar uvf ../build/icepush-0.5.6.jar icepush-modified.js
popd
pushd classes
jar uvf ../icepush-0.5.6.jar org
popd

cp icepush-0.5.6.jar zip-staging

pushd zip-staging
# Ensure zip manifest version is correct
rm *~ */*~
rm ../icepush-0.5.6.zip
zip ../icepush-0.5.6.zip . -r
popd