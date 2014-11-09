#!

cd project/bin

Start=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
java Arithmetic/AdaptiveArithmeticCompress files/text1.txt files/compressed
java Algorithms/BinaryDump 32 < files/compressed
End=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

Time=$[$End-$Start]
echo "Time: " $Time " milliseconds to compress"