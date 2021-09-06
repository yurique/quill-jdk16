### 1. run with java8:
```shell
sbt -java-home /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/Contents/Home compile 
```

It compiles.

### 2. run with java11:
```shell
sbt -java-home /Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home compile 
```

It compiles.

### 3. run with java11:
```shell
sbt  -java-home /Library/Java/JavaVirtualMachines/adoptopenjdk-16.jdk/Contents/Home compile
```

An exception is thrown during marco expansion:

```
[error] java.lang.RuntimeException: java.lang.reflect.InaccessibleObjectException: Unable to make field protected transient int java.util.AbstractList.modCount accessible: module java.base does not "opens java.util" to unnamed module @7c694e76
[error] 	at com.twitter.chill.ResourcePool.borrow(ResourcePool.java:44)
[error] 	at com.twitter.chill.KryoPool.toBytesWithClass(KryoPool.java:114)
[error] 	at io.getquill.quat.KryoQuatSerializer$.serialize(KryoQuatSerializer.scala:64)
[error] 	at io.getquill.quat.Quat.serializeJVM(Quat.scala:50)
[error] 	at io.getquill.quat.Quat.serializeJVM$(Quat.scala:50)
[error] 	at io.getquill.quat.Quat$Product.serializeJVM(Quat.scala:148)
...
[error] Caused by: java.lang.reflect.InaccessibleObjectException: Unable to make field protected transient int java.util.AbstractList.modCount accessible: module java.base does not "opens java.util" to unnamed module @7c694e76
[error] 	at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:357)
[error] 	at java.base/java.lang.reflect.AccessibleObject.checkCanSetAccessible(AccessibleObject.java:297)
[error] 	at java.base/java.lang.reflect.Field.checkCanSetAccessible(Field.java:177)
[error] 	at java.base/java.lang.reflect.Field.setAccessible(Field.java:171)
[error] 	at com.esotericsoftware.kryo.serializers.FieldSerializer.buildValidFields(FieldSerializer.java:283)
[error] 	at com.esotericsoftware.kryo.serializers.FieldSerializer.rebuildCachedFields(FieldSerializer.java:218)
[error] 	at com.esotericsoftware.kryo.serializers.FieldSerializer.rebuildCachedFields(FieldSerializer.java:157)
[error] 	at com.esotericsoftware.kryo.serializers.FieldSerializer.<init>(FieldSerializer.java:150)
...
[error]       liftQuery(items).foreach { item =>
[error]                                ^
[error] one error found
```

### 4. Varia

#### Single-item upsert

The single-item upsert compiles regardless of the number of fields in the case class or assigns in the `onConflictUpdate`.

#### Less assigns

In `Repo.scala`, comment out this line:

```scala
_.a5 -> _.a5
```

Run:

```shell
sbt  -java-home /Library/Java/JavaVirtualMachines/adoptopenjdk-16.jdk/Contents/Home compile
```

It will compile.

#### More fields in the case class

Uncomment this line in `Item.scala`:

```scala
//    a14: String
```

Now, compilation will fail even with one assign specified for the `onConflictUpdate` in `Repo.scala`:

```scala
  .onConflictUpdate(_.id)(
    _.a2 -> _.a2,
//    _.a3 -> _.a3,
//    _.a4 -> _.a4,
//    _.a5 -> _.a5
  )
```

#### JVM options

Adding `--add-opens=java.base/java.util=ALL-UNNAMED` to the `.jvmopts` doesn't resolve the issue.

Adding `--illegal-access=permit` resolves the issue, but it is deprecated.
