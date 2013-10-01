package gsc

class PostalCode {
  Integer code
  String name
  Float latitude
  Float longitude
  Integer countyCode
  String countyName

  PostalCode(c, n, lat, lng, cc, cn) {
    code = c
    name = n
    latitude = lat
    longitude = lng
    countyCode = cc
    countyName = cn
  }
  PostalCode() {}
}
