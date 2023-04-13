package com.codebootup.codegenerator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultLocationTest {
    @Test
    public void checkDefaultLocationIsAsExpected(){
        Assertions.assertThat(new DefaultLocation().getBaseDirectory()).isEqualTo("");
        Assertions.assertThat(new DefaultLocation().getFileDirectory()).isEqualTo("");
    }
}
