# ColorPicker

A nice color picker using Java Swing that contains a visual color selection and input boxes to enter RGB and HSB values manually.

## Usage

Use the class com.bric.colorpicker.ColorPicker as an entry point. You can easily initialize the panel using

```Java
ColorPicker colorPicker = new ColorPicker(true, true);
colorPicker.addColorListener(colorModel -> System.out.println(colorModel.getColor()));
```

This creates a color picker component with expert controls and opacity settings. If the user selects a color, the color listener will be notified.

## Continuous Integration

[![Linux/Mac Build Status](https://secure.travis-ci.org/dheid/colorpicker.png)](http://travis-ci.org/dheid/roperty-rest)
[![Windows Build Status](https://img.shields.io/appveyor/ci/dheid/colorpicker/master.svg?label=windows)](https://ci.appveyor.com/project/dheid/roperty-rest/branch/master)
[![Coverage Status](https://coveralls.io/repos/dheid/colorpicker/badge.svg?branch=master&service=github)](https://coveralls.io/github/dheid/roperty-rest?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.dheid/colorpicker.svg?maxAge=2592000)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22io.github.dheid%22%20AND%20a%3A%22colorpicker%22)


## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D

## History

This color picker was a part of the javagraphics project (http://javagraphics.blogspot.com), initiated by Jeremy Wood. The javagraphics project is a collection of small stand-alone projects.

## Credits

The original color picker component is copyright 2011 by Jeremy Wood.

## License

This modification of the color picker is released under a BSD 3-Clause
license. More details can be found here:
 
https://opensource.org/licenses/BSD-3-Clause