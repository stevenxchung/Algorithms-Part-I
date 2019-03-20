## Quiz 2: Hashing with wrong `hashCode()` or `equals()`

*Suppose that you implement a data type `OlympicAthlete` for use in `java.util.HashMap`:*
* *Describe what happens when you override `hashCode()` but not `equals()`*
* *Describe what happens when you override `equals()` but not `hashCode()`*
* *Describe what happens when you override `hashCode()` but implement `public boolean equals(OlympicAthlete that)` instead of `public boolean equals(Object that)`*

A: For the following cases above:
* When `hashCode()` is overwritten but not `equals()`, then `equals()` will return true only if references to the same object are equal
* When `equals()` is overwritten but not `hashCode()`, then `hashCode()` will not return the correct answer since there could be duplicates in the hash map
* When `hashCode()` is overwritten but `public boolean equals(OlympicAthlete that)` is implemented instead of `public boolean equals(Object that)`, then `public boolean equals(OlympicAthlete that)` will return true only if references to the same `OlympicAthlete` are equal
