# ColorPicker

[![pipeline status](https://gitlab.com/dheid/colorpicker/badges/master/pipeline.svg)](https://gitlab.com/dheid/colorpicker/commits/master)
[![coverage report](https://gitlab.com/dheid/colorpicker/badges/master/coverage.svg)](https://gitlab.com/dheid/colorpicker/commits/master)
[![Maven Central](https://img.shields.io/maven-central/v/org.drjekyll/colorpicker.svg?maxAge=2592000)](http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.drjekyll%22%20AND%20a%3A%22colorpicker%22)

A nice color picker using Java Swing that contains a visual color selection and input boxes to enter RGB and HSB values manually.

## Building

Please use the included Maven wrapper to build the JAR.

    ./mvnw package
    
After that you can include the file `fontchooser/target/fontchooser-VERSION.jar` into your project (replace VERSION with
the current project version).

## Install to local Maven repository

To test a version locally, please execute

    ./mvnw install
    
This will install the version to your local repository. You can now include it using Maven (see below).

## Usage

Include the following dependency to your project:

```xml 
<dependency>
  <groupId>org.drjekyll</groupId>
  <artifactId>colorpicker</artifactId>
  <version>1.2</version>
</dependency>
```

Use the class `com.bric.colorpicker.ColorPicker` as an entry point. You can easily initialize the panel using

```Java
ColorPicker colorPicker = new ColorPicker(true, true);
colorPicker.addColorListener(colorModel -> System.out.println(colorModel.getColor()));
```

This creates a color picker component with expert controls and opacity settings. If the user selects a color, the color listener will be notified.

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
