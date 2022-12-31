package baitaplon.babyphoto.data.model

class DataResult<T> {

    var state: State = State.SUCCESS
    var data: T? = null

    enum class State {
        SUCCESS, FAIL, ERROR
    }

}
