package cc.femto.kommon.mvi

import io.reactivex.Observable

interface View<INTENT : Intent, ACTION : Action, VM> {
    /**
     * Attach the view to view model and action streams
     *
     * Usage:
     * <code>
     *     view.attach(model.viewModel(), model.actions())
     * </code>
     */
    fun attach(viewModel: Observable<VM>, actions: Observable<ACTION>)

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