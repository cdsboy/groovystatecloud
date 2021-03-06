package gsc

import groovy.sql.Sql
import gsc.State
import gsc.PostalCode

class Fetcher {
  def purge() {
    def sql = Sql.newInstance('jdbc:sqlite:database.sqlite', 'org.sqlite.JDBC')
    
    def metadata = sql.connection.getMetaData()
    def statesTables =  metadata.getTables(null, null, "states", null)
    def codesTables =  metadata.getTables(null, null, "postalcodes", null)

    if (codesTables.next()) {
      sql.execute("DROP TABLE postalcodes")
    }
    if (statesTables.next()) {
      sql.execute("DROP TABLE states")
    }
  }

  def scrape_or_run() {
    def sql = Sql.newInstance('jdbc:sqlite:database.sqlite', 'org.sqlite.JDBC')

    def metadata = sql.connection.getMetaData()
    def tables =  metadata.getTables(null, null, "states", null)

    def states
    if (!tables.next()) {
      states = scrape()
      states.sort{a,b -> b.name<=>a.name}

      sql.execute("CREATE TABLE states(id INTEGER PRIMARY KEY, code TEXT, name TEXT, countrycode TEXT, totalcount INTEGER)")
      sql.execute("CREATE TABLE postalcodes(stateid INTEGER, code INTEGER, name TEXT, lat FLOAT, long FLOAT, countycode INTEGER, countyname TEXT, FOREIGN KEY(stateid) REFERENCES states(id))")
    
      for (state in states) {
        sql.execute("INSERT INTO states(code, name, countrycode, totalcount) VALUES(?, ?, ?, ?)",
                    [state.code, state.name, state.countryCode, state.totalCount])

        def rows = sql.rows("SELECT id FROM states WHERE name = ?", [state.name])
        assert rows.size() == 1
        def id = rows[0][0]

        for (postalCode in state.postalCodes) {
          sql.execute("INSERT INTO postalcodes(stateid, code, name, lat, long, countycode, countyname) VALUES(?, ?, ?, ?, ?, ?, ?)",
                      [id, postalCode.code, postalCode.name, postalCode.latitude,
                       postalCode.longitude, postalCode.countyCode,
                       postalCode.countyName])
        }
      }
    } else {
      states = []
      sql.eachRow('SELECT * FROM states ORDER BY name ASC') { row ->
        def state = new State(row[1], row[2], row[3], row[4])
        sql.eachRow('SELECT * FROM postalcodes WHERE stateid = ?', [row[0]]) { prow ->
          def postalCode = new PostalCode(prow[1], prow[2], prow[3], prow[4],
                                          prow[5], prow[6])
          state.postalCodes.add(postalCode)
        }
        states.add(state)
      }
    }
    return states
  }

  def scrape() {
    final stateCodes = [['Alabama', 'US-AL'], ['Alaska', 'US-AK'],
                        ['Arizona', 'US-AZ'], ['Arkansas', 'US-AR'],
                        ['California', 'US-CA'], ['Colorado', 'US-CO'],
                        ['Connecticut', 'US-CT'], ['Delaware', 'US-DE'], 
                        ['Florida', 'US-FL'], ['Georgia', 'US-GA'], 
                        ['Hawaii', 'US-HI'], ['Idaho', 'US-ID'],
                        ['Illinois', 'US-IL'], ['Indiana', 'US-IN'],
                        ['Iowa', 'US-IA'], ['Kansas', 'US-KS'],
                        ['Kentucky', 'US-KY'], ['Louisiana', 'US-LA'],
                        ['Maine', 'US-ME'], ['Maryland', 'US-MD'],
                        ['Massachusetts', 'US-MA'], ['Michigan', 'US-MI'],
                        ['Minnesota', 'US-MN'], ['Mississippi', 'US-MS'],
                        ['Missouri', 'US-MO'], ['Montana', 'US-MT'],
                        ['Nebraska', 'US-NE'], ['Nevada', 'US-NV'],
                        ['New Hampshire', 'US-NH'], ['New Jersey', 'US-NJ'],
                        ['New Mexico', 'US-NM'], ['New York', 'US-NY'],
                        ['North Carolina', 'US-NC'], ['North Dakota', 'US-ND'],
                        ['Ohio', 'US-OH'], ['Oklahoma', 'US-OK'],
                        ['Oregon', 'US-OR'], ['Pennsylvania', 'US-PA'], 
                        ['Rhode Island', 'US-RI'], ['South Carolina', 'US-SC'],
                        ['South Dakota', 'US-SD'], ['Tennessee', 'US-TN'],
                        ['Texas', 'US-TX'], ['Utah', 'US-UT'],
                        ['Vermont', 'US-VT'], ['Virginia', 'US-VA'],
                        ['Washington', 'US-WA'], ['West Virginia', 'US-WV'],
                        ['Wisconsin', 'US-WI'], ['Wyoming', 'US-WY']]

    def states = []
    for (stateCode in stateCodes) {
      def stateURL = "http://api.geonames.org/postalCodeSearch?placename=${stateCode[1]}&username=bbowlby"
      def stateStream = stateURL.toURL().openStream()
      def state = new State(stateStream, stateCode[0], stateCode[1])
      states.add(state)
    }

    return states
  }
}
