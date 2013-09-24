import gsc.Fetcher

class Runner {
  static void main(args) {
    def fetcher = new Fetcher()
    if (args.size() > 0 && args[0] == "purge") {
      fetcher.purge()
    }
    fetcher.scrape_or_run()
  }
}
