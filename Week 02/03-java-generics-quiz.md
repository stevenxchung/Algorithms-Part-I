## Quiz 3: Java Generics

*Explain why Java prohibits generic array creation.*

A: When we create an array in Java, we must specify the type. However, generic types are unknown at run-time even if they are defined in compile-time (due to type erasure - Java erases the types from all objects at compile-time), this means that two objects with different types could have the same class at run-time. To avoid this issue, Java prohibits generic array creation.
