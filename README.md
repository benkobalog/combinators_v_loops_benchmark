# combinators_v_loops_benchmark
I'm trying to measure how the speed of loops compare against build in combinators in Scala.
The current cases are ArrayBuffer vs Map-Filter vs Collect implementations.

At this point I'm collect seems to be the fastest until 50k elements where loops takes the lead. 
Note: At this point I use lists based implementations for testing the combinators.

Plans (This is mostly a note for myself):
1. Put results here in some organised format. 
2. Check if the loop implementations can be faster.
3. Use some sleep functions to emulate longer operations than a few arithmetic operations.
4. Play with JVM settings, to see how memory constaints affect the results, because I assume that the map based solutions have a lower memory limit.
5. Try to figure out if Scala does some automatic parallelisation for map,filter,collect, and if yes try to measure with and without it.

