import gsc.Fetcher

class Runner {
  static void main(args) {
    def fetcher = new Fetcher()
    fetcher.scrape_or_run()
  }
}
