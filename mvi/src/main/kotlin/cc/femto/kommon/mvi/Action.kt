package cc.femto.kommon.mvi

/**
 * Actions flow from the model to the view.
 * Unlike the view model, actions do not persist over time.
 *
 * Example:
 * In order to finish an activity, the model
 * would send a corresponding action to the view.
 */
interface Action