
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

object Main extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*1.1*/("""<main>

    <div id="root">


    <section id="content">
        <div class="intro">
            <h1 class="intro_heading">
                        Welcome to RedditLytics:
            </h1>

            <div class="intro_search">

                <form id="queryForm" method="get" >
                    <label for="keyword">
                            </label>
                    <input
                    class="intro_search--input"
                    type="text"
                    id="queryInput"
                    name="keyword"
                    placeholder="Enter search keyword: " />

                    <input
                    class="intro_search--submit"
                    id="formSubmit"
                    type="submit" value="Go"/>
                </form>

            </div>

        </div>
    </section>


<br>
<div class="clear">
    <div><a href="/home/">Clear Results</a></div>

    <div><a href="/word/word-stats">Word Statistics</a></div>
</div>

    <section class="container">

</section>

    </div>
</main>"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/Main/Main.scala.html
                  HASH: 00d6f4607c79bbb3cea23718ab8cdf289565f2fa
                  MATRIX: 993->0
                  LINES: 32->1
                  -- GENERATED --
              */
          