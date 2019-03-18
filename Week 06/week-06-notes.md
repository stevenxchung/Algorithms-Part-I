## Week 6: Hash Tables

> We begin by describing the desirable properties of hash function and how to implement them in Java, including a fundamental tenet known as the *uniform hashing assumption* that underlies the potential success of a hashing application. Then, we consider two strategies for implementing hash tables—*separate chaining* and *linear probing*. Both strategies yield constant-time performance for search and insert under the uniform hashing assumption. We conclude with applications of symbol tables including sets, dictionary clients, indexing clients, and sparse vectors.

### Hash Tables
* Another approach to implementing symbol tables that can also be very effective in a practical application is hashing
* Hashing involves using a **hash function** - a method for computing array index from a key
* When using a hash function we want to scramble the keys uniformly to produce a table index:
  * Efficiently computable
  * Each table index equally likely for each key
* Some examples of bad hashing:
  * Using the first three digits of a phone number
  * Using the first three digits of SSN
* Some examples of good hashing:
  * Using the last digits of a phone number or SSN
* We also need a different approach for each key type
* All Java classes inherit a method `hashCode()` which returns a 32-bit `int`
* In general if `x.equals(y)`, then `(x.hashCode() == y.hashCode())`
* It is highly desirable to have `!x.equals(y)`, then `(x.hashCode() != y.hashCode())`
* The default implementation for `hashCode()` uses the memory address of x

* Java implementation is different for `Integer`, `Boolean`, `Double`, etc. below is an implementation of `hashCode()` for the `Integer` type:
```java
public final class Integer {
  private final int value {
    // ...
    public int hashCode() {
      return value;
    }
  }
}
```

* The standard recipe for hash code design is as follows:
  * Combine each significant field using the *31 * x + y* rule
  * If field is a primitive type, use wrapper type `hashCode()`
  * If field is null, return `0`
  * If field is a reference type, use `hashCode()`
  * If field is an array, apply to each entry
