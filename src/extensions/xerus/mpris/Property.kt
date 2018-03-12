package xerus.mpris

import org.freedesktop.DBus

private class PropertyProvider {
    operator fun provideDelegate(
            target: AbstractMPRISPlayer,
            prop: KProperty<*>
    ): ReadOnlyProperty<AbstractMPRISPlayer, T> {
        target.properties
        return ResourceDelegate()
    }
}

class DBusProperty<T>(): ReadWriteProperty<AbstractMPRISPlayer, T> {

    override fun setValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>, value: T) {

    }

    override fun getValue(thisRef: AbstractMPRISPlayer, property: KProperty<*>): T {

    }

}
