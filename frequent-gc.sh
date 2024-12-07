#!/bin/bash

# Set the maximum heap size to 64 MB
MAX_HEAP_SIZE=4m

# Set garbage collector options for frequent garbage collection
GC_OPTIONS="-XX:+UseSerialGC -XX:NewSize=16m -XX:MaxNewSize=16m -XX:+PrintGCDetails"

# Run the Java game with the specified options
java -Xmx$MAX_HEAP_SIZE $GC_OPTIONS -cp build/classes/java/main hackathon.JavaRuntimeInvaders

