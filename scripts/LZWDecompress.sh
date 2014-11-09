#!

cd project/bin

Start1=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
java Algorithms/LZW - < files/text1.txt | java Algorithms/BinaryDump 32
End1=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')

Time1=$[$End-$Start]

Start2=$(ruby -e 'puts (Time.now.to_f * 1000).to_i')
java Algorithms/LZW - < files/text1.txt | java Algorithms/BinaryDump 32
echo "Time: " $Time1 " milliseconds to compress"