package  com.github.kondury.flashcards.cor.handlers

abstract class AbstractCorExec<T>(
    override val title: String,
    override val description: String = "",
    private val blockActiveIf: suspend T.() -> Boolean = { true },
    private val blockOnException: suspend T.(Throwable) -> Unit = {},
): CorExec<T> {
    protected abstract suspend fun handle(context: T)

    private suspend fun isActive(context: T): Boolean = context.blockActiveIf()
    private suspend fun onException(context: T, e: Throwable) = context.blockOnException(e)

    override suspend fun exec(context: T) {
        if (isActive(context)) {
            try {
                handle(context)
            } catch (e: Throwable) {
                onException(context, e)
            }
        }
    }
}
