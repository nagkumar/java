GitHub packages article should cover

1. GitHub does not support delete of repo from gradle and maven
2. it does not allow override of packages other than snapshots
3. it is always good tasks.named("publish") {
   outputs.upToDateWhen { false }
   }
4. how to make packages published are public to view and also private or public to publish
5. how to show package related info in published artifact. what file should contain the content
6. Using the simpler plugin to publish - https://github.com/vanniktech/gradle-maven-publish-plugin
7. packages does not need user/pwd for both publish and reading to that way it is limited
8. talk about another option to publish with git protocol is jitpack repos
