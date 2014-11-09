@echo off

cd project/bin

set file=%1
java Algorithms/Huffman - < files/%file%.txt
