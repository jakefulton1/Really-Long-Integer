For extra credit, I made ReallyLongInt3, which does everything ReallyLongInt2 can, but also accepts and handles negative numbers. To test it, I made the class ExtraCredRLITest3.java, which compiles and runs properly without any logic errors.
Doing all this required changing the string and long constructors to create negative numbers, changing the add, subtract, and multiply functions to handle every case involving completing operations with negative numbers, in addition to all of their recursive functions. I also changed the compareTo function to handle negative numbers. I didn’t have to change the equals function, but I did slightly anyway. I also made a function called positiveCopy() which returns a ReallyLongInt3 object that is a positive version of the original object. I commented throughout the class, hopefully the comments are helpful.
