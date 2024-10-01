package com.badoo.ribs.example.root

// TODO: Uncomment once Mockito issues with BackStack are fixed.
//  This blocked the update to AGP 8.2.2
//@ExtendWith(RxSchedulerExtension::class)
//class RootInteractorAuthStateTest {
//
//    private val backStack: BackStack<Configuration> = mock()
//
//    private val stateRelay = PublishRelay.create<AuthState>()
//    private val authDataSource = mock<AuthDataSource>().apply {
//        whenever(authUpdates).thenReturn(stateRelay)
//    }
//    private lateinit var interactor: RootInteractor
//    private lateinit var interactorTestHelper: InteractorTestHelper<Nothing>
//
//    @BeforeEach
//    fun setup() {
//        interactor = RootInteractor(
//            buildParams = emptyBuildParams(),
//            backStack = backStack,
//            authDataSource = authDataSource,
//            networkErrors = PublishRelay.create()
//        )
//        interactorTestHelper = InteractorTestHelper(interactor)
//    }
//
//    @ParameterizedTest
//    @MethodSource("data")
//    fun `an example test with some conditions should pass`(
//        authState: AuthState,
//        expectedConfiguration: Configuration
//    ) {
//        interactorTestHelper.moveToStateAndCheck(Lifecycle.State.CREATED) {
//            stateRelay.accept(authState)
//
//            verify(backStack).replace(expectedConfiguration)
//        }
//    }
//
//    companion object {
//        @JvmStatic
//        fun data(): Stream<Arguments> = Stream.of(
//            Arguments.of(AuthState.Unauthenticated, Configuration.Content.LoggedOut),
//            Arguments.of(AuthState.Anonymous, Configuration.Content.LoggedIn),
//            Arguments.of(AuthState.Authenticated(""), Configuration.Content.LoggedIn)
//        )
//    }
//}
