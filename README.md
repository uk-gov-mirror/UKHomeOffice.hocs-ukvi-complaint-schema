# hocs-ukvi-complaint-schema
The JSON schema for the UKVI Complaints Management complaint schema.
Contains the schems, examples and validation tests.

## Generating test data
This is a [useful site for generating JSON from JSON schema](https://json-schema-faker.js.org)

## Publishing

### Artifactory
The artifact is published to a Maven repo held in Artifactory. View the `.drone.yml` file for
more information on the commands used in the pipeline and when this is triggered.

### Maven Local
For local development, the module can be published to the 
[Maven local repository](https://docs.gradle.org/current/userguide/publishing_maven.html#publishing_maven:install).

Example command for publishing to Maven local:
```
./gradlew publishToMavenLocal -PartifactVersion=0.0.3
```
