package com.yurique.quill

import io.getquill._

object Repo {

  val ctx = new SqlMirrorContext(PostgresDialect, SnakeCase)

  import ctx._

  private def insertOrUpdate(item: Item) =
    run {
      query[Item]
        .insert(lift(item))
        .onConflictUpdate(_.id)(
          _.a1 -> _.a1,
          _.a2 -> _.a2,
          _.a3 -> _.a3,
          _.a4 -> _.a4,
        )
    }

  private def insertOrUpdate(items: List[Item]) =
    run {
      liftQuery(items).foreach { item =>
        query[Item]
          .insert(item)
          .onConflictUpdate(_.id)(
            _.a1 -> _.a1,
            _.a2 -> _.a2,
            _.a3 -> _.a3,
            _.a4 -> _.a4,
            _.a5 -> _.a5,
          )
      }
    }

}
