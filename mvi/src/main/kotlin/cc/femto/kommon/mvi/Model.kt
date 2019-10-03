package cc.femto.kommon.mvi

import io.reactivex.Observable

interface Model<INTENT : Intent, STATE> {
    /**
     * Attach the model to an intent stream
     *
     * NB: Always attach model to view before attaching intent stream to model
     *
     * Usage:
     * <code>
     *     override fun onCreate(savedInstanceState: Bundle?) {
     *         super.onCreate(savedInstanceState)
     *         view.attach(model.state().observeOn(AndroidSchedulers.mainThread()))
     *         model.attach(view.intents())
     *     }
     * </code>
     */
    fun attach(intents: Observable<INTENT>)

    /**
     * Detach the model from previously attached intent stream
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
