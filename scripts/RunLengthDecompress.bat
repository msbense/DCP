@echo off

cd project/bin

set file=%1
java Algorithms/RunLength + < files/%file%.bin
