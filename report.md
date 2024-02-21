# Report for assignment 3

## Project

Name: Anne Haaker, Alex Gunnarsson, Hugo Tricot, Juan Bautista Lavagnini Portela

URL: https://github.com/haaker1/mockito 

The project is mockito, which is a tool used for mock-testing Java projects.

## Onboarding experience

The could be built and run as documented in the project README. To build the project, JDK had to be upgraded to JDK 17 which was easily done from the JDK documentation. In addition, gradle had to be installed, but it was installed automatically when running the build script. The build concluded without errors, but there were some warnings such as "OpenJDK 64-bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended". However, this did not seem to affect the build since it concluded successfully. All the tests passed on our systems. We are leaning toward continuing on the project.

## Complexity

The complexity measurement tool lizard was run on the code base to identify four large functions. The cyclomatic complexity of these functions was also counted by hand. We ran lizard using the command `lizard src/ -x"./src/tests/*" -l java -T cyclomatic_complexity=10` and obtained the following results:

| NLOC | CCN | location                                                                   | file                                                                                         |
| ---- | --- | -------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
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

From this, we picked out four functions to count manually: 

| Member | function                   | lizard CCN | jacoco CCN | CCN manual1 | CCN manual2                      |
| ------ | -------------------------- | ---------- | ---------- | ----------- | -------------------------------- |
| Alex   | SerializableMethod::equals | 14         | 25         | 13          | 13 (Anne)                        |
| Anne   | ArrayEquals::matches       | 21         | 21         | 11          | 11 (Alex)                        |
| Hugo   | EqualsBuilder::append      | 17         | 18         | 18          |                                  |
| Juan   |                            |            |            |             | 2 (matches), 3 (areEqual) (Hugo) |

**Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?**

Both lizard and JaCoCo disagree with regards to CCN in some cases, which means they measure it differently. Therefore, it is natural that our own counts disagree, perhaps because of how bitwise operators are treated or how it reads the code. The manual CCN counts seem to disagree with the tools, but agree between each other. A possible reason for this is that there are different methods of counting cyclomatic complexity, and the tools use a different method from us. As a consequence, the results from the tools are a bit unclear.

**Are the functions just complex, or also long?**

The length of the most complex functions differ. However, they seem to lean towards being medium to long. This makes sense, since a longer function will be able to fit more decision points such as if/else-statements inside of it.

**What is the purpose of the functions?**

| function                              | purpose                                                                                               |
| ------------------------------------- | ----------------------------------------------------------------------------------------------------- |
| SerializableMethod::equals            | Check if the SerializableMethod object is equal to another object.                                    |
| ArrayEquals::matches                  | Check if a given object is an array, and is equal to another object that is an array of the same type |
| EqualsBuilder::append(Object, Object) | Compare two Object, notably arrays of primitive types and return if they are equal in-depth.          |
| Matches::matches                      |                                                                                                       |
| Equality::AreEquals                   |                                                                                                       |

**Are exceptions taken into account in the given measurements?**
The chosen functions lack exceptions which makes it unclear whether or not the tools take these into account.

**Is the documentation clear w.r.t. all the possible outcomes?**
The documentation is lacking for all of the functions, and does not hold any information about the possible outcomes of the functions. 

## Refactoring

**Plan for refactoring complex code:**

* SerializableMethod::equals - The function has a lot of if-statements and different ways of figuring out if all necessary fields are equal between two objects. One initial thought is to split these up and handle different stages in different helper-functions. However, upon further inspection, it seems that there is a bit of dead code present; if-statements which are impossible to reach. For example, one branch is taken only if the return type of a function is null (which is not the same as void), which is impossible since every method needs a return type. Therefore, a refactoring plan is to remove the dead code and therefore reduce the complexity.
* ArrayEquals::matches - 
* EqualsBuilder::append - The main part of the function, but also the one to induce most of the complexity, is a large switch-like structure to call the other `append` functions of correct type. This part cannot be refactored easily, attempts to modify it resulted in test failuers. Instead, we can refactor the first 2 parts, the one handling basic scenario (e.g. one or both objects being null; lines 342-351 in the original code), and the second handling non-array types and arrays of different dimensions (e.g. `int[]` and `int[][]`; lines 352-362 in the original code).
* - 

**Estimated impact of refactoring (lower CC, but other drawbacks?).**

The CC will be lower, but there will be more functions, which can sometimes be a drawback if considering readability. Some functions are just inherently complex.

**Carried out refactoring (optional, P+):**

| Member | Refactor                     | Improvement                                                         |
| ------ | ---------------------------- | ------------------------------------------------------------------- |
| Alex   | `git diff e7690c9^..e7690c9` | CCN reduced from 14 to 8 (lizard), or 13 to 7 (manual)              |
| Anne   | ``                           |                                                                     |
| Hugo   | `git diff dcc5154^..dcc5154` | CCN reduced from 17 to 11 (lizard and manual), or 18 to 11 (jacoco) |
| Juan   | ``                           |                                                                     |

## Coverage

### Tools

The tool used to measure code coverage was jacoco. It was already integrated in the existing build environment which made it easy to use with the command `./gradlew coverageReport`, although it wasn't properly documented. This generated a html file with information about the coverage for all the functions in the project. 

### Your own coverage tool

**The git command that is used to obtain the patch:**

git diff dd5de3f^..0a075f8

**What kinds of constructs does your tool support, and how accurate is its output?**

Our tool checks if a branch has been reached. It technically supports all types of branches, but they have to be conciously implemented by the programmer who seeks to measure the coverage. The branch logging is done via function calls, so ternaries have to be converted into if-else statements which call the function to log the branch.

### Evaluation

**How detailed is your coverage measurement?**

The coverage tool outputs the coverage measurement as a percentage of branches visited. In addition, it outputs which branches have been visited and which have not as identified by the branch ID. The tool is dependent on the utilization by the programmer who uses it. The tool does not work well with ternaries, but can be turned into if-else statements which are properly supported. It does take exceptions into account since it counts the if- or catch-blocks leading up to exceptions being thrown. 

**What are the limitations of your own tool?**

There are some limitations of the tool. For example, in if-statements such as `if(a || b)`, it does not measure which of `a` or `b` was true, only if the inside of the if-statement was reached. In such cases there are two ways to reach the if-statement, but we cannot know which way we took. Functionally they are the same branch since it does not matter for the outcome of the if/else statement which of the variables was true. However, it could lead to limitations in testing and make it harder to spot bugs since the coverage tool does not tell us if we have tested both ways to reach the if-statement. To test this, we would have to split up such 'combined' conditionals into several different if-statements. Changing the code would require the instrumentation of the tool to change, to include counting of the new branches that may be created.

**Are the results of your tool consistent with existing coverage tools?**

There were some differences in results between our coverage tool and jacoco. One reason for the differences is that the two tools use different ways of counting. The jacoco tool is more detailed and counts cases such as `if(a || b)` as four different branches (a b, a !b, !a b, !a !b), whereas our tool only counts it as two branches. In addition, jacoco seems to include the coverage of lambda functions and other inner functions, which our tool does not. 

## Coverage improvement

The table below shows the coverage in percentage.

| function (without new tests) | jacoco coverage | DIY coverage |
| ---------------------------- | --------------- | ------------ |
| SerializableMethod::equals   | 46%             | 41.7%        |
| ArrayEquals::matches         | 72%             | 91.7%        |
| EqualsBuilder::append        | 94%             | 100%         |
| Matches::matches             | 70%             |              |
| Equality::AreEquals          | 91%             |              |


Report of old coverage: [./jacocoHtml - before](https://github.com/haaker1/mockito/tree/issue/6-report/jacocoHtml%20-%20before)

Number of test cases added: two per team member (P) or at least four (P+).

| Member | TC1                          | TC2                          | TC3                          | TC4                          |
| ------ | ---------------------------- | ---------------------------- | ---------------------------- | ---------------------------- |
| Alex   | `git diff a4fa4ce^..a4fa4ce` | `git diff 787f032^..787f032` | `git diff 8e7796c^..8e7796c` | `git diff 68919fb^..68919fb` |
| Anne   | `git diff a10d43c^..a98781b` | `git diff 792aa0a^..792aa0a` |                              |                              |
| Hugo   | `git diff 114783c^..114783c` | `git diff 114783c^..114783c` | `git diff 114783c^..114783c` | `git diff 114783c^..114783c` |
| Juan   | `git diff a10d43c^..8f2807b` | `git diff a10d43c^..9be7b8d` |                              |                              |

**Requirements to increase coverage:**

* SerializableMethod::equals - It has a few checks which are never reached, for example whether the other object is null or if they are not of the same class, which could be added as test case. Also, depending on the methods that the function is comparing, they must also have the same method names and parameter types, which are not tested and could be added. 
* ArrayEquals::matches - The current test suite does not cover cases where the wanted object is an int array and the actual given object is something else. It also does not cover cases where the actual given object is null.
* EqualsBuilder::append - The existing tests already reach full DIY coverage and as such the it cannot be improved. On the other hand the branch coverage as used by jacoco is not 100%. Coverage can be improved by testing an input with one instance of `BigDecimal` against one that is of another type (e.g. `int`). Another test to try two `BigDecimal` of the same value (but not the same variable) in the input increases the coverage further. 
* - 

| function (with new tests)  | jacoco coverage | DIY coverage |
| -------------------------- | --------------- | ------------ |
| SerializableMethod::equals | 61%             | 58.3%        |
| ArrayEquals::matches       | 80%             | 100%         |
| EqualsBuilder::append      | 97%             | 100%         |
| Matches::matches           | 100%            |              |
| Equality::AreEquals        | 100%            |              |

Report of new coverage: [link]

## Self-assessment: Way of working

Our current state according to the Essence standard is 'in place'. Some tools that we have been using for the entire course, such as git and Java, are being used by the whole team in a natural way. However, some tools have been adapted for the context of the current project, such as using gradle instead of maven and using lizard and jacoco for complexity and coverage analysis. These new tools are being used by the whole team to perform their work, but the use is not yet fully natural. An improvement is that the team members are starting to work more independently and asking for input from the other team members when encountering questions or issues. This is possible because the way of working of the team is becoming more unified. There is however potential for improvement when it comes to communication and planning in the beginning of a new project.

## Overall experience

**What are your main take-aways from this project? What did you learn?**

**Is there something special you want to mention here?**
