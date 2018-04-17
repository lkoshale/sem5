#!/bin/bash

gnuplot -e "plot 'outfile.txt' using 1:2 with lines"
P1=$!
wait $P1

