# Local AEM with Gradle

1. Configure AEM quickstart.jar and licence URLs

`sh gradlew props`

2. Create and start instance:

`sh gradlew instanceSetup`

3. Recreate instance from scratch:

`sh gradlew instanceResetup -Pforce`

4. Tail instances logs:

`sh gradlew instanceTail`

Please refer to [Gradle AEM Plugin documentation](https://github.com/Cognifide/gradle-aem-plugin) to see full list of features available. 

