package cc.femto.kommon.mvi

/**
 * The model uses a reducer function that produces an updated view model
 * based on the previous view model and a state mutation event.
 * <code>
 *     reducer: (VM, EVENT) -> VM
 * </code>
 * State mutations are internal to the model and are not exposed to the view.
 */
interface Event
