package groovystatecloud

import grails.converters.JSON

import gsc.Fetcher
import gsc.TagClouder

class CloudController {
    Fetcher fetcher
    
    CloudController() {
      fetcher = new Fetcher()
    }
    
    def index() {
      def states = fetcher.scrape_or_run()
      def tagClouder = new TagClouder()
      def tagList = tagClouder.getWordList(states)
      [tags:tagList]
    }

    def getList() {
      def states = fetcher.scrape_or_run()
      def tagClouder = new TagClouder()
      def tagList = tagClouder.getWordList(states)
      render tagList as JSON
    }

    def purge() {
      fetcher.purge()
      render 'true'
    }
}
