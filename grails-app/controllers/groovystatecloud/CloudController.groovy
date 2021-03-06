package groovystatecloud

import grails.converters.JSON

import groovy.sql.Sql

import gsc.Fetcher
import gsc.TagClouder

class CloudController {
    Fetcher fetcher
    
    CloudController() {
      fetcher = new Fetcher()
    }
    
    def index() {
    }

    def getList() {
      def states = State.list(sort:'name')

      if (states.size == 0) {
        println 'scraping'
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
                                                                                    
        for (stateCode in stateCodes) {                                             
          def stateURL = "http://api.geonames.org/postalCodeSearch?placename=${stateCode[1]}&username=bbowlby"
          def stateStream = stateURL.toURL().openStream()                           
          def geonames = new XmlParser().parse(stateStream)
          def totalCount = geonames.totalResultsCount.text().toInteger()
          def state = new State(code:stateCode[1], name:stateCode[0],
                                countryCode:"US", totalCount:totalCount)
          geonames.code.findAll{
            state.addToPostalCodes(new PostalCode(code:it.postalcode.text().toInteger(),
                                                  name:it.name.text(),
                                                  latitude:it.lat.text().toFloat(),
                                                  longitude:it.lng.text().toFloat(),
                                                  countyCode:it.adminCode2.text().toInteger(),
                                                  countyName:it.adminName2.text()))
          }
          state.save()
        }
        states = State.list(sort:'name')
      }

      def tagClouder = new TagClouder()
      def tagList = tagClouder.getWordList(states)
      render tagList as JSON
    }

    def purge() {
      println "purgin"
      PostalCode.executeUpdate("delete PostalCode c where c.code > :oldCode",
                               [oldCode: 0])
      State.executeUpdate("delete State c where c.countryCode = :oldCode",
                          [oldCode: "US"])
      render 'true'
    }
}
