package cc.femto.kommon.mvi

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseModel<INTENT : Intent, ACTION : Action, VM> : Model<INTENT, ACTION, VM> {

    protected val disposables = CompositeDisposable()
    protected val viewModel: BehaviorSubject<VM> = BehaviorSubject.create()
    protected val actions: PublishSubject<ACTION> = PublishSubject.create()

    override fun viewModel(): Observable<VM> = viewModel.observeOn(AndroidSchedulers.mainThread())

    override fun actions(): Observable<ACTION> = actions.observeOn(AndroidSchedulers.mainThread())

    override fun detach() {
        disposables.clear()
    }

    protected fun <T : Event> createModel(events: Observable<out T>, initialViewModel: VM, reducer: (VM, T) -> VM) {
        disposables.add(events.scan(initialViewModel, reducer)
                .distinctUntilChanged()
                .subscribe(viewModel::onNext))
    }

    protected fun createActions(actions: Observable<out ACTION>) {
        disposables.add(actions.subscribe(this.actions::onNext))
    }
}