import gsc.State

class Fetcher {
  public static main(args) {
    def mnstream = 'http://api.geonames.org/postalCodeSearch?placename=MN&username=demo'.toURL().openStream()
    def mn = new State(mnstream)
    println mn.name
  }
}
