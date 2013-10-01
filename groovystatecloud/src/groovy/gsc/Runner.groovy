import gsc.Fetcher
import gsc.TagClouder

class Runner {
  static void main(args) {
    def fetcher = new Fetcher()
    if (args.size() > 0 && args[0] == "purge") {
      fetcher.purge()
    }
    def states = fetcher.scrape_or_run()
    def tagClouder = new TagClouder()
    println tagClouder.getWordList(states)
  }
}
