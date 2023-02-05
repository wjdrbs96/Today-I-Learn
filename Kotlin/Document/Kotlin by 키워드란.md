## `Kotlin by 키워드란?`

```kotlin
interface IWindow {
    fun getWidth() : Int
    fun getHeight() : Int
}
```

```kotlin
class TransparentWindow : IWindow {
    override fun getWidth(): Int {
        return 100
    }

    override fun getHeight() : Int{
        return 150
    }
}
```

`IWindow` 인터페이스를 구현하면 일반적으론 위와 같은 상황이 될 것이다. 

<br>

```kotlin
class UI(window: IWindow) : IWindow {
    val mWindow: IWindow = window

    override fun getWidth(): Int {
        return mWindow.getWidth()
    }

    override fun getHeight(): Int {
        return mWindow.getHeight()
    }
}
```

그런데 일반적인 방법을 사용하여 인터페이스를 구현하는 것이 아니라 `UI -> IWindow`를 참조하고 파라미터로 들어온 타입의 값을 get / set을 통해 반환해주는 것이다. 

<br>

```kotlin
fun main() {
    val window: IWindow = TransparentWindow()
    val ui = UI(window)
    println("Width : ${ui.getWidth()}, height: ${ui.getHeight()}")
}
```

즉, UI 객체의 파라미터로 `TransparentWindow`을 넣어서 생성하면 `UI` 객체의 값들을 꺼낼 때, `TransparentWindow` 타입의 값을 꺼내올 수 있다.

<br>

```kotlin
class UI(window: IWindow) : IWindow by window
```

즉, 이렇게만 써주면 파라미터로 `IWindow` 구현체가 들어왔을 때 UI 객체에서 손쉽게 해당 구현체의 메소드, 값들을 사용할 수 있게 된다.