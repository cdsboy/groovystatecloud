package gsc

class TagClouder {
  def getWordList(states) {
    def statewords = []

    def tmax = states.max{ it.totalCount }.totalCount
    def tmin = states.min{ it.totalCount }.totalCount
    def fmax = 20

    for (state in states) {
      def s = 10 + ((fmax * (state.totalCount - tmin))/(tmax - tmin))
      statewords.add([state.name, s])
    }
    
    return statewords
  }
}

