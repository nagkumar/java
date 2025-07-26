package com.shivohamai.testing.tools.junit5.assertscounter.agent.tests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public final class TestAssertJAsserts
{
    @Test
    void assertJAssertionsLoop()
    {
	for (int i = 1; i <= 30; i++)
	{
	    Assertions.assertThat(i).isGreaterThan(0);
	    Assertions.assertThat(i).isNotNegative();
	    Assertions.assertThat(i).isEqualTo(i);
	    Assertions.assertThat(i).isNotEqualTo(i + 1);
	    Assertions.assertThat(i).isNotNull();
	}
    }
}
