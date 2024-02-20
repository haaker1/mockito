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
| ---- | --- | -------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| 119  | 24  | InlineDelegateByteBuddyMockMaker::InlineDelegateByteBuddyMockMaker@225-348 | @src/main/java/org/mockito/internal/creation/bytebuddy/InlineDelegateByteBuddyMockMaker.java |
| 234  | 27  | MockMethodAdvice::ConstructorShortcut::wrap@387-643                        | @src/main/java/org/mockito/internal/creation/bytebuddy/MockMethodAdvice.java                 |
| 123  | 13  | ModuleHandler::ModuleSystemFound::adjustModuleGraph@170-292                | @src/main/java/org/mockito/internal/creation/bytebuddy/ModuleHandler.java                    |
| 149  | 34  | SubclassBytecodeGenerator::mockClass@126-301                               | @src/main/java/org/mockito/internal/creation/bytebuddy/SubclassBytecodeGenerator.java        |
| 41   | 17  | EqualsBuilder::append@341-387                                              | @src/main/java/org/mockito/internal/matchers/apachecommons/EqualsBuilder.java                |
| 25   | 21  | ArrayEquals::matches@17-41                                                 | @src/main/java/org/mockito/internal/matchers/ArrayEquals.java                                |
| 48   | 23  | ReturnsEmptyValues::returnValueFor@106-158                                 | @src/main/java/org/mockito/internal/stubbing/defaultanswers/ReturnsEmptyValues.java          |

From this, we picked out the first 4 functions to count manually: 

| function                         | CCN | CCN manual1 | CCN manual2 |
| -------------------------------- | --- | ----------- | ----------- |
| InlineDelegateByteBuddyMockMaker | 24  |             |             |
| wrap                             | 27  |             |             |
| adjustModuleGraph                | 13  |             |             |
| mockClass                        | 34  |             |             |

* InlineDelegateByteBuddyMockMaker (constructor for class with identical name) with NLOC = 119 and CC = 24. Manual count CC = 15 if not counting throws, otherwise 13. This function is a constructor for the class with the same name, which is used to mock final types and methods and avoid creating a sub-class when mocking.
* wrap (in the ConstructorShortcut class in MockMethodAdvice), with NLOC = 234 and CC = 27. Manual count CC = 27. 
* adjustModuleGraph (in ModuleSystemFound class in ModuleHandler) with NLOC = 123 and CC = 13. Manual count CC = 13 if not counting throws, otherwise 10. The function is used to adjust a module graph of a source module so that a mock can be created.
* mockClass (in SubclassBytecodeGenerator) with NLOC = 149 and CC = 34. Manual count CC = 34. The purpose of the function is to create a mock class.
1. What are your results for five complex functions?
   * Did all methods (tools vs. manual count) get the same result?
   * Are the results clear?
2. Are the functions just complex, or also long?

Both long and complex, however there are some other functions that are complex but not as long (ReturnValueFor in ReturnsEmptyValues class with CC=23 and NLOC=48, and matches in ArrayEquals class with CC=21 and NLOC=25).

3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?

Exceptions are not taken into account in the measurements for the tools, but are shown for manually calculated measurements. There, exceptions are thought of as an exit point, which lowers the CC.

5. Is the documentation clear w.r.t. all the possible outcomes?

The documentation is lacking for all of the functions...

## Refactoring

Plan for refactoring complex code:

Estimated impact of refactoring (lower CC, but other drawbacks?).

Method InlineDelegateByteBuddyMockMaker::InlineDelegateByteBuddyMockMaker():

- CCN: 24

- Possible refactor: a large part at the beginning (lines 263 to 265) can be extracted into a separate function. This code deals with initialization error and is independent from the object creation. This part greatly lowers the CC yet has no real drawback. With only this refactor, the function is still above 15 CCN according to lizard's computation method. If needed, we can also refactor the lambda method to assign `isMockConstruction`  (lines 293 to 337) into a separate method.

Method ArrayEquals::matches(Object):

- CCN: 21

- Problem: this is basically a big switch structure, where we have to check all the time for the current array's type (always some basic one), same for the target array and then check equality

- Posssible refactor: as the code always checks the instance of the target array against the actual array given as parameter, we can remove testing the instance of the `actual` array, if we check whether or not the target and actual array are of the same instance at the beginning by `if(wanted.getClass().isInstance(actual)) {return false;}`. Should not create other problems.

Method EqualsBuilder::append(Object[], Object[]):

- CCN: 17

- Problem: it's not too easy to refactor this one. One way is to try to make sense of the type dispatching. But this may caus tests on this method to fail (it did in my attempt at refactoring it). Another is to pick the first lines returning `this` if the arrays are null, known to be different already, or their pointers are the same for a new method
  
  ```java
  // Returns true if the objects are known to be non-equal, if any is null, or if they are
  // the same pointer
  
  private boolean checkNoWorkToBeDone(Object lhs, Object rhs) {
      if (!isEquals) {
          return true;
      }
      if (lhs == rhs) {
          return true;
      }
      if (lhs == null || rhs == null) {
          this.setEquals(false);
          return true;
      }
      return false;
  }
  ```
  
  This stilll causes an error, but because one test checks how long it takes to do an operation and with the new method it takes longer. We will not implement this one.

Method ModuleHandler::adjustModuleGraph(Class, Class, boolean, boolean):

- CCN: 13

- Possible refactoring: a few parts can be extracted into separate functions, e.g.
  
  - ```java
    while (!targetVisible && classLoader != null) {
                    classLoader = classLoader.getParent();
                    targetVisible = classLoader == target.getClassLoader();
    }
    ```

```java
if (needsExport) {
                implementation =
                        implementation.andThen(
                                MethodCall.invoke(addExports)
                                        .onMethodCall(sourceLookup)
                                        .with(target.getPackage().getName())
                                        .withMethodCall(targetLookup));
            }
            if (needsRead) {
                implementation =
                        implementation.andThen(
                                MethodCall.invoke(addReads)
                                        .onMethodCall(sourceLookup)
                                        .withMethodCall(targetLookup));
            }
```

    This should help to decrease the CCN while not changing the behaviour.

Method SubclassBytecodeGenerator::mockClass(MockFeatures):

- CCN: 34

- Possible refactoring: extract the following code lines into dedicated methods (it should reduce the CCN, but increase the time for execution by a bit):
  
  - 139-152: this is a handler in case we don't have the same class loader yet, return a boolean and assign it to `shouldIncludeContextLoader`, change the break for a return.
  
  - 166-223: extract this into a `handleMockLocalOrNot(MockFeatures<T> features, ClassLoader classLoader)` method and give `features` as argument (should be of void return type)
  
  - 238-244: extract into an annotation handler, no return and takes Annotation[] as argument
  
  - 277-295: make a function to add properties/behaviour to the builder

- Documentation: the documentation inside the class may be used to create the documentation for these new methods.

Method MockMethodAdvice::ConstructorShortcut::wrap(...):

- CCN: 27

- Problem: a new class is defined in the return of the function, and holds most of the complexity.

- Possible refactor: create a new class that inherits from MethodVisitor and overrides the given functions. This new class will take most of the complexity away. We can further extract lines 591 to 623 as they have a common condition to be executed. Instead of using twice the same conditions we can merge them, specifying via comments the delimitation between the two parts as they create variables of the same name. It would take `implementationContext`, `instrumentedType` and `instrumentedMethod` as parameters. We can also extract lines 409 to 414 as this part deals with the specific of a private constructor.

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

Is there something special you want to mention here
