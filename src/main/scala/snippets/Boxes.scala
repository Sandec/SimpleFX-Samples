package samples.snippets

import simplefx.core._
import simplefx.all._

object Boxes extends App
@SimpleFXApp class Boxes {

  @Bind val t =  <--(time % (5 s) / (5 s))

  val myRoot = new Group(new Box(10,10,10) {material = new PhongMaterial(Color.GREY)})

  scene = new Scene(myRoot, 640, 640) {
    camera = new PerspectiveCamera(true) {
      transform <-- (new Rotate(180 * t, Rotate.X_AXIS) *
        new Rotate(180 * t, Rotate.Y_AXIS) *
        new Rotate(180 * t, Rotate.Z_AXIS) *
        new Translate(0,0,-(20 + t * 60)))
    }
  }
}
