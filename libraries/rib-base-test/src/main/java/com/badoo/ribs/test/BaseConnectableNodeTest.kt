package com.badoo.ribs.test

@Suppress("FunctionNaming", "IllegalIdentifier")
abstract class BaseConnectableNodeTest {

    abstract fun `WHEN child emit some output before it is attached to parent THEN parent receive the output after child attach finished`()

    abstract fun `WHEN child is attached and emit some output THEN parent receive the exact output`()

    abstract fun `WHEN child emit multiple outputs before it is attached to parent THEN parent receive the output after attach finished in correct order`()

    abstract fun `WHEN child emit output before it is attached to parent and then after it is attached to parent THEN parent receive the output in correct order`()
}
