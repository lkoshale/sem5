#!/bin/bash

java Main < $1 >out.miniRA

java -jar kgi.jar < out.miniRA

