#!/bin/sh
find ./src -type f -name "*.java" | xargs javadoc -d doc
