import models.BirdsCommand
import kotlin.reflect.KClass

class UnknownRequestException(cls: KClass<*>) : RuntimeException("Class $cls can't be mapped and not supported")

class UnknownCommandException(cmd: BirdsCommand) : RuntimeException("Command $cmd can't be mapped and not supported")

class UnsupportedCommandException(cmd: BirdsCommand) : RuntimeException("Command $cmd is not supported")

