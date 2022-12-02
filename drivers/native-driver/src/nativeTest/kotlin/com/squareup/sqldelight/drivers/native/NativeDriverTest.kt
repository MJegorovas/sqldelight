package com.squareup.sqldelight.drivers.native

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import co.touchlab.sqliter.DatabaseFileContext.deleteDatabase
import com.squareup.sqldelight.driver.test.DriverTest
import kotlin.test.Test

class NativeDriverTest : DriverTest() {
  override fun setupDatabase(schema: SqlSchema): SqlDriver {
    val name = "testdb"
    deleteDatabase(name)
    return NativeSqliteDriver(schema, name)
  }

  @Test
  fun executeAttachDatabase() {
    driver.execute(null, "ATTACH DATABASE ? AS test", 1) {
      bindString(0, "testdb")
    }
    driver.executeQuery(null, "PRAGMA database_list", { cursor ->
      // For result debugging
//      while (cursor.next()) {
//        println(cursor.getString(0))
//        println(cursor.getString(1))
//        println(cursor.getString(2))
//      }
//      assert(true)

      cursor.next() // This moves to row with 'main' database
      assert(cursor.next()) // Should move to attached database row
    }, 0)
  }
}

class NativeDriverMemoryTest : DriverTest() {
  override fun setupDatabase(schema: SqlSchema): SqlDriver {
    return inMemoryDriver(schema)
  }
}
