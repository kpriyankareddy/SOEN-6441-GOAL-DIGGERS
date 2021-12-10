
package views.html.Main

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
/*1.2*/import java.util

object WordStats extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template1[util.Map[String, Integer],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(stats: util.Map[String, Integer]):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.36*/("""
"""),format.raw/*3.1*/("""<head>
    <title>Reactive Stock News Dashboard</title>

    <link rel="shortcut icon" type="image/png" href=""""),_display_(/*6.55*/routes/*6.61*/.Assets.at("images/favicon.png")),format.raw/*6.93*/("""">


    <link rel="stylesheet" media="screen" href=""""),_display_(/*9.50*/routes/*9.56*/.Assets.at("stylesheets/main.css")),format.raw/*9.90*/("""">

    <script type='text/javascript' src='"""),_display_(/*11.42*/routes/*11.48*/.Assets.at("javascripts/index.js")),format.raw/*11.82*/("""'></script>
</head>


<div id="root">
        <section id="content">
            <div class="intro">

                <div class="back-icon">
                    <button value="Back" type="Button" onclick="history.go(-1);">Back</button>
                </div>

                <h2 style="text-align: center">Word level statistics of submissions: </h2>

                <div class="intro_search">
                    """),_display_(/*26.22*/if(stats != null )/*26.40*/ {_display_(Seq[Any](format.raw/*26.42*/("""
                        """),_display_(/*27.26*/for((word,count) <- stats) yield /*27.52*/ {_display_(Seq[Any](format.raw/*27.54*/("""
                            """),format.raw/*28.29*/("""<span> """),_display_(/*28.37*/{word}),format.raw/*28.43*/(""" """),format.raw/*28.44*/("""- </span>
                            <span> """),_display_(/*29.37*/{count}),format.raw/*29.44*/(""" """),format.raw/*29.45*/("""</span>
                            <br>
                            """)))}),format.raw/*31.30*/("""
                    """)))}),format.raw/*32.22*/("""
                """),format.raw/*33.17*/("""</div>
            </div>
        </section>
</div>"""))
      }
    }
  }

  def render(stats:util.Map[String, Integer]): play.twirl.api.HtmlFormat.Appendable = apply(stats)

  def f:((util.Map[String, Integer]) => play.twirl.api.HtmlFormat.Appendable) = (stats) => apply(stats)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/Main/WordStats.scala.html
                  HASH: 310d299cf6a20c6bb60283a27c9efeb5401cd945
                  MATRIX: 615->1|959->20|1088->54|1116->56|1256->170|1270->176|1322->208|1405->265|1419->271|1473->305|1547->352|1562->358|1617->392|2076->824|2103->842|2143->844|2197->871|2239->897|2279->899|2337->929|2372->937|2399->943|2428->944|2502->991|2530->998|2559->999|2662->1071|2716->1094|2762->1112
                  LINES: 23->1|28->2|33->2|34->3|37->6|37->6|37->6|40->9|40->9|40->9|42->11|42->11|42->11|57->26|57->26|57->26|58->27|58->27|58->27|59->28|59->28|59->28|59->28|60->29|60->29|60->29|62->31|63->32|64->33
                  -- GENERATED --
              */
          