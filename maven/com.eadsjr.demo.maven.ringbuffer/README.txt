This is a ringbuffer data structure example. It is implemented in Java OOP style using the Linked-List approach. It is theoretically datatype generic, but is not tested for that in this example. Some unit tests validate the code consistency. Logging provided by slf4j backed by log4j. You can reduce log verbosity by changing DEBUG to INFO in ./src/main/resources/log4j.properties 

input1.txt contains some working input.
input2.txt contains input that attempts an illegal operation.
input3.txt contians input that provides a negative integer.
input4.txt contains input that has a formatting error.

The pom file is configured to execute the working example by default.

Note: The input format is deliberatly very strict.
