package com.badoo.ribs.test

@Suppress("FunctionNaming", "IllegalIdentifier")
abstract class BaseNodeConnectorTest {

    abstract fun `GIVEN nodeConnector onAttached is not called WHEN output is accepted THEN accepted output do not reach observer`()

    abstract fun `GIVEN and output is accepted before onAttached WHEN nodeConnector onAttached is called THEN accepted output reach the observer`()

    abstract fun `GIVEN nodeConnector is attached WHEN output is accepted THEN every accepted output reach the observer`()

    abstract fun `GIVEN outputs accepted before and after onAttached WHEN node is attached THEN every accepted output reach the observer`()

    abstract fun `WHEN nodeConnector onAttached is called twice THEN error is raised`()

    abstract fun `GIVEN multiple observers and output is accepted before OnAttached WHEN nodeConnector onAttached is called THEN every accepted output reach the observers`()

    abstract fun `GIVEN multiple observers and nodeConnector is attached WHEN output is accepted THEN every accepted output reach the observer`()

    abstract fun `GIVEN multiple observers that subscribe before and after onAttached  and outputs accepted before and after onAttached WHEN node is attached THEN every accepted output reach the observer`()

    abstract fun `WHEN multiple output are accepted from multiple threads THEN output is correctly received when onAttached is called`()

    abstract fun `WHEN accept and onAttached are called by different thread at the same time THEN output is the expected`()
}
