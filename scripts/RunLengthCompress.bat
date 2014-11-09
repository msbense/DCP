@echo off

cd project/bin

set file=%1
java Algorithms/BinaryDump 32 < files/%file%.txt | java Algorithms/RunLength - 
