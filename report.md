# Report for assignment 3

## Project

Name: Anne Haaker, Alex Gunnarsson, Hugo Tricot, Juan Bautista Lavagnini Portela

URL: https://github.com/haaker1/mockito 

The project is mockito, which is a tool used for mock-testing Java projects.

## Onboarding experience

The could be built and run as documented in the project README. To build the project, JDK had to be upgraded to JDK 17 which was easily done from the JDK documentation. In addition, gradle had to be installed, but it was installed automatically when running the build script. The build concluded without errors, but there were some warnings such as "OpenJDK 64-bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended". However, this did not seem to affect the build since it concluded successfully. All the tests passed on our systems.

## Complexity

The complexity measurement tool lizard was run on the code base to identify four large functions. The cyclomatic complexity of these functions was also counted by hand. 

These four large functions were:
* wrap (in the ConstructorShortcut class in MockMethodAdvice), with NLOC = 234 and CC = 27. Manual count CC = 27. 
* mockClass (in SubclassBytecodeGenerator) with NLOC = 149 and CC = 34. Manual count CC = 34. The purpose of the function is to create a mock class.
* adjustModuleGraph (in ModuleSystemFound class in ModuleHandler) with NLOC = 123 and CC = 13. Manual count CC = 13 if not counting throws, otherwise 10. The function is used to adjust a module graph of a source module so that a mock can be created.
* InlineDelegateByteBuddyMockMaker (constructor for class with identical name) with NLOC = 119 and CC = 24. Manual count CC = 15 if not counting throws, otherwise 13. This function is a constructor for the class with the same name, which is used to mock final types and methods and avoid creating a sub-class when mocking.



1. What are your results for five complex functions?
   * Did all methods (tools vs. manual count) get the same result?
   * Are the results clear?
2. Are the functions just complex, or also long?

Both long and complex, however there are some other functions that are complex but not as long (ReturnValueFor in ReturnsEmptyValues class with CC=23 and NLOC=48, and matches in ArrayEquals with CC=21 and NLOC=25).

3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?

Exceptions are not taken into account in the measurements for the tools, but are shown for manually calculated measurements. There, exceptions are thought of as an exit point, which lowers the CC.
5. Is the documentation clear w.r.t. all the possible outcomes?

The documentation is lacking for all of the functions...

## Refactoring

Plan for refactoring complex code:

Estimated impact of refactoring (lower CC, but other drawbacks?).

Carried out refactoring (optional, P+):

git diff ...

## Coverage

### Tools

The tool used to measure code coverage was jacoco. It was already integrated in the existing build environment which made it easy to use with the command `./gradlew coverageReport`. This generated a html file with information about the coverage for all the functions in the project. 



Document your experience in using a "new"/different coverage tool.

How well was the tool documented? Was it possible/easy/difficult to
integrate it with your build environment?

### Your own coverage tool

Show a patch (or link to a branch) that shows the instrumented code to
gather coverage measurements.

The patch is probably too long to be copied here, so please add
the git command that is used to obtain the patch instead:

git diff ...

What kinds of constructs does your tool support, and how accurate is
its output?

### Evaluation

1. How detailed is your coverage measurement?

2. What are the limitations of your own tool?

3. Are the results of your tool consistent with existing coverage tools?

## Coverage improvement

Show the comments that describe the requirements for the coverage.

Report of old coverage: [link]

Report of new coverage: [link]

Test cases added:

git diff ...

Number of test cases added: two per team member (P) or at least four (P+).

## Self-assessment: Way of working

Current state according to the Essence standard: ...

Was the self-assessment unanimous? Any doubts about certain items?

How have you improved so far?

Where is potential for improvement?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?