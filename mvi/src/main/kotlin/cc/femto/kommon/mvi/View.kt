package cc.femto.kommon.mvi

import io.reactivex.Observable

interface View<INTENT : Intent, VM> {
    /**
     * Attach the view to a view model stream
     *
     * Usage:
     * <code>
     *     view.attach(model.viewModel().observeOn(AndroidSchedulers.mainThread()))
     * </code>
     */
    fun attach(viewModel: Observable<VM>)

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