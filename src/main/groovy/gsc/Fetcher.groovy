package gsc
import gsc.State

class Fetcher {
  public static main(args) {
    final stateCodes = ['US-AL', 'US-AK', 'US-AZ', 'US-AR', 'US-CA', 'US-CO',
                        'US-CT', 'US-DE', 'US-FL', 'US-GA', 'US-HI', 'US-ID',
                        'US-IL', 'US-IN', 'US-IA', 'US-KS', 'US-KY', 'US-LA',
                        'US-ME', 'US-MD', 'US-MA', 'US-MI', 'US-MN', 'US-MS',
                        'US-MO', 'US-MT', 'US-NE', 'US-NV', 'US-NH', 'US-NJ',
                        'US-NM', 'US-NY', 'US-NC', 'US-ND', 'US-OH', 'US-OK',
                        'US-OR', 'US-PA', 'US-RI', 'US-SC', 'US-SD', 'US-TN',
                        'US-TX', 'US-UT', 'US-VT', 'US-VA', 'US-WA', 'US-WV',
                        'US-WI', 'US-WY']

    def states = []
    for (stateCode in stateCodes) {
      def stateURL = "http://api.geonames.org/postalCodeSearch?placename=$stateCode&username=bbowlby"
      def stateStream = stateURL.toURL().openStream()
      def state = new State(stateStream)
      states.add(state)
    }
  }
}
