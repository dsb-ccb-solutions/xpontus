#!/bin/bash
APPHOME=`echo $PWD`
echo $APPHOME
java -Dxpontus.home="$APPHOME" -Dclassworlds.conf="$APPHOME/etc/xpontus.conf" -cp boot/classworlds-1.1.jar org.codehaus.classworlds.Launcher $*;
