#!

cd project/bin

Start=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
java Algorithms/LZW - < files/HighEntropy.txt | java Algorithms/BinaryDump 32
End=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

Time=$[$End-$Start]
echo "Time: " $Time " milliseconds to compress"