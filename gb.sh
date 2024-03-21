#!/bin/bash -ex
CodeBase=/data/code
cd $CodeBase
mkdir -p $JOB_NAME
cd $JOB_NAME

echo

[ "$PWD" != "/" ] && rm -rf *

branch=main

if [ $PackageName = com.universe.messenger ]; then
if [ $AppName = GBWhatsApp ]; then
branch=gb_universe
elif [ $AppName = WhatsApp2Plus ]; then
branch=plus_universe
elif [ $AppName = OBWhatsApp ]; then
branch=ob_universe
fi
elif [ $AppName = GBWhatsApp ]; then
branch=main
elif [ $AppName = WhatsApp2Plus ]; then
branch=plus_aft
elif [ $AppName = OBWhatsApp ]; then
branch=ob_aft
fi
echo $branch

repo=gbwhatsapp_$Base

if ! `cd appinit 2>/dev/null`; then
git clone git@gitlab.adsconflux.xyz:tianyejun/$repo.git -b $branch
cd $repo
git clone git@gitlab.adsconflux.xyz:malinkang/gb_build_script.git
python gb_build_script/config_gen.py "$Changelog" "$PackageName" "$VersionName" "$VersionCode" "$Base" "$UploadUrl" "$Target" "$VersionCodeSuffix"
python gb_build_script/main.py --config gb_build_script/config.yml


fi