## Quiz 4: Java autoboxing and equals()

*Consider two `double` values **a** and **b** and their corresponding `Double` values **x** and **y**.*
* *Find values such that `(a == b)` is `true` but `x.equals(y)` is `false`*
* *Find values such that `(a == b)` is `false` but `x.equals(y)` is `true`*

A: Since `Double` is an object and `double` is a primitive data type we can have the following:
* a = 0.0, b = -0.0
* a = Double.NaN, a = Double.NaN
