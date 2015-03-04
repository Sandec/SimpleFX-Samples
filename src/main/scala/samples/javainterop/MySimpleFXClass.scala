package samples.javainterop

import simplefx.core._
import simplefx.all._

class MySimpleFXClass extends HBox {

  <++(new Label("I'm from SimpleFX"))
  <++(new CheckBox{selected = true})
}
