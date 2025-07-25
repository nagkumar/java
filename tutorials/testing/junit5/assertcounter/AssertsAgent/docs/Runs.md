gradle --rerun-tasks clean jar test
gradle --rerun-tasks test
gradle --rerun-tasks clean jar test
gradle publis
gradle --rerun-tasks clean build

gradle --rerun-tasks clean test : this would fail because jar file of agent does not exist