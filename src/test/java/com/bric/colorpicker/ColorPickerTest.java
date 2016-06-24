package com.bric.colorpicker;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class ColorPickerTest {

    private DialogFixture fixture;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setUp() {
        ColorPickerDialog dialog = GuiActionRunner.execute(() -> new ColorPickerDialog());
        fixture = new DialogFixture(dialog);
        fixture.show();
    }

    @Test
    public void hexValueCorrectlyParsed() {
        JTextComponentFixture hex = fixture.textBox("Hex");
        hex.deleteText();
        hex.enterText("facade");

        fixture.spinner("Hue").requireValue(335);
        fixture.spinner("Saturation").requireValue(19);
        fixture.spinner("Brightness").requireValue(98);

        fixture.spinner("Red").requireValue(250);
        fixture.spinner("Green").requireValue(202);
        fixture.spinner("Blue").requireValue(222);
    }

    @Test
    public void hsbValuesCorrectlyInterpreted() {
        fixture.spinner("Hue").enterTextAndCommit("125");
        fixture.spinner("Saturation").enterTextAndCommit("68");
        fixture.spinner("Brightness").enterTextAndCommit("52");

        fixture.spinner("Red").requireValue(43);
        fixture.spinner("Green").requireValue(133);
        fixture.spinner("Blue").requireValue(50);
    }

    @Test
    public void rgbValuesCorrectlyInterpreted() {
        fixture.spinner("Red").enterTextAndCommit("40");
        fixture.spinner("Green").enterTextAndCommit("0");
        fixture.spinner("Blue").enterTextAndCommit("69");

        fixture.spinner("Hue").requireValue(275);
        fixture.spinner("Saturation").requireValue(100);
        fixture.spinner("Brightness").requireValue(27);
    }

}
