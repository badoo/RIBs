package com.badoo.ribs.example.rib.main_foo_bar

import com.nhaarman.mockitokotlin2.mock

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class MainFooBarRouterTest {

  private var interactor: MainFooBarInteractor = mock()
  private var router: MainFooBarRouter? = null

  @Before
  fun setup() {
    router = MainFooBarRouter(
      savedInstanceState = null
    )
  }

  @After
  fun tearDown() {
  }

  /**
   * TODO: Add real tests.
   */
  @Test
  fun `an example test with some conditions should pass`() {
  }
}
