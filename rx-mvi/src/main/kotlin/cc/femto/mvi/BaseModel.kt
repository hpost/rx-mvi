package cc.femto.mvi

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BaseModel<ACTION : Action, STATE : Any> : Model<ACTION, STATE> {

    protected val disposables = CompositeDisposable()
    protected val state: BehaviorSubject<STATE> = BehaviorSubject.create()
    private val events: PublishSubject<Event> = PublishSubject.create()

    override fun state(): Observable<STATE> = state

    override fun attach(actions: Observable<ACTION>) {
        makeStateStream(
            makeStateMutations(actions),
            initialState(),
            ::reduce
        )
        disposables += makeSideEffects(actions)
    }

    override fun detach() {
        disposables.clear()
    }

    /**
     * @return instance of [STATE] representing the initial state
     */
    protected abstract fun initialState(): STATE

    /**
     * Define events that will mutate state within the reducer
     *
     * NB: Operations that cause side-effects rather than state mutations reside in [makeSideEffects]
     *
     * @return [Observable] of [Event] feeding into [reduce]
     */
    protected abstract fun makeStateMutations(actions: Observable<ACTION>): Observable<out Event>

    /**
     * Reduce events to mutated state
     *
     * NB: Should be a pure function without side effects
     */
    protected abstract fun reduce(state: STATE, event: Event): STATE

    /**
     * Define side effects that don't result in state mutations
     *
     * Default implementation results in no-op.
     *
     * NB: Returned subscriptions will be added to [disposables] and disposed in [detach]
     *
     * @return [CompositeDisposable] containing subscriptions that need to be managed
     */
    protected open fun makeSideEffects(actions: Observable<ACTION>) = CompositeDisposable()

    /**
     * Exposes the internal [Event] stream that is being reduced to state over time
     */
    protected fun events(): Observable<Event> = events

    /**
     * Dispatch an event to the reducer
     */
    fun dispatchEvent(event: Event) = events.onNext(event)

    /**
     * Subscribes internal event relay to supplied event stream and sets up [state] stream
     */
    private fun makeStateStream(
        events: Observable<out Event>,
        initialState: STATE,
        reducer: (STATE, Event) -> STATE
    ) {
        disposables += this.events
            .scan(initialState, reducer)
            .distinctUntilChanged()
            .subscribe(state::onNext)
        disposables += events.subscribe(this.events::onNext)
    }
}
