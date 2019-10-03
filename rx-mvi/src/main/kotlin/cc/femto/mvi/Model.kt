package cc.femto.mvi

import io.reactivex.Observable

interface Model<ACTION : Action, STATE> {
    /**
     * Attach the model to an action stream
     *
     * NB: Always attach model to view before attaching action stream to model
     *
     * Usage:
     * <code>
     *     override fun onCreate(savedInstanceState: Bundle?) {
     *         super.onCreate(savedInstanceState)
     *         view.attach(model.state().observeOn(AndroidSchedulers.mainThread()))
     *         model.attach(view.actions())
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
