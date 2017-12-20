package cc.femto.kommon.mvi

/**
 * The model uses a reducer function that produces an updated view model
 * based on the previous view model and an event.
 * <code>
 *     reducer: (VM, EVENT) -> VM
 * </code>
 * Events are internal to the model and do not leave its boundaries.
 */
interface Event
