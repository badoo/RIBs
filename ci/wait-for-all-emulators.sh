#!/usr/bin/env bash

set +e

DEVICE_COUNT=3

connected_fail_counter=0
connected_timeout_in_sec=120
current_device_count=0

echo "Waiting for $DEVICE_COUNT devices..."

until [[ ${current_device_count} =~ $DEVICE_COUNT ]]; do
    DEVICES=`adb devices | grep -v devices | grep device | cut -f 1`

    current_device_count=${#DEVICES[*]}
    echo "$current_device_count devices connected out of $DEVICE_COUNT"
    let "failcounter += 1"
    sleep 1

    if [[ $connected_fail_counter -gt connected_timeout_in_sec ]]; then
      echo "Timeout ($connected_timeout_in_sec seconds) reached; failed to start emulators"
      exit 1
    fi
done

for device in $DEVICES; do
    echo "$device $@ ..."

    bootanim=""
    failcounter=0
    timeout_in_sec=360

    until [[ "$bootanim" =~ "stopped" ]]; do
      bootanim=`adb -s ${device} -e shell getprop init.svc.bootanim 2>&1 &`
      if [[ "$bootanim" =~ "device not found" || "$bootanim" =~ "device offline"
        || "$bootanim" =~ "running" ]]; then
        let "failcounter += 1"
        echo "Waiting for emulator to start"
        if [[ ${failcounter} -gt timeout_in_sec ]]; then
          echo "Timeout ($timeout_in_sec seconds) reached; failed to start emulator"
          exit 1
        fi
      fi
      sleep 1
    done

    adb -s ${device} shell input keyevent 82 &

    echo "Emulator is ready"
done
