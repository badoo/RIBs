#!/usr/bin/env bash

set +e

DEVICES=`adb devices | grep -v devices | grep device | cut -f 1`
for device in $DEVICES; do
    echo "$device $@ ..."

    bootanim=""
    failcounter=0
    timeout_in_sec=360

    until [[ "$bootanim" =~ "stopped" ]]; do
      bootanim=`adb -s $device -e shell getprop init.svc.bootanim 2>&1 &`
      if [[ "$bootanim" =~ "device not found" || "$bootanim" =~ "device offline"
        || "$bootanim" =~ "running" ]]; then
        let "failcounter += 1"
        echo "Waiting for emulator to start"
        if [[ $failcounter -gt timeout_in_sec ]]; then
          echo "Timeout ($timeout_in_sec seconds) reached; failed to start emulator"
          exit 1
        fi
      fi
      sleep 1
    done

    adb shell -s $device input keyevent 82 &

    echo "Emulator is ready"
done
