# Tutorial #3 - solutions

### HelloWorldView
```kotlin
interface HelloWorldView : RibView,
    ObservableSource<Event>,
    Consumer<ViewModel> {

    sealed class Event {
        object ButtonClicked : Event()
    }

    data class ViewModel(
        val titleText: Text,
        val welcomeText: Text
    )
}
```

### HelloWorldViewImpl
```kotlin
class HelloWorldViewImpl private constructor(
    context: Context, attrs: AttributeSet? = null, defStyle: Int = 0, private val events: PublishRelay<Event>
) : ConstraintLayout(context, attrs, defStyle),
    HelloWorldView,
    ObservableSource<Event> by events,
    Consumer<ViewModel> {

    @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyle: Int = 0
    ) : this(context, attrs, defStyle, PublishRelay.create<Event>())

    override val androidView = this
    private val title: TextView by lazy { findViewById<TextView>(R.id.hello_world_title) }
    private val welcome: TextView by lazy { findViewById<TextView>(R.id.hello_world_welcome) }
    private val button: Button by lazy { findViewById<Button>(R.id.hello_world_button) }

    override fun onFinishInflate() {
        super.onFinishInflate()
        button.setOnClickListener { events.accept(Event.ButtonClicked) }
    }

    override fun accept(vm: ViewModel) {
        title.text = vm.titleText.resolve(context)
        welcome.text = vm.welcomeText.resolve(context)
    }
}
```

### HelloWorldInteractor
```kotlin
class HelloWorldInteractor(
    user: User,
    config: HelloWorld.Configuration,
    savedInstanceState: Bundle?,
    router: Router<Configuration, Nothing, Content, Nothing, HelloWorldView>
) : Interactor<Configuration, Content, Nothing, HelloWorldView>(
    savedInstanceState = savedInstanceState,
    router = router,
    disposables = null
) {

    override fun onViewCreated(view: HelloWorldView, viewLifecycle: Lifecycle) {
        super.onViewCreated(view, viewLifecycle)
        view.accept(initialViewModel)
    }
    
    private val initialViewModel =
        HelloWorldView.ViewModel(
            titleText = Text.Resource(R.string.hello_world_title, user.name()),
            welcomeText = config.welcomeMessage
        )
}
```

### HelloWorld
```kotlin
interface GreetingsContainer : Rib {

    interface Dependency {
        fun user(): User
        fun config(): Config
    }

    data class Config {
        val welcomeMessage: Text
    }

    // remainder the same
}
```

### HelloWorldModule
```kotlin
@dagger.Module
internal object HelloWorldModule {

    @HelloWorldScope
    @Provides
    @JvmStatic
    internal fun interactor(
        user: User,
        config: HelloWorld.Configuration,
        router: HelloWorldRouter
    ): HelloWorldInteractor =
        HelloWorldInteractor(
            user = user,
            config = config,
            router = router
        )

    // remainder the same
}
```

### GreetingsContainer
```kotlin
interface GreetingsContainer : Rib {

    interface Dependency {
        fun user(): User
        fun greetingsContainerOutput(): Consumer<Output>
    }

    sealed class Output {
        data class GreetingsSaid(val greeting: String) : Output()
    }
}
```

### RootActivity
```kotlin
/** The tutorial app's single activity */
class RootActivity : RibActivity() {

    // remainder the same

    override fun createRib(savedInstanceState: Bundle?): Node<*> =
        GreetingsContainerBuilder(
            object : GreetingsContainer.Dependency {
                override fun user(): User =
                    User.DUMMY

                override fun greetingsContainerOutput(): Consumer<GreetingsContainer.Output> =
                    Consumer { output ->
                        when (output) {
                            is GreetingsContainer.Output.GreetingsSaid -> {
                                Snackbar.make(rootViewGroup, output.greeting, Snackbar.LENGTH_SHORT).show()
                            }
                        }
                    }
            }
        ).build()
}
```

