package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.matches;

import org.junit.Test;

public class MatchesTest {
    @Test
    public void TestFalseIfActualIsNotAString() {
        assertFalse(new Matches("1").matches(1));
    }
}
