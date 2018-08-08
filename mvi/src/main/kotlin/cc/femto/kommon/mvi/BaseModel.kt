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

    override fun attach(intents: Observable<INTENT>) {
        makeViewModel(
                eventsFrom(intents),
                initialViewModel(),
                ::reduce
        )
        disposables.add(sideEffectsFrom(intents))
    }

    override fun detach() {
        disposables.clear()
    }

    /**
     * @return instance of [VM] representing the initial state
     */
    protected abstract fun initialViewModel(): VM

    /**
     * Define state mutation events that result in view model changes
     *
     * NB: Operations that don't cause a state mutation reside in [sideEffectsFrom]
     *
     * @return [Observable] of [Event] feeding into [reduce]
     */
    protected abstract fun eventsFrom(intents: Observable<INTENT>): Observable<out Event>

    /**
     * Reduce state mutations to updated view models
     *
     * NB: Should be a pure function without side effects
     */
    protected abstract fun reduce(model: VM, event: Event): VM

    /**
     * Define side effects that don't result in state mutations, if any
     *
     * Default implementation results in no-op.
     *
     * NB: Returned subscriptions will be added to [disposables] and disposed in [detach]
     *
     * @return [CompositeDisposable] containing subscriptions that need to be managed
     */
    protected open fun sideEffectsFrom(intents: Observable<INTENT>): CompositeDisposable {
        return CompositeDisposable()
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