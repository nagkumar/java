package com.shivoham.tools.tests.junit5.assertcounter.tests;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestAssertJAsserts
{
    @Test
    void assertJAssertionsLoop()
    {
	for (int i = 1; i <= 3; i++)
	{
	    Assertions.assertThat(i).isGreaterThan(0);
	    Assertions.assertThat(i).isNotNegative();
	    Assertions.assertThat(i).isEqualTo(i);
	    Assertions.assertThat(i).isNotEqualTo(i + 1);
	    Assertions.assertThat(i).isNotNull();
	}
    }
}
