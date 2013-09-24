package gsc
import gsc.PostalCode

class State {
  String code
  String name
  String countryCode
  Integer totalCount
  List postalCodes

  State(InputStream xmlStream) {
    def geonames = new XmlParser().parse(xmlStream)

    totalCount = geonames.totalResultsCount.text().toInteger()

    def firstCode = geonames.code[0]
    code = firstCode.adminCode1.text()
    name = firstCode.adminName1.text()
    countryCode = firstCode.countryCode.text()
    
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
}
