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
| 119  | 24  | InlineDelegateByteBuddyMockMaker::InlineDelegateByteBuddyMockMaker@225-348 | @src/main/java/org/mockito/internal/creation/bytebuddy/InlineDelegateByteBuddyMockMaker.java |
| 234  | 27  | MockMethodAdvice::ConstructorShortcut::wrap@387-643                        | @src/main/java/org/mockito/internal/creation/bytebuddy/MockMethodAdvice.java                 |
| 123  | 13  | ModuleHandler::ModuleSystemFound::adjustModuleGraph@170-292                | @src/main/java/org/mockito/internal/creation/bytebuddy/ModuleHandler.java                    |
| 149  | 34  | SubclassBytecodeGenerator::mockClass@126-301                               | @src/main/java/org/mockito/internal/creation/bytebuddy/SubclassBytecodeGenerator.java        |
| 41   | 17  | EqualsBuilder::append@341-387                                              | @src/main/java/org/mockito/internal/matchers/apachecommons/EqualsBuilder.java                |
| 25   | 21  | ArrayEquals::matches@17-41                                                 | @src/main/java/org/mockito/internal/matchers/ArrayEquals.java                                |
| 48   | 23  | ReturnsEmptyValues::returnValueFor@106-158                                 | @src/main/java/org/mockito/internal/stubbing/defaultanswers/ReturnsEmptyValues.java          |

From this, we picked out the first 4 functions to count manually: 

| function                         | lizard CCN | jacoco CCN | CCN manual1 | CCN manual2 |
|----------------------------------|------------|------------|-------------|-------------|
| InlineDelegateByteBuddyMockMaker | 24         | 8          | 15          | 8           |
| wrap                             | 27         | 8          | 27          | 8           |
| adjustModuleGraph                | 13         | 15         | 13          | 10          |
| mockClass                        | 34         | 35         | 34          | 39          |


All of the different methods of counting the complexity generated different results. For example, `InlineDelegateByteBuddyMockMaker` got 24 CCN by lizard, but only 8 by jacoco. The manual counts also differed a bit. Both the `InlineDelegateByteBuddyMockMaker` and `wrap` methods contain several methods, both lambdas and regular, inside of them. The first manual count includes these functions in the count, but the second manual counter did not. Most likely lizard includes these in the count, but jacoco does not. Another reason for the different counts may be if exceptions are counted, and if so if they are counted as another different branch or 'decision point', or if they are counted as an exit point. The first manual counter did not count thrown exceptions, but the second manual counter counted them as exit points. Therefore, the results for the complexity of the four functions are a bit unclear. Since the first manual count is similar to lizard, that tool is most likely not taking exceptions into account. It is not clear whether or not Jacoco takes exceptions into account. The second manual counter did not get the same count as jacoco for `adjustModuleGraph`, which is a function without lambdas or other inner functions, but with exceptions. The four chosen functions are both long and complex. However there are some other functions that are complex but not as long such as `matches` with CCN of 21 but only 25 lines. The documentation is lacking for all of the functions, and does not hold any information about the possible outcomes of the functions. 

3. What is the purpose of the functions?

| function                         | purpose                                                                                                                                                    |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| InlineDelegateByteBuddyMockMaker | This function is a constructor for the class with the same name, which is used to mock final types and methods and avoid creating a sub-class when mocking |
| wrap                             |                                                                                                                                                            |
| adjustModuleGraph                | The function is used to adjust a module graph of a source module so that a mock can be created                                                             |
| mockClass                        | The purpose of the function is to create a mock class                                                                                                      |




## Refactoring

Plan for refactoring complex code:

Estimated impact of refactoring (lower CC, but other drawbacks?).

Carried out refactoring (optional, P+):

git diff ...

## Coverage

### Tools

The tool used to measure code coverage was jacoco. It was already integrated in the existing build environment which made it easy to use with the command `./gradlew coverageReport`. This generated a html file with information about the coverage for all the functions in the project. 

### Your own coverage tool

The git command that is used to obtain the patch:

git diff a4da798

What kinds of constructs does your tool support, and how accurate is
its output?


Our tool checks if a branch has been reached. It supports constructs such as if/else, for and while loops and catch blocks. It does not measure coverage inside lambda functions or other functions contained within the function that is checked. 

### Evaluation

1. How detailed is your coverage measurement?

The coverage tool outputs the coverage measurement as a percentage of branches visited. In addition, it outputs which branches have been visited and which have not as identified by the branch ID. The tool does not count ternaries, but it does take exceptions into account since it counts the if- or catch-blocks leading up to exceptions being thrown. 

2. What are the limitations of your own tool?

There are some limitations of the tool. For example, in if-statements such as `if(a || b)`, it does not measure which of `a` or `b` was true, only if the inside of the if-statement was reached. In such cases there are two ways to reach the if-statement, but we cannot know which way we took. Functionally they are the same branch since it does not matter for the outcome of the if/else statement which of the variables was true. However, it could lead to limitations in testing and make it harder to spot bugs since the coverage tool does not tell us if we have tested both ways to reach the if-statement. To test this, we would have to split up such 'combined' conditionals into several different if-statements. Changing the code would require the instrumentation of the tool to change, to include counting of the new branches that may be created.

3. Are the results of your tool consistent with existing coverage tools?

There were some differences in results between our coverage tool and jacoco. For `wrap` and `mockClass` our coverage tool measured higher branch coverage than jacoco, whereas it measured lower coverage for `InlineDelegateByteBuddyMockMaker` and `adjustModuleGraph`. One reason for the differences is that the two tools use different ways of counting. The jacoco tool is more detailed and counts cases such as `if(a || b)` as four different branches (a b, a !b, !a b, !a !b), whereas our tool only counts it as two branches. In addition, jacoco seems to include the coverage of lambda functions and other inner functions, which our tool does not. 

The table below shows the coverage in percentage.

| function                         | jacoco coverage | DIY coverage |
|----------------------------------|-----------------|--------------|              
| InlineDelegateByteBuddyMockMaker | 50%             | 22.2%        |  
| wrap                             | 78%             | 87.5%        |              
| adjustModuleGraph                | 28%             | 14.3%        |            
| mockClass                        | 63%             | 78.4%        | 


## Coverage improvement

Show the comments that describe the requirements for the coverage.

Report of old coverage: [link]

Report of new coverage: [link]

Test cases added:

git diff ...

Number of test cases added: two per team member (P) or at least four (P+).



Requirements to increase coverage:
* ArrayEquals.matches: The current test suite does not cover cases where the wanted object is an int array and the actual given object is something else. It also does not cover cases where the actual given object is null.

| function                         | old coverage | new coverage | added test cases  | group member |
|----------------------------------|--------------|--------------|-------------------|--------------|              
| ArrayEquals.matches              | 72%          | 80%          | 792aa0a & 86a022e | Anne Haaker  |


## Self-assessment: Way of working

Current state according to the Essence standard: ...

Was the self-assessment unanimous? Any doubts about certain items?

How have you improved so far?

Where is potential for improvement?

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?
