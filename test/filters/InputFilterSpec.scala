package filters

import base.SpecBase

class InputFilterSpec extends SpecBase with InputFilter {

  "Input filter" must {
    "filter out those hackers" in {

      filter("<script>(.*?)</script>").mustBe("")
      filter("<script(.*?)>").mustBe("")
      filter("</script>").mustBe("")
      filter("javascript:").mustBe("")
      filter("vbscript:").mustBe("")
      filter("onload(.*?)=").mustBe("")
      filter("eval((.*?)").mustBe("")
      filter("expression((.*?)").mustBe("")

    }
  }
}
