//package samples.javainterop;
//
//import javafx.application.Application;
//import javafx.scene.Scene;
//import javafx.scene.control.Label;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
///**
// * We show the following forms of interoperability:
// * 1. Adding SimpleFX classes to a JavaFX-Program
// * 2. Adding JavaFX classes to a SimpleFX-Program
// * 3. Changing JavaFX-Instances in SimpleFX
// * 4. Using FXML inside of SimpleFX
// *    (note that we didn't had to change the imports)
// * 5. Wrapping own JavaFX classes, to use it's own properties inside of SimpleFX
// */
//public class MainApp extends Application {
//  public void start(Stage stage) {
//    VBox root = new VBox();
//    root.getChildren().add(new Label("Hello Java"));
//    root.getChildren().add(new MySimpleFXClass());
//
//    Scene scene = new Scene(root);
//
//    stage.setScene(scene);
//
//    stage.show();
//  }
//
//  public static void main(String ... args) {
//    launch(args);
//  }
//
//}
