package com.bric.colorpicker;

import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.Callable;

@Ignore
public class ColorPickerGUITest {

    private DialogFixture fixture;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setUp() {
        ColorPickerDialog dialog = GuiActionRunner.execute((Callable<ColorPickerDialog>) ColorPickerDialog::new);
        this.fixture = new DialogFixture(dialog);
        this.fixture.show();
    }

    @Test
    public void hexValueCorrectlyParsed() {
        JTextComponentFixture hex = this.fixture.textBox("Hex");
        hex.deleteText();
        hex.enterText("facade");

        this.fixture.spinner("Hue").requireValue(335);
        this.fixture.spinner("Saturation").requireValue(19);
        this.fixture.spinner("Brightness").requireValue(98);

        this.fixture.spinner("Red").requireValue(250);
        this.fixture.spinner("Green").requireValue(202);
        this.fixture.spinner("Blue").requireValue(222);
    }

    @Test
    public void hsbValuesCorrectlyInterpreted() {
        this.fixture.spinner("Hue").enterTextAndCommit("125");
        this.fixture.spinner("Saturation").enterTextAndCommit("68");
        this.fixture.spinner("Brightness").enterTextAndCommit("52");

        this.fixture.spinner("Red").requireValue(43);
        this.fixture.spinner("Green").requireValue(133);
        this.fixture.spinner("Blue").requireValue(50);
    }

    @Test
    public void rgbValuesCorrectlyInterpreted() {
        this.fixture.spinner("Red").enterTextAndCommit("40");
        this.fixture.spinner("Green").enterTextAndCommit("0");
        this.fixture.spinner("Blue").enterTextAndCommit("69");

        this.fixture.spinner("Hue").requireValue(275);
        this.fixture.spinner("Saturation").requireValue(100);
        this.fixture.spinner("Brightness").requireValue(27);
    }

}
