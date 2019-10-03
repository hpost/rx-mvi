package cc.femto.kommon.mvi

import io.reactivex.Observable

interface View<INTENT : Intent, STATE> {
    /**
     * Attach the view to a state stream
     *
     * Usage:
     * <code>
     *     view.attach(model.state().observeOn(AndroidSchedulers.mainThread()))
     * </code>
     */
    fun attach(state: Observable<STATE>)

    /**
     * Obtain intent stream for attaching to model
     *
     * Usage:
     * <code>
     *     model.attach(view.intents())
     * </code>
     */
    fun intents(): Observable<INTENT>
}
