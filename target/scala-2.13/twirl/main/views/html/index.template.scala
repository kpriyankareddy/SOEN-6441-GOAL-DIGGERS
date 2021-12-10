
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.data._
import play.core.j.PlayFormsMagicForJava._
import scala.jdk.CollectionConverters._
/*1.2*/import views.html.Header.Header
/*2.2*/import views.html.Main.Form
/*3.2*/import views.html.Main.Main

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[play.mvc.Http.Request,org.webjars.play.WebJarsUtil,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*5.2*/(request: play.mvc.Http.Request, webJarsUtil: org.webjars.play.WebJarsUtil):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*5.77*/("""
"""),format.raw/*6.1*/("""<!DOCTYPE html>

<html>
<head>
    <title>Reactive Stock News Dashboard</title>

    <link rel="shortcut icon" type="image/png" href=""""),_display_(/*12.55*/routes/*12.61*/.Assets.at("images/favicon.png")),format.raw/*12.93*/("""">


    <link rel="stylesheet" media="screen" href=""""),_display_(/*15.50*/routes/*15.56*/.Assets.at("stylesheets/main.css")),format.raw/*15.90*/("""">

    """),_display_(/*17.6*/webJarsUtil/*17.17*/.locate("jquery.min.js").script()),format.raw/*17.50*/("""
    """),_display_(/*18.6*/webJarsUtil/*18.17*/.locate("jquery.flot.js").script()),format.raw/*18.51*/("""
    """),format.raw/*19.5*/("""<script type='text/javascript' src='"""),_display_(/*19.42*/routes/*19.48*/.Assets.at("javascripts/index.js")),format.raw/*19.82*/("""'></script>
</head>
<body data-ws-url=""""),_display_(/*21.21*/routes/*21.27*/.HomeController.ws.webSocketURL(request)),format.raw/*21.67*/("""">
    """),_display_(/*22.6*/Header()),format.raw/*22.14*/("""
    """),_display_(/*23.6*/Main()),format.raw/*23.12*/("""

"""),format.raw/*25.1*/("""</body>
</html>
"""))
      }
    }
  }

  def render(request:play.mvc.Http.Request,webJarsUtil:org.webjars.play.WebJarsUtil): play.twirl.api.HtmlFormat.Appendable = apply(request,webJarsUtil)

  def f:((play.mvc.Http.Request,org.webjars.play.WebJarsUtil) => play.twirl.api.HtmlFormat.Appendable) = (request,webJarsUtil) => apply(request,webJarsUtil)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/index.scala.html
                  HASH: 54cec74e1a1ccfb47e66007593eeaf8c55733c4b
                  MATRIX: 610->1|649->35|684->65|1060->97|1230->172|1258->174|1426->315|1441->321|1494->353|1578->410|1593->416|1648->450|1685->461|1705->472|1759->505|1792->512|1812->523|1867->557|1900->563|1964->600|1979->606|2034->640|2103->682|2118->688|2179->728|2214->737|2243->745|2276->752|2303->758|2334->762
                  LINES: 23->1|24->2|25->3|30->5|35->5|36->6|42->12|42->12|42->12|45->15|45->15|45->15|47->17|47->17|47->17|48->18|48->18|48->18|49->19|49->19|49->19|49->19|51->21|51->21|51->21|52->22|52->22|53->23|53->23|55->25
                  -- GENERATED --
              */
          