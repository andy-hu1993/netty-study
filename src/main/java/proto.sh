#!/bin/sh
protoc -I=. --java_out=. ./${1}