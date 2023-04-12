package com.codebootup.codegenerator

data class Root(val name: String, val children: List<Children>)
data class Children(val name: String)
