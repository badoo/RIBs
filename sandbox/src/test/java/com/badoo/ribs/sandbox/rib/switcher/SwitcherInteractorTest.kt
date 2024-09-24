package com.badoo.ribs.sandbox.rib.switcher

// TODO: Uncomment once Mockito issues with BackStack are fixed.
//  This blocked the update to AGP 8.2.2
//@RunWith(RobolectricTestRunner::class)
//class SwitcherInteractorTest {
//
//    private val backStack: BackStack<Configuration> = mock()
//    private val dialogToTestOverlay: DialogToTestOverlay = mock()
//
//    private val viewEventRelay = PublishRelay.create<Event>()
//    private lateinit var interactor: SwitcherInteractor
//    private lateinit var interactorTestHelper: InteractorTestHelper<SwitcherView>
//
//    @Before
//    fun setup() {
//        interactor = SwitcherInteractor(
//            buildParams = emptyBuildParams(),
//            backStack = backStack,
//            dialogToTestOverlay = dialogToTestOverlay,
//            transitions = Observable.just(SETTLED),
//            transitionSettled = { true }
//        )
//
//        interactorTestHelper = createInteractorTestHelper(interactor, viewEventRelay)
//    }
//
//    @Test
//    fun `router open overlay dialog when show overlay dialog clicked`(){
//        interactorTestHelper.moveToStateAndCheck(STARTED) {
//            viewEventRelay.accept(Event.ShowOverlayDialogClicked)
//
//            verify(backStack).pushOverlay(Overlay.Dialog)
//        }
//    }
//
//    @Test
//    fun `router open blocker when show blocker clicked`(){
//        interactorTestHelper.moveToStateAndCheck(STARTED) {
//            viewEventRelay.accept(Event.ShowBlockerClicked)
//
//            verify(backStack).push(Content.Blocker)
//        }
//    }
//
//    @Test
//    fun `skip view event when view not resumed`(){
//        interactorTestHelper.moveToStateAndCheck(CREATED) {
//            viewEventRelay.accept(Event.ShowBlockerClicked)
//
//            verify(backStack, never()).push(Content.Blocker)
//        }
//    }
//}
