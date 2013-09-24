package gsc
import gsc.PostalCode

class State {
  String code
  String name
  String countryCode
  Integer totalCount
  List postalCodes

  State(InputStream xmlStream) {
    def geonames = new XmlParser().parase(xmlStream)

    totalCount = geonames.totalResultsCount.text()

    def firstCode = geonames.code[0]
    code = firstCode.adminCode1.text()
    name = firstCode.adminName1.text()
    countryCode = firstCode.countryCode.text()
    
    postalCode = []
    geonames.code.findAll{
      postalCode = new PostalCode()
      postalCode.code = it.postcalcode.text()
      postalCode.name = it.name.text()
      postalCode.latitude = it.lat.text()
      postalCode.longitude = it.lng.text()
      postalCode.countyCode = it.adminCode2.text()
      postalCode.countyName = it.adminName2.text()
      
      postalCodes.add(postalCode)
    }
  }
}
