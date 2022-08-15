package com.bric.colorpicker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class JVMTest {

    @Test
    void usingQuartzDisabledOnLinux() {

        boolean usingQuartz = JVM.isUsingQuartz();

        assertThat(usingQuartz).isFalse();

    }

    @Test
    void checksWhetherSystemIsMac() {

        boolean isMac = JVM.IS_MAC;

        assertThat(isMac).isFalse();


    }
}
