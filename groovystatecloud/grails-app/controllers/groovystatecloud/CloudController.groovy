package groovystatecloud

import grails.converts.*

import gsc.Fetcher
import gsc.TagClouder

class CloudController {

    def index() {
      def fetcher = new Fetcher()
      def states = fetcher.scrape_or_run()
      def tagClouder = new TagClouder()
      def tagList = tagClouder.getWordList(states)
      [tags:tagList]
    }
}
