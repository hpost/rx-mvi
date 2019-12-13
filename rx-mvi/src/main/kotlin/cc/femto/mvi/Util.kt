package cc.femto.mvi

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

fun <ACTION : Action, STATE, T : ACTION> attachComponent(
    view: View<ACTION, STATE>,
    model: Model<ACTION, STATE>,
    actions: Observable<T> = Observable.empty(),
    block: () -> Unit = {}
) {
    view.attach(model.state().observeOn(AndroidSchedulers.mainThread()))
    model.attach(view.actions().mergeWith(actions))
    block()
}

fun <ACTION : Action, STATE, T : ACTION> detachComponent(
        view: BaseView<ACTION, STATE>,
        model: Model<ACTION, STATE>
) {
    view.detach()
    model.detach()
}
