package cc.femto.mvi

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.subjects.PublishSubject

interface BaseView<ACTION : Action, STATE : Any> : View<ACTION, STATE> {

    /**
     * Keeps track of subscriptions to be disposed when [detach] is called
     */
    val disposables: CompositeDisposable

    /**
     * Implementation for [actions] using a [PublishSubject]
     *
     * Usage:
     * <code>
     *     actionStream.subscribe(actions::onNext)
     * </code>
     */
    val actions: PublishSubject<ACTION>

    override fun actions(): Observable<ACTION> = actions

    /**
     * Subscribes [render] function to [state] stream
     */
    override fun attach(state: Observable<STATE>) {
        disposables += render(state)
    }

    /**
     * Detach view by disposing all subscriptions
     *
     * Usage:
     * This should be called from your component's
     * opposite lifecycle method to [attach]
     */
    fun detach() {
        disposables.clear()
    }

    /**
     * Render view state by subscribing to [state] stream
     *
     * Usage:
     * Render partial state by mapping the stream to
     * relevant parts and using operators like [Observable.distinctUntilChanged]
     *
     * @param state stream passed in from [attach]
     * @return [CompositeDisposable] which will be disposed in [detach]
     */
    fun render(state: Observable<STATE>): CompositeDisposable
}
