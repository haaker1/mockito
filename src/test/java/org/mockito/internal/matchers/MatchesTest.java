/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.matchers;

import static org.junit.Assert.*;

import org.junit.Test;

public class MatchesTest {
    @Test
    public void TestFalseIfActualIsNotAString() {
        assertFalse(new Matches("1").matches(1));
    }
}
