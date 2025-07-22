Use this project code to write tech article that talks about how to write unit tests using JUnit5 for legacy code that has System.out and System.err strings printed to console.

Firs talk about the problem with legacy code and constriant of not to touch/change any legacy code and still test all the System.out and System.err messages

Show 3 ways to do it

1. Replace .out and .err before test and test with our own outputstreams, use the code in first folder and subfolders
2. send is using log4j, i.e replace these .out and .err streams with Log4J Output stream and then append logcapter to assert
3. SL4j way

Think yourself as worlds best technical author who is understood as the most simplisit, best presenter to simplify techhnical conpcet
Make sure this article on medium and further posted on linkedin receives best likes, most interactive comments
