# Report for assignment 3

## Project

Name: Anne Haaker, Alex Gunnarsson, Hugo Tricot, Juan Bautista Lavagnini Portela

URL: https://github.com/haaker1/mockito 

The project is mockito, which is a tool used for mock-testing Java projects.

## Onboarding experience

The could be built and run as documented in the project README. To build the project, JDK had to be upgraded to JDK 17 which was easily done from the JDK documentation. In addition, gradle had to be installed, but it was installed automatically when running the build script. The build concluded without errors, but there were some warnings such as "OpenJDK 64-bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended". However, this did not seem to affect the build since it concluded successfully. All the tests passed on our systems. We are leaning toward continuing on the project.

## Complexity

The complexity measurement tool lizard was run on the code base to identify four large functions. The cyclomatic complexity of these functions was also counted by hand. We ran lizard using the command `lizard src/ -x"./src/tests/*" -l java -T nloc=100` and obtained the following results:

| NLOC | CCN | location                                                                   | file                                                                                         |
|------|-----|----------------------------------------------------------------------------|----------------------------------------------------------------------------------------------|
| 45   | 11  | MockAnnotationProcessor::processAnnotationForMock@33-81                    | @src/main/java/org/mockito/internal/configuration/MockAnnotationProcessor.java               |
| 57   | 14  | InlineBytecodeGenerator::triggerRetransformation@247-306                   | @src/main/java/org/mockito/internal/creation/bytebuddy/InlineBytecodeGenerator.java          |
| 119  | 24  | InlineDelegateByteBuddyMockMaker::InlineDelegateByteBuddyMockMaker@225-348 | @src/main/java/org/mockito/internal/creation/bytebuddy/InlineDelegateByteBuddyMockMaker.java |
| 234  | 27  | MockMethodAdvice::ConstructorShortcut::wrap@387-643                        | @src/main/java/org/mockito/internal/creation/bytebuddy/MockMethodAdvice.java                 |
| 123  | 13  | ModuleHandler::ModuleSystemFound::adjustModuleGraph@170-292                | @src/main/java/org/mockito/internal/creation/bytebuddy/ModuleHandler.java                    |
| 149  | 34  | SubclassBytecodeGenerator::mockClass@126-301                               | @src/main/java/org/mockito/internal/creation/bytebuddy/SubclassBytecodeGenerator.java        |
| 72   | 14  | SerializableMethod::equals@102-173                                         | @src/main/java/org/mockito/internal/invocation/SerializableMethod.java                       |
| 41   | 12  | EqualsBuilder::reflectionEquals@223-270                                    | @src/main/java/org/mockito/internal/matchers/apachecommons/EqualsBuilder.java                |
| 41   | 17  | EqualsBuilder::append@341-387                                              | @src/main/java/org/mockito/internal/matchers/apachecommons/EqualsBuilder.java                |
| 53   | 14  | ValuePrinter::print@23-79                                                  | @src/main/java/org/mockito/internal/matchers/text/ValuePrinter.java                          |
| 25   | 21  | ArrayEquals::matches@17-41                                                 | @src/main/java/org/mockito/internal/matchers/ArrayEquals.java                                |
| 48   | 23  | ReturnsEmptyValues::returnValueFor@106-158                                 | @src/main/java/org/mockito/internal/stubbing/defaultanswers/ReturnsEmptyValues.java          |

From this, we picked out the first 4 functions to count manually: 

| Member         | function                         | lizard CCN | jacoco CCN | CCN manual1 | CCN manual2 |
|----------------|----------------------------------|------------|------------|-------------|-------------|
| Alex           | SerializableMethod::equals       | 14         | 25         | 4           |             |
| Anne           | ArrayEquals::matches             | 21         | 21         |             | 11 (Alex)   |
| Hugo           | EqualsBuilder::append            | 17         | 18         |             |             |
| Juan           |                                  |            |            |             |             |


Both lizard and JaCoCo disagree with regards to CCN, which means they measure it differently. Therefore, it is natural that our own counts disagree, perhaps because of how bitwise operators are treated, etc..

3. What is the purpose of the functions?

| function                         | purpose       |
|----------------------------------|---------------|
| SerializableMethod::equals       | Check if the SerializableMethod object is equal to another object. |
| ArrayEquals::matches             |       |
| EqualsBuilder::append            |       |
|                                  |       |




## Refactoring

Plan for refactoring complex code:

Estimated impact of refactoring (lower CC, but other drawbacks?).

Carried out refactoring (optional, P+):

git diff ...

## Coverage

### Tools

The tool used to measure code coverage was jacoco. It was already integrated in the existing build environment which made it easy to use with the command `./gradlew coverageReport`, although it wasn't properly documented. This generated a html file with information about the coverage for all the functions in the project. 

### Your own coverage tool

**The git command that is used to obtain the patch:**

git diff a10d43ca5cb9377978b2a1fe2888fdeef33cdf79

**What kinds of constructs does your tool support, and how accurate is its output?**

Our tool checks if a branch has been reached. It technically supports all types of branches, but they have to be conciously implemented by the programmer who seeks to measure the coverage. The branch logging is done via function calls, so ternaries have to be converted into if-else statements which call the function to log the branch.

### Evaluation

1. How detailed is your coverage measurement?

The coverage tool outputs the coverage measurement as a percentage of branches visited. In addition, it outputs which branches have been visited and which have not as identified by the branch ID. The tool is dependent on the utilization by the programmer who uses it. The tool does not work well with ternaries, but can be turned into if-else statements which are properly supported. It does take exceptions into account since it counts the if- or catch-blocks leading up to exceptions being thrown. 

2. What are the limitations of your own tool?

There are some limitations of the tool. For example, in if-statements such as `if(a || b)`, it does not measure which of `a` or `b` was true, only if the inside of the if-statement was reached. In such cases there are two ways to reach the if-statement, but we cannot know which way we took. Functionally they are the same branch since it does not matter for the outcome of the if/else statement which of the variables was true. However, it could lead to limitations in testing and make it harder to spot bugs since the coverage tool does not tell us if we have tested both ways to reach the if-statement. To test this, we would have to split up such 'combined' conditionals into several different if-statements. Changing the code would require the instrumentation of the tool to change, to include counting of the new branches that may be created.

3. Are the results of your tool consistent with existing coverage tools?

There were some differences in results between our coverage tool and jacoco. One reason for the differences is that the two tools use different ways of counting. The jacoco tool is more detailed and counts cases such as `if(a || b)` as four different branches (a b, a !b, !a b, !a !b), whereas our tool only counts it as two branches. In addition, jacoco seems to include the coverage of lambda functions and other inner functions, which our tool does not. 



## Coverage improvement

The table below shows the coverage in percentage.

| function (without new tests)     | jacoco coverage | DIY coverage |
|----------------------------------|-----------------|--------------|
| SerializableMethod::equals       | 46%             | 41.7%        |
| ArrayEquals::matches             | 72%             | 91.7%        |
| EqualsBuilder::append            | 94%             | 100%         |
|                                  |                 |              |

Report of old coverage: [link]

Number of test cases added: two per team member (P) or at least four (P+).

| Member         | TC1    | TC2    | TC3    | TC4    |
|----------------|--------|--------|--------|--------|
| Alex           |        |        |        |        |
| Anne           |        |        |        |        |
| Hugo           |        |        |        |        |
| Juan           |        |        |        |        |

Requirements to increase coverage:
* SerializableMethod::equals - 
* ArrayEquals::matches - The current test suite does not cover cases where the wanted object is an int array and the actual given object is something else. It also does not cover cases where the actual given object is null.
* EqualsBuilder::append -
*  - 

| function (with new tests)        | jacoco coverage | DIY coverage |
|----------------------------------|-----------------|--------------|
| SerializableMethod::equals       | 61%             | 58.3%        |
| ArrayEquals::matches             | 80%             |              |
| EqualsBuilder::append            |                 |              |
|                                  |                 |              |

Report of new coverage: [link]


## Self-assessment: Way of working

Current state according to the Essence standard: ...

Was the self-assessment unanimous? Any doubts about certain items?

How have you improved so far?

Where is potential for improvement?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?
