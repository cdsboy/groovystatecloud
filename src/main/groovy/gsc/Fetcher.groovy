package gsc
import gsc.State

class Fetcher {
  public static main(args) {
    def mnstream = 'http://api.geonames.org/postalCodeSearch?placename=MN&username=bbowlby'.toURL().openStream()
    def mn = new State(mnstream)
    println mn.name
    for (code in mn.postalCodes) {
      println code.code
    }
  }
}
