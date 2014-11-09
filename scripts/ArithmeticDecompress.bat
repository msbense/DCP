@echo off

cd project/bin

set file=%1
set file2=%2
java Arithmetic/AdaptiveArithmeticDecompress files/%file%.bin files/%file2%.txt
