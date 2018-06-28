package cc.femto.kommon.mvi

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

abstract class BaseModel<INTENT : Intent, ACTION : Action, VM> : Model<INTENT, ACTION, VM> {

    protected val disposables = CompositeDisposable()
    protected val viewModel: BehaviorSubject<VM> = BehaviorSubject.create()
    protected val actions: PublishSubject<ACTION> = PublishSubject.create()
    private val events: PublishSubject<Event> = PublishSubject.create()

    override fun viewModel(): Observable<VM> = viewModel

    override fun actions(): Observable<ACTION> = actions

    override fun detach() {
        disposables.clear()
    }

    /**
     * Exposes the internal [Event] stream that passes through the reducer
     */
    protected fun events(): Observable<Event> = events

    /**
     * Dispatch an event to the reducer
     */
    fun dispatchEvent(event: Event) = events.onNext(event)

    /**
     * Subscribes internal event relay to supplied event stream and sets up [viewModel]
     */
    protected fun makeViewModel(events: Observable<out Event>, initialViewModel: VM, reducer: (VM, Event) -> VM) {
        disposables.add(this.events
                .scan(initialViewModel, reducer)
                .distinctUntilChanged()
                .subscribe(viewModel::onNext))
        disposables.add(events.subscribe(this.events::onNext))
    }

    /**
     * Subscribes internal action relay to supplied action stream and sets up [actions]
     */
    protected fun makeActions(actions: Observable<out ACTION>) {
        disposables.add(actions.subscribe(this.actions::onNext))
    }
}