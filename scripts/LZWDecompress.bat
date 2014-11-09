@echo off

cd project/bin

set file=%1
java Algorithms/LZW + < files/%file%.bin
