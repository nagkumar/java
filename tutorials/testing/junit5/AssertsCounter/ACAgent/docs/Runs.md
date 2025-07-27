gradle --rerun-tasks clean jar test
gradle --rerun-tasks test
gradle --rerun-tasks clean jar test
gradle publish
gradle --rerun-tasks clean build
gradle cleanBuildCache
gradle --rerun-tasks clean test : this would fail because jar file of agent does not exist

git config -l
git config --global user.name nagkumar