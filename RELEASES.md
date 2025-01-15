# Release Process

1. Update the version in `build.gradle.kts`
2. Update the version in `library/src/commonMain/kotlin/Constants.kt`
3. PR to `main` branch
4. Create a new release on GitHub on Main
5. GitHub Actions will publish to Maven
6. Release the new version on https://central.sonatype.com/
