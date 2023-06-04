package cutejason.observer

interface Observable {
    fun addObserver(observer: Observer)
    fun removeObserver(observer: Observer)
    fun updateObservers()
}