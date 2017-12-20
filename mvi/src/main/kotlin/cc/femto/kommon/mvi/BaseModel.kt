package cc.femto.kommon.mvi

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.subjects.BehaviorSubject
import rx.subjects.PublishSubject
import rx.subscriptions.CompositeSubscription

abstract class BaseModel<INTENT : Intent, ACTION : Action, VM> : Model<INTENT, ACTION, VM> {

    protected val subscriptions = CompositeSubscription()
    protected val viewModel: BehaviorSubject<VM> = BehaviorSubject.create()
    protected val actions: PublishSubject<ACTION> = PublishSubject.create()

    override fun viewModel(): Observable<VM> = viewModel.observeOn(AndroidSchedulers.mainThread())

    override fun actions(): Observable<ACTION> = actions.observeOn(AndroidSchedulers.mainThread())

    override fun detach() {
        subscriptions.clear()
    }

    protected fun <T : Event> createModel(events: Observable<out T>, initialViewModel: VM, reducer: (VM, T) -> VM) {
        subscriptions.add(events.scan(initialViewModel, reducer)
                .distinctUntilChanged()
                .subscribe(viewModel))
    }

    protected fun createActions(actions: Observable<out ACTION>) {
        subscriptions.add(actions.subscribe(this.actions))
    }
}