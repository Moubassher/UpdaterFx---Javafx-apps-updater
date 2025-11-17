# ====================================================================
# ProGuard Rules for JavaFX Applications
# ====================================================================

# 1. Keep the main Application class and its entry points
# Replace 'com.yourpackage.MainApplication' with your actual main class path.
-keep public class com.yourpackage.MainApplication {
    public static void main(java.lang.String[]);
    public void start(javafx.stage.Stage);
}

# 2. Keep all fields and methods annotated with @FXML
# FXML files use reflection to access these, so their names must not change.
-keepclasseswithmembers class * {
    @javafx.fxml.FXML <fields>;
    @javafx.fxml.FXML <methods>;
}

# 3. Keep all controller classes and their constructors
# FXML loaders instantiate these classes by name.
# Replace 'com.yourpackage.controller' with your actual controller package.
-keep public class com.yourpackage.controller.* {
    public <init>();
    public *;
}

# 4. Keep required JavaFX components (e.g., for controls/skins)
-keep public class * extends javafx.scene.Node {
    public <init>(...);
}

# 5. General optimization and warning handling
# Remove unused code and suppress warnings related to common dependencies 
# that ProGuard can't analyze (like platform-specific JavaFX implementations).
-optimizations !code/simplification/arithmetic
-dontwarn javafx.**
-dontwarn com.sun.javafx.**

# Optional: Add your model classes if they are used in bindings (reflection)
# -keep class com.yourpackage.model.** { *; }