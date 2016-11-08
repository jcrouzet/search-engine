#!/bin/bash

# the main idea is to reduce the name of the files for example for the first file it will be 1.txt instead of #20150101_00b667339f32fcff37db6e89aea53065.txt this processing will help us to reduce the size of the index later

declare -i counter=0

for file in /home/amal/search-engine/project/data/corpus/*
do
counter=$((counter+1))
    cp  -r $file /home/amal/search-engine/project/data/corpus_reduit/$counter.txt
    rm $file
done

