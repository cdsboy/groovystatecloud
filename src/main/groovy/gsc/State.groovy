package gsc
import gsc.PostalCode

class State {
  String code
  String name
  String countryCode
  Integer totalCount
  List postalCodes

  State(xmlStream, stateName, stateCode) {
    def geonames = new XmlParser().parse(xmlStream)

    totalCount = geonames.totalResultsCount.text().toInteger()
    
    name = stateName
    code = stateCode
    countryCode = "US"

    postalCodes = []
    geonames.code.findAll{
      def postalCode = new PostalCode()
      postalCode.code = it.postalcode.text().toInteger()
      postalCode.name = it.name.text()
      postalCode.latitude = it.lat.text().toFloat()
      postalCode.longitude = it.lng.text().toFloat()
      postalCode.countyCode = it.adminCode2.text().toInteger()
      postalCode.countyName = it.adminName2.text()
      
      postalCodes.add(postalCode)
    }
  }

  State(c, n, cc, t) {
    code = n
    name = n
    countryCode = cc
    totalCount = t
    postalCodes = []
  }
}
