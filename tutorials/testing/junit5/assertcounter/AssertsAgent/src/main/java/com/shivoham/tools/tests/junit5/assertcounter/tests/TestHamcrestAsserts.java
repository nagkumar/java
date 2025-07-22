package com.shivoham.tools.tests.junit5.assertcounter.tests;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class TestHamcrestAsserts
{
    @Test
    void hamcrestAssertionsLoop()
    {
	for (int i = 1; i <= 3; i++)
	{
	    assertThat(i, greaterThan(0));
	    assertThat(i, not(lessThan(0)));
	    assertThat(i, equalTo(i));
	    assertThat(i, not(equalTo(i + 1)));
	    assertThat(i, notNullValue());
	}
    }
}
