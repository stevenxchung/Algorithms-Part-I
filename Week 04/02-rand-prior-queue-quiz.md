## Quiz 2: Randomized priority queue

*Describe how to add the methods **sample()** and **delRandom()** to our binary heap implementation. The two methods return a key that is chosen uniformly at random among the remaining keys, with the latter method also removing that key. The **sample()** method should take constant time; the **delRandom()** method should take logarithmic time. Do not worry about resizing the underlying array.*

A: To implement **sample()** We would have to get a random number (perhaps implement a randomIndex() method which returns an integer between 0 to lengthArray - 1) and select `a[randomIndex()]`. To remove the random key with **delRandom()**, swap element `a[randomIndex()]` with the last element and proceed with the binary heap implementation
