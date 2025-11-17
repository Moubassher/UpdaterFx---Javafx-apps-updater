<h3> Introduction</h3>
There are generally 2 ways to package a javafx program:

1. "all-batteries-included", AKA "fat jar", (libraries + resources) and optionally a bundled custom runtime image.
2. a thin jar, which is usually kilobytes in size with all dependencies referenced by it sitting next to it in relative paths.
  
Plus, there's the native executable created, with AOT provided by GraalVM, which is technically the first method except, you get an exe that doesn't require the JVM at all 
(hence a faster startup, improved security through resistance to reverse-engineering (no need for obfuscation) etc.)

However, one thing I haven't found enough discussion about is the streamlining of the update process of javaFX programs. And so, I have never been satisfied with the first method of packaging JavaFX programs. Because unless your program was so feature-complete it would never need an update till the end of times, you have to have a better way of letting users get updates to your program in small patches, and as soon as they come out or else, it would be a horrible User Experience!

More and more desktop apps, e.g. VS Code, Chrome, Github Destkop, and most modern desktop programs started having these streamlined update techniques that are so non-intrusive, they just notify you the updates will download in the background and will be applied automatically with the next run of the program.

One repo that had potential to provide this functionality was Update4j (https://github.com/update4j/update4j). But I say "had" because it's a public archive now with an API that didn't work out very well, and in the abscence of maintainers, I had to just implement a solution from scratch.
I have endeavered to create this CRUCIAL feature/experience through this repository along with a gradle plugin I wrote (https://github.com/Moubassher/release-manifest-plugin).

<h3>How it works</h3>

1. The program gets a release manifest generated (manifest.json) with it when packaged with a packaging tool such as the badass-runtime plugin. This JSON file tracks all files in the directories created by the badass runtime plugin (app/ and runtime/) through a hash created for each file.
2. When a new version of the program is published, a remote path (configurable through the [release-manifest-plugin](https://github.com/Moubassher/release-manifest-plugin)) is checked for the same manifest file. If the hash of a file is different, it is assumed to have been part of the new update, hence is downloaded upon the user's approval to the update.

Done. That's it!


<h3> Usage:</h3>
1. Add the Release Manifest plugin:
(Currently the release manfiest works only with the badass-runtime plugin. But as long as the packaged program contains app/ and runtime/ directories, it would work too).

```groovy
plugins {
    id 'java'
     id 'org.beryx.runtime' version '2.0.1'  
    id 'com.moubassher.release-manifest-generator' version '2.0.0'
}
```

2. Your application class should extend UpdatableApplication and implement showMainWindow(stage) like so:

```java
public class MainApp extends UpdatableApplication {

    @Override
    protected void showMainWindow(Stage stage) throws Exception {
        Pane root = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
        stage.setScene(new Scene(root, 400, 400));
    }
}
```
3. The main class then just calls launch() of MainApp, and you get a popup window that notifies the user of an update if there's one at the remote url specified at the manifest file.
```java
public class Launcher {
    public static void main(String[] args) throws Exception {
        MainApp.launch(MainApp.class, args);
    }
}
```
PS: This repo is a work-in-progress. The update pop-up still requires a lot of polishing, but this is a good start, I'd say. 
Don't forget to star the repo if you used it and like it. Enjoy!
