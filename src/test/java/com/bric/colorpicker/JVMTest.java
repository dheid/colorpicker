package com.bric.colorpicker;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

class JVMTest {

    @Test
    void usingQuartzDisabledOnLinux() {

        boolean usingQuartz = JVM.isUsingQuartz();

        assertThat(usingQuartz).isFalse();

    }

    @EnabledOnOs(value = {OS.LINUX, OS.WINDOWS, OS.AIX, OS.FREEBSD, OS.OTHER, OS.OPENBSD, OS.SOLARIS})
    @Test
    void checksWhetherSystemIsNotMac() {

        boolean isMac = JVM.IS_MAC;

        assertThat(isMac).isFalse();

    }

    @EnabledOnOs(value = OS.MAC)
    @Test
    void checksWhetherSystemIsMac() {

        boolean isMac = JVM.IS_MAC;

        assertThat(isMac).isFalse();

    }

}
