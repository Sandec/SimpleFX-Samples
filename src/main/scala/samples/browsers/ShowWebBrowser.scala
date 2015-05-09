package samples.browsers

import simplefx.core._
import simplefx.all._
import simplefx.scene.paint.Color._
import simplefx.experimental._


/* === ShowWebBrowser =========================== START ============================================================= */
object ShowWebBrowser extends App
@SimpleFXApp class ShowWebBrowser { title = "SimpleFX Sample - WebBrowser"

  /* Create the Scene ----------------------------------------------------------------------------------------------- */
  val browser = new WebBrowser { styleClass ::= "browser" }
  scene = new Scene (browser, 800, 600, SLATEGREY) {
    stylesheets ::= "/browsers/BrowserToolbar.css"
  }
  /* ................................................................................................................ */
}
/* === ShowWebBrowser =========================== END =============================================================== */

