# Colony
 An Educational Evolution Simulator
 * Mac installer - [download .pkg](https://www.mediafire.com/file/9irtq3by53frhd8/ColonyInstaller-1.1.pkg/file)

Colony lets you easily observe the processes of mutation and natural selection!
The user can vary the temperature, type of food, and abundance of food, and see the effects on the traits of a simulated population.
This is not meant as an accurate scientific demonstration but instead as an accessible way to display the process of evolution!
Feel free to use as an educational tool or just for fun.

## Getting Started
### Prerequisites
Java 14 or higher
### Installing
Step 1) Navigate to the root directory and run:
```
gradlew jpackage
```

Step 2) This should generate a installer specific to your OS in directory build/your-os-installer.
Run the installer.

## Built With
* [Java](https://www.java.com/en/) - Language the application is built in
* [JavaFX](https://openjfx.io) - Framework for GUI and 2D graphics
* [Intellij](https://www.jetbrains.com/idea/) - IDE used for development
* [Gradle](https://gradle.org/features/) - Build automation tool
* [Badass Runtime Plugin](https://badass-runtime-plugin.beryx.org/releases/latest/) - For creating installers and runtime images
* [Gluon Scene Builder](https://gluonhq.com/products/scene-builder/) - For creating GUI layout
### Dependencies/Libraries
* [JFoenix](https://github.com/sshahine/JFoenix) - For material design UI elements
