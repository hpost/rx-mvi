package cc.femto.mvi

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable

fun <ACTION : Action, STATE : Any, T : ACTION> attachComponent(
    view: View<ACTION, STATE>,
    model: Model<ACTION, STATE>,
    actions: Observable<T> = Observable.empty(),
    block: () -> Unit = {}
) {
    model.attach(view.actions().mergeWith(actions))
    view.attach(model.state().observeOn(AndroidSchedulers.mainThread()))
    block()
}

fun <ACTION : Action, STATE : Any, T : ACTION> detachComponent(
    view: BaseView<ACTION, STATE>,
    model: Model<ACTION, STATE>
) {
    view.detach()
    model.detach()
}
