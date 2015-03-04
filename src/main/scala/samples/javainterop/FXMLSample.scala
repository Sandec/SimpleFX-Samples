package samples.javainterop

import simplefx.all._
import simplefx.core._
import simplefx.experimental._
import javafx.fxml.{FXML, FXMLLoader}

/* === FXMLSample =============================== START ============================================================= */
/*
      NOTE: In this sample we show how we can use an FXML-file (created by the Scene-Builder)
            representing the major UI-components.

            In order to show the inter-operability between any JavaFX-program (be it created by FXML or not)
            and SimpleFX, we here show how a Group of nodes can be pinned to the Scene; the nodes being a mix of
            JavaFX- and SimpleFX-generated nodes.

            The mainNode here represents the top-level node created by JavaFX(FXML), whereas the background rectangle
            and the label are created from SimpleFX-code.

            We also show how a node created by JavaFX (the mainNode) has been extended through the core SimpleFX
            (through Extension Classes) and therefore offers SimpleFX-properties like "draggable=true" or "layoutXY".

            We also show how SimpleFX Binding can be applied to any node, no matter whether it has been created by
            JavaFX or through SimpleFX-code.

            We also see some examples of SimpleFX Extensions introduced to make the API simpler than the original
            JavaFX-API; the "selectedItem" property is used instead of needing a "SelectionModel" in between,
            the "wh"-property is used instead of the "width" and "height" properties, as foreseen in the standard
            JavaFX API.  The same also applies for the "bipX", "bipEY" and "bipW" properties used (they are synonyms
            for the JavaFX-properties "boundsInParent.getX", "boundsInParent.getMaxY" and "boundsInParent.getWidth".

 */
object FXMLSample extends App
@SimpleFXApp class FXMLSample { title = "FXML TableView Example"

  /* Declaration of all UI-components to be pinned to the scene ----------------------------------------------------- */
  root = new Group {                                                            // The root implicitly sets the Scene.
    def path(s:String) = getClass.getResource(s)                                // Convention for the Path ..
    val loader         = new FXMLLoader(path("fxml_tableview.fxml"))            // Load the FXML-file.
    val mainNode       = loader.load[Node]                                      // Set the main Node of the FXML.
    val controller     = loader.getController[FXMLTableViewController]          // Set the controller.
    def table          = controller.tableView                                   // Define the table object.
    def isSelected     = table.selectedItem != null                             // Is any table-item selected ..
    def selectedName   = if(isSelected) table.selectedItem.firstName else ""    // Define the selected table-item.
    def selectedLast   = if(isSelected) table.selectedItem.lastName  else ""    // Define the selected table-item.

    <++ (new Rectangle {wh = (1000,800); fill = Color.ALICEBLUE})               // Pin the background rectangle.
    <++ (new Label {                                                            // Pin a display for the selected item.
      laXY           <-- (mainNode.bipX, mainNode.bipEY + 50)                   //  .. the display's position.
      text           <-- "Name: " + selectedName + " " + selectedLast           //  .. the content of the display.
      prefWidthProp  <-- mainNode.bipW                                          //  .. the width is as for mainNode.
      alignment        = Pos.CENTER                                             //  .. the text should be centered.
      font             = new Font(32)                                           //  .. the fontsize = 32.
      textFill         = Color.DARKBLUE                                         //  .. the text should be dark blue.
    })
    <++(mainNode)                                                               // Pin the FXML-based Scene-definition.
    mainNode.layoutXY <-- (((1000.0,800.0) - mainNode.bipWH) / 2)               //  .. set it's scene-position.
    mainNode.draggable  = true                                                  //  .. make it draggable.
  }
  /* ................................................................................................................ */
}
/* === FXMLSample =============================== END =============================================================== */


/* Defining the Person-class used in the FXML-file ------------------------------------------------------------------ */
class Person(var firstName: String, var lastName: String, var email: String) {  // Make the necessary definitions.
  def this()                   = this("","","")                                 // FXML needs an empty constructor.
  def setFirstName (x: String) = firstName = x                                  // FXML needs the setters and getters.
  def setLastName  (x: String) = lastName  = x
  def setEmail     (x: String) = email     = x
  def getFirstName             = firstName
  def getLastName              = lastName
  def getEmail                 = email
}
/* .................................................................................................................. */


/* Defining the controller used in the FXML-file -------------------------------------------------------------------- */
class FXMLTableViewController {
  @FXML var tableView     : TableView[Person] = _                               // These fields will be filled
  @FXML var firstNameField: TextField         = _                               // by means of their definitions
  @FXML var lastNameField : TextField         = _                               // made in the FXML-file.
  @FXML var emailField    : TextField         = _

  @FXML                                                                         // This function is called from the
  def addPerson(event: ActionEvent) {                                           // button defined in the FXML file.
    tableView.items     ::= new Person (firstNameField.text ,                   // A new Person is instantiated.
                                        lastNameField.text  ,
                                        emailField.text     )
    firstNameField.text   = ""
    lastNameField.text    = ""
    emailField.text       = ""
  }

  def initialize() {                                                            // Defining the initialize-method,
    emailField.text <-- (firstNameField.text + "."         +                    // as required by FXML.
                         lastNameField.text  + "@gmail.com")                    // NOTE: we use SimpleFX-Bindings.
  }
}
/* .................................................................................................................. */

