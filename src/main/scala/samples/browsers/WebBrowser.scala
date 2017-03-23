package samples.browsers

import simplefx.core._
import simplefx.all._
import simplefx.experimental._
import simplefx.all.Pos._
import javafx.concurrent.Worker.State._

/* === WebBrowser =============================== START ============================================================= */
/*
 *      This sample shows a simple browser consisting of two main objects:
 *
 *      a renderer(WebView) and a toolBar(HBox).
 *
 *      In addition to "normal rendering" this browser shows how it can
 *
 *        - use the toolbar to decide which page to be loaded(rendered)
 *        - use a combobox to store the page-history
 *        - trigger loading of selected pages from the combobox
 *        - catch popups and render their content in a separate window
 *        - trigger javascript-methods of certain web-pages
 *        - register own methods for certain web-pages, for them to call those
 *          methods from their javascript code.
 *
 *      The toolbar consists of
 *
 *          a combobox   (Combobox )	,
 *          hyperlinks   (Hyperlink)	,
 *          a filler     (Region   ) 	and
 *          a docToggler (Button   )	.
 *
 * .................................................................................................................. */


//class WebBrowser extends SimpleFXRegion {
//
//  private def pin  	     = this		                          // Pin to the screen from here ...
//  private def TOGGLE_TXT = "Toggle previous Docs"	          // Text for the button.
//  private def JS_TOGGLE  = "toggleDisplay('PrevRel')"	      // A js-method of the HTML-page.
//  @Bind var lastWasDoc   = false                            // Last loaded == "Documentation".
//
//
//  /* Declare and pin the Browser & the smallView. ------------------------------------------------------------------- */
//  object jsInterface { def exit() = javafx.application.Platform.exit() }
//  lazy val renderer = new WebView {
//    prefWidthProp  <--  pin.width                           // Binds this' width to scene's width.
//    prefHeightProp <-- (pin.height - toolBar.height)	      // Binds the height.
//    when(loadOk)   --> { global.window.app = jsInterface }
//  }.extended
//  /* ................................................................................................................ */
//
//
//  /* Declare and fill the toolbar. ---------------------------------------------------------------------------------- */
//  private lazy val toolBar = new HBox()  {
//    styleClass	::= "browser-toolbar"                       // ID for the CSS-file.
//    alignment       = javafx.geometry.Pos.CENTER            // Horizontal alignment.
//    layoutXY      <-- (0, pin.height-height)                // Point for the toolbar's position.
//    prefWidthProp <-- pin.width                             // Binds toolbar's width to total width.
//    hgrowAll	      = true                                  // Enable growing for All.
//  }
//
//  /* A combobox. */
//  type WebHistoryEntry = javafx.scene.web.WebHistory#Entry
//  private lazy val comboBox = new ComboBox[WebHistoryEntry]{  // Here used for the pages-history.
//    def offs        = selectedIndex - renderer.currentIndex   // Just a practical helper method.
//    prefWidthProp   = 60
//    onAction      --> renderer.goHistory  (offs)              // Load previous page from the history.
//    entries       <-- renderer.entries                        // Bind the History to the Combobox.
//  }
//
//  /* Hyperlinks */
//  def createHyperlink(caption: String, image: String, url: String) = new Hyperlink(caption, new ImageView(image)) {
//    onAction --> {
//      renderer.engine.load(url)
//      lastWasDoc = (caption=="Documentation")
//  }}
//  val hyperlinks = List(
//    createHyperlink("Products"     , "/browsers/Product.png"      , "http://www.oracle.com/products/index.html"),
//    createHyperlink("Blogs"        , "/browsers/Blog.png"         , "http://blogs.oracle.com"                  ),
//    createHyperlink("Documentation", "/browsers/Documentation.png", "http://docs.oracle.com/javase/index.html" ),
//    createHyperlink("Partners"     , "/browsers/Partners.png"     , "http://www.oracle.com/partners/index.html"),
//    createHyperlink("Help"         , "/browsers/Help.png", getClass.getResource("/browsers/help.html").toExternalForm)
//  )
//
//  /* A toggler for the documentation pages ("docToggler"). */
//  private lazy val docToggler = new Button(TOGGLE_TXT){
//    onAction  --> {renderer.executeScript(JS_TOGGLE)}               // Calls a js-method of the HTML-page.
//    visible   <-- {lastWasDoc && renderer.loadOk}                   // Visible iff last caption was a "doc",
//  }                                                                 // and loading finished successfully.
//  /* ................................................................................................................ */
//
//
//  /* Pin all nodes to the scene-graph. ------------------------------------------------------------------------------ */
//  toolBar.children = comboBox :: hyperlinks ::: new Region :: docToggler :: Nil
//  pin      <++ (renderer, toolBar)
//  /* ................................................................................................................ */
//
//
//  /* Load the start page -------------------------------------------------------------------------------------------- */
//  renderer.engine.load("http://www.oracle.com/products/index.html")
//  /* ................................................................................................................ */
//}
///* === WebBrowser =============================== END   ============================================================= */