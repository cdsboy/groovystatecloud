package groovystatecloud

class PostalCode {
  Integer code
  String name
  Float latitude
  Float longitude
  Integer countyCode
  String countyName

  static belongsTo = [state: State]

  static constraints = {
    name(blank: false)
    countyName(blank: false)
  }
}
