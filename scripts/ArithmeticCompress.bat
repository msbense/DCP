@echo off

cd project/bin

set file=%1
set file2=%2
java Arithmetic/AdaptiveArithmeticCompress files/%file%.txt files/%file2%.bin
