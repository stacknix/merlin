# Merlin
Merlin is a Android database which is primarily designed for live networking objects.
It provides the live object which is directly mirror the actual state of object.
As mentioned It designed for work with networking that doesn't mean can't be used for general purpose database storage.
In a nutshell deal directly with live managed object and put all other component like networking, caching and API's aside.

> Note this is still under development with pre-release beta.

### Features
* NoSQL Database
* Live Objects
* Built in Networking
* Direct Caching
* Backed by multiple database

## Installation
Add it in your root build.gradle at the end of repositories:

```shell
allprojects {
  repositories {
      ...
      maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency in your app module:

```shell
implementation 'com.github.stacknix:merlin:beta-0.0.1'
```
Additionally, you can use annotation processor for code generation.
If you are using kotlin use kapt. (optional)

```shell
kapt 'com.github.stacknix:merlin:beta-0.0.1'
```

## Getting Started
Create your data models, choose your preferred language, 
for this example we are using Kotlin.

```kotlin
// annotated with Model pass any name you want, must be unique though.

@Model("product")
data class Product(var id: Long, var name: String, var price: Float) : MerlinObject()
```

For java users:

```java
@Model("product")
public class Product extends MerlinObject {
    public long id;
    public String name;
    public float price;
}
```

Add many models as you needs in same manner. Make sure always use unique
model name, else compiler will through exception. 
You need to perform build once you add your very first model,
this will generate MerlinDatabase class which we need for
initialization.

## Initialize

Finally, you need to initialize database. You can simply do that once
for all in you Application class.

```kotlin
class CartApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        MerlinDatabase.init(this)
        ...
    }
}
```

## All set!
Now you can simply use your model for database operations. 

#### Create
Once you initialize your data class use save method to create record.

```kotlin
Product(1, "Apple", 3.2f).save()
```

#### Read All
Following way, you can read all the record from product model.
```kotlin
val result = Merlin.where(Product::class.java).find()
```

#### Listen
Use listen interface for observe live objects change.
```kotlin
result.listen {
   Log.i("All Count", it.size.toString())
}
```

#### Delete All
```kotlin
result.deleteAll()
```

#### Filter Result
You can apply built in query filter to fit your requirement. 
In following example we are filtering result where name is Apple.
This also apply on listen interface, you will be only observing
changes where name is Apple only.

```kotlin
val result = Merlin.where(Project::class.java).equal("name", "Apple").find()

result.listen {
    Log.i("Apple Count", it.size.toString())
}
```


## More Documentation Coming Soon... :)
