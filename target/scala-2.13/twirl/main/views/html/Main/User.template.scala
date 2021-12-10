
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

object User extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template2[UserDetails,util.List[UserSubmission],play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*2.2*/(userDetails : UserDetails , userSubmissions : util.List[UserSubmission]):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.75*/("""
"""),format.raw/*3.1*/("""<head>
    <title>Reactive Stock News Dashboard</title>

    <link rel="shortcut icon" type="image/png" href=""""),_display_(/*6.55*/routes/*6.61*/.Assets.at("images/favicon.png")),format.raw/*6.93*/("""">


    <link rel="stylesheet" media="screen" href=""""),_display_(/*9.50*/routes/*9.56*/.Assets.at("stylesheets/main.css")),format.raw/*9.90*/("""">

    <script type='text/javascript' src='"""),_display_(/*11.42*/routes/*11.48*/.Assets.at("javascripts/index.js")),format.raw/*11.82*/("""'></script>
</head>
<div id="root">
<section class="card">
    <div class="back-button">
        <button value="Back" type="Button" onclick="history.go(-1);">Go back</button>
    </div>
    """),_display_(/*18.6*/if(userDetails != null && userDetails.getData != null)/*18.60*/ {_display_(Seq[Any](format.raw/*18.62*/("""
        """),format.raw/*19.9*/("""<section class="user_info">
            <div class="user_info--profile">
            """),_display_(/*21.14*/if(userDetails.getData.getSnoovatar_img())/*21.56*/ {_display_(Seq[Any](format.raw/*21.58*/("""
                """),format.raw/*22.17*/("""<img alt="Profile picture" src=""""),_display_(/*22.50*/userDetails/*22.61*/.getData.getSnoovatar_img),format.raw/*22.86*/("""" class="user_info--profile-img"/>
            """)))}/*23.15*/else/*23.20*/{_display_(Seq[Any](format.raw/*23.21*/("""
                """),_display_(/*24.18*/if(userDetails.getData.getIcon_img)/*24.53*/ {_display_(Seq[Any](format.raw/*24.55*/("""
                    """),format.raw/*25.21*/("""<img alt="Profile picture" src=""""),_display_(/*25.54*/userDetails/*25.65*/.getData.getIcon_img),format.raw/*25.85*/("""" class="user_info--profile-img"/>
                """)))}/*26.19*/else/*26.24*/{_display_(Seq[Any](format.raw/*26.25*/("""
                    """),format.raw/*27.21*/("""<img alt="Profile picture" src="/assets/images/default.png" class="user_info--profile-img" />
                """)))}),format.raw/*28.18*/("""
            """)))}),format.raw/*29.14*/("""
            """),format.raw/*30.13*/("""</div>
            <div class="user_info--details">
                <h1>"""),_display_(/*32.22*/userDetails/*32.33*/.getData.getName),format.raw/*32.49*/("""</h1>
                <p class="title">Total Karma : """),_display_(/*33.49*/userDetails/*33.60*/.getData.getTotal_karma),format.raw/*33.83*/("""</p>
                """),_display_(/*34.18*/if(userDetails.getData.getSubreddit != null)/*34.62*/ {_display_(Seq[Any](format.raw/*34.64*/("""
                    """),format.raw/*35.21*/("""<p class="title">Total Subscribers : """),_display_(/*35.59*/userDetails/*35.70*/.getData.getSubreddit.getSubscribers),format.raw/*35.106*/("""</p>
                """)))}),format.raw/*36.18*/("""
            """),format.raw/*37.13*/("""</div>
        </section>

    """)))}/*40.7*/else/*40.12*/{_display_(Seq[Any](format.raw/*40.13*/("""
        """),format.raw/*41.9*/("""<span style="font-weight: bold;">User Details not found!!</span>
    """)))}),format.raw/*42.6*/("""
"""),format.raw/*43.1*/("""</section>
"""),_display_(/*44.2*/if(userSubmissions != null)/*44.29*/ {_display_(Seq[Any](format.raw/*44.31*/("""
    """),_display_(/*45.6*/for((submission, idx) <- userSubmissions.zipWithIndex) yield /*45.60*/ {_display_(Seq[Any](format.raw/*45.62*/("""
        """),format.raw/*46.9*/("""<section class="card">
            <h4><span> """),_display_(/*47.25*/{
                {idx} + 1
            }),format.raw/*49.14*/(""" """),format.raw/*49.15*/(""". """),_display_(/*49.18*/submission/*49.28*/.getTitle),format.raw/*49.37*/("""
            """),format.raw/*50.13*/("""</span>
            </h4>
            <p>"""),_display_(/*52.17*/submission/*52.27*/.getSelftext),format.raw/*52.39*/("""</p>
            <p><span style="font-weight: bold;">Posted Date :</span>
                """),_display_(/*54.18*/submission/*54.28*/.getDate),format.raw/*54.36*/("""</p>
            <a href=""""),_display_(/*55.23*/submission/*55.33*/.getFull_link),format.raw/*55.46*/("""" target="_blank">"""),_display_(/*55.65*/submission/*55.75*/.getFull_link),format.raw/*55.88*/("""</a>
            <p></p>
        </section>
    """)))}),format.raw/*58.6*/("""
""")))}/*59.3*/else/*59.8*/{_display_(Seq[Any](format.raw/*59.9*/("""
    """),format.raw/*60.5*/("""<section class="card">
        <span style="font-weight: bold;">No Submissions found!</span>
    </section>
""")))}),format.raw/*63.2*/("""
"""),format.raw/*64.1*/("""</div>"""))
      }
    }
  }

  def render(userDetails:UserDetails,userSubmissions:util.List[UserSubmission]): play.twirl.api.HtmlFormat.Appendable = apply(userDetails,userSubmissions)

  def f:((UserDetails,util.List[UserSubmission]) => play.twirl.api.HtmlFormat.Appendable) = (userDetails,userSubmissions) => apply(userDetails,userSubmissions)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/Main/User.scala.html
                  HASH: 24e70316e1d183d48931795de2cccb91f1efa8b7
                  MATRIX: 615->1|966->20|1134->93|1162->95|1302->209|1316->215|1368->247|1451->304|1465->310|1519->344|1593->391|1608->397|1663->431|1887->629|1950->683|1990->685|2027->695|2142->783|2193->825|2233->827|2279->845|2339->878|2359->889|2405->914|2473->964|2486->969|2525->970|2571->989|2615->1024|2655->1026|2705->1048|2765->1081|2785->1092|2826->1112|2898->1166|2911->1171|2950->1172|3000->1194|3143->1306|3189->1321|3231->1335|3333->1410|3353->1421|3390->1437|3472->1492|3492->1503|3536->1526|3586->1549|3639->1593|3679->1595|3729->1617|3794->1655|3814->1666|3872->1702|3926->1725|3968->1739|4021->1775|4034->1780|4073->1781|4110->1791|4211->1862|4240->1864|4279->1877|4315->1904|4355->1906|4388->1913|4458->1967|4498->1969|4535->1979|4610->2027|4674->2070|4703->2071|4733->2074|4752->2084|4782->2093|4824->2107|4895->2151|4914->2161|4947->2173|5067->2266|5086->2276|5115->2284|5170->2312|5189->2322|5223->2335|5269->2354|5288->2364|5322->2377|5404->2429|5425->2433|5437->2438|5475->2439|5508->2445|5650->2557|5679->2559
                  LINES: 23->1|28->2|33->2|34->3|37->6|37->6|37->6|40->9|40->9|40->9|42->11|42->11|42->11|49->18|49->18|49->18|50->19|52->21|52->21|52->21|53->22|53->22|53->22|53->22|54->23|54->23|54->23|55->24|55->24|55->24|56->25|56->25|56->25|56->25|57->26|57->26|57->26|58->27|59->28|60->29|61->30|63->32|63->32|63->32|64->33|64->33|64->33|65->34|65->34|65->34|66->35|66->35|66->35|66->35|67->36|68->37|71->40|71->40|71->40|72->41|73->42|74->43|75->44|75->44|75->44|76->45|76->45|76->45|77->46|78->47|80->49|80->49|80->49|80->49|80->49|81->50|83->52|83->52|83->52|85->54|85->54|85->54|86->55|86->55|86->55|86->55|86->55|86->55|89->58|90->59|90->59|90->59|91->60|94->63|95->64
                  -- GENERATED --
              */
          