package de.schabby.cyntaja

import de.schabby.cyntaja.Type.Companion.i32
import de.schabby.cyntaja.tools.malloc
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MallocTest {

    @Test
    @DisplayName("compile and run HelloWorld.c")
    fun helloWorld() {

        val p = Program().build {
            include(Include.STDIO)
            include(Include.STDINT)
            include(Include.STDLIB)

            function("main") {
                returnType = i32
                body {
                    val intPtr = variableDeclaration(i32.asPointer, "intPtr", malloc(i32, 100))
                    functionCall("printf", StringLiteral("address %p\\n"), VarIdentifier(intPtr))
                    returnStatement(Literal("0"))
                }
            }
        }

        gcc.writeToFileAndCompile(p, "gcc", "build/malloc.c", "build/malloc")
        val lines = runExecutable("build/malloc")

        Assertions.assertThat(lines[0].startsWith("address "))
    }


}