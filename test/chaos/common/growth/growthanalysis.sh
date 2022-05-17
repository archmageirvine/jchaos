#!/bin/bash
PTS=1000
javac GrowthAnalysis.java

java sundry.GrowthAnalysis -c ${PTS} 50 | tee coverage.dat
gnuplot <<EOF
set term gif
set ylabel "growth rate"
set xlabel "turns to reach 50% coverage"
set nokey
set output "coverage.gif"
plot "coverage.dat" using 2:1 with linesp
set output "death.gif"
set xlabel "deaths(%)"
plot "coverage.dat" using 3:1
EOF

for t in 10 20 25 30 40 50 60 70 75 80 90 100; do
    echo "Doing rate $t"
    java sundry.GrowthAnalysis -t ${t} >tvg${t}.dat
done
gnuplot <<EOF
set term gif
set xlabel "turn number"
set ylabel "number of growth cells"
set output "count.gif"
plot "tvg10.dat" title "10%", "tvg20.dat" title "20%", "tvg30.dat" title "30%", "tvg40.dat" title "40%", "tvg50.dat" title "50%", "tvg60.dat" title "60%", "tvg70.dat" title "70%", "tvg80.dat" title "80%", "tvg90.dat" title "90%", "tvg100.dat" title "100%"
set term dumb
set output "count.txt"
plot "tvg25.dat" title "25%", "tvg50.dat" title "50%", "tvg75.dat" title "75%", "tvg100.dat" title "100%"
EOF

