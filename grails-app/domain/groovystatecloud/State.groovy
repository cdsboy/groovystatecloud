package groovystatecloud

class State {
  String code
  String name
  String countryCode
  Integer totalCount
  
  static hasMany = [postalCodes: PostalCode]

  static constraints = {
    code(blank: false)
    name(blank: false)
    countryCode(blank: false)
  }
}
