#!

cd project/bin

Start=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
java Algorithms/BinaryDump 32 < files/text1.txt | java Algorithms/RunLength - | java Algorithms/HexDump
End=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

Time=$[$End-$Start]
echo "Time: " $Time " milliseconds to compress"