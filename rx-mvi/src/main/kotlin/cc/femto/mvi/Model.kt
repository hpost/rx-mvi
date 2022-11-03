package cc.femto.mvi

import io.reactivex.rxjava3.core.Observable

interface Model<ACTION : Action, STATE : Any> {
    /**
     * Attach the model to an action stream
     *
     * Usage:
     * <code>
     *     override fun onCreate(savedInstanceState: Bundle?) {
     *         super.onCreate(savedInstanceState)
     *         model.attach(view.actions())
     *         view.attach(model.state().observeOn(AndroidSchedulers.mainThread()))
     *     }
     * </code>
     */
    fun attach(actions: Observable<ACTION>)

    /**
     * Detach the model from previously attached action stream
     *
     * Usage:
     * <code>
     *     override fun onDestroy() {
     *         model.detach()
     *         super.onDestroy()
     *     }
     * </code>
     */
    fun detach()

    /**
     * State stream
     *
     * NB: Observe on main thread before touching UI
     */
    fun state(): Observable<STATE>
}
