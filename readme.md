# Java Challenge by Emanuel Barrera

### What I did

- First, I did a manual test of the api, using the Swagger tool
- Fixed some bugs I found during manual testing
- Added unit tests with junit5, so that I could code using TDD

- Parameterized Departments as an entity
- Added DTO objects for requests and responses
- Added parameters validations
- Added custom exceptions
- Added pagination for GetEmployees and GetDepartments
- Used a logging library (log4j2) and improved logs
- Added cache for the database query used by getEmployees

- Included and configured sonarqube for code analysis, that helped me identify and fix issues
- Included a code coverage plugin for reports (jacoco), that runs when doing `mvn verify`

#### What I would like to improve had I more time

- Add login (i.e with Spring Security)
- Improve unit test coverage, an add integration tests
- Add e2e/automation tests (i.e. using Karate)
- Enhance error handling
- Include versioning management
- Include support for environments

#### My experience in Java

- I have been using Java since I started my career 9 years ago. (the only exception being the ~2 years I worked at Eventbrite, where we used Python)
- The first framework I used professionally was Grails, for about 2 years. Then my company moved to Spring Boot, and it's the framework I've used ever since.
- I'm using Spring Boot at my current job.
