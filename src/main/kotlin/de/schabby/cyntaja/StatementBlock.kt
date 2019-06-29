package de.schabby.cyntaja

class StatementBlock(val parent:VariableContainer) : Writable {
    val list = mutableListOf<Statement>()
    override fun writeCode(): String {
        val sb = StringBuilder()
        list.forEach {s ->
            sb.append("   ")
            sb.append(s.writeCode())
            sb.append(";\n")
        }
        return sb.toString()
    }

    fun functionCall(name:String, vararg params: Expression) {
        val fb = FunctionCall(name)
        params.forEach {
            fb.parameters.add(it)
        }
        list.add(fb)
    }

    fun returnStatement(exp: Expression) {
        val fb = Return(exp)
        list.add(fb)
    }


    fun functionCall(func: Function, vararg params: Expression) {
        val fb = FunctionCall(func.name)
        params.forEach {
            fb.parameters.add(it)
        }
        list.add(fb)
    }


    fun variableDeclaration(varType:Type, varName:String) : Variable {
        println("in variableDeclaration()")
        val s = VariableDeclaration(varType, varName)
        list.add(s)
        return s.variable
    }

    fun variableDeclaration(varType:Type, varName:String, assignmentExp: Expression) : Variable {
        val s = VariableDeclaration(varType, varName, assignmentExp)
        list.add(s)
        return s.variable
    }

    fun ifStatement(booleanExp: Expression, block:IfStatment.()->Unit) {
        val ifStmt = IfStatment(booleanExp, this)
        ifStmt.apply(block)
        list.add(ifStmt)
    }


    fun assignment(lvalue:LValue, exp: Expression) {
        list.add(AssignmentStatement(lvalue, exp))
    }

    fun findStruct(name:String) : Struct = parent.findStruct(name)

    fun findVar(name:String) : Variable {
        list.forEach { s ->
            if (s is VariableDeclaration && s.variable.name.equals(name)) return s.variable
        }

        return parent.findVar(name)
    }

}