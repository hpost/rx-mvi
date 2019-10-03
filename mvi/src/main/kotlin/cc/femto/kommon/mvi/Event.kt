package cc.femto.kommon.mvi

/**
 * The model uses a reducer function that produces an updated state
 * based on the previous state and a state mutation event.
 *
 * <code>
 *     reducer: (STATE, EVENT) -> STATE
 * </code>
 *
 * State mutation events are internal to the model and are not exposed to the view.
 */
interface Event
